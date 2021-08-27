package br.com.gerencianet.mobile;

import android.content.res.AssetManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Date;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import com.google.gson.JsonObject;
import br.com.gerencianet.mobile.exceptions.AuthorizationException;

public class APIRequest {
    private final Request requester;
    private Auth authenticator;
    private final JsonObject body;
    private final Config config;
    private final String authenticateRoute;
    private final String authenticateMethod;
    private final AssetManager cert;

    public APIRequest(String method, String route, JsonObject body, Config config, AssetManager cert) throws Exception {
        this.config = config;
        this.authenticateMethod = config.getEndpoints().getAsJsonObject("authorize").get("method").getAsString();
        this.authenticateRoute = config.getEndpoints().getAsJsonObject("authorize").get("route").getAsString();
        this.cert = cert;
        this.auth();

        String url = config.getOptions().get("baseUri") + route;
        URL link = new URL(url);

        if (config.getOptions().containsKey("certificadoPix")) {
            HttpsURLConnection client = (HttpsURLConnection) link.openConnection();
            client.setSSLSocketFactory(
                    this.addCert((cert.open((String) config.getOptions().get("certificadoPix")))).getSocketFactory());
            this.requester = new Request(method, client);

        } else {
            HttpURLConnection client = (HttpsURLConnection) link.openConnection();
            this.requester = new Request(method, client);
        }

        if (config.getOptions().containsKey("partnerToken"))
            this.requester.addHeader("partner-token", (String) config.getOptions().get("partnerToken"));
        if (config.getOptions().containsKey("headers"))
            this.requester.addHeader("x-skip-mtls-checking", (String) config.getOptions().get("headers"));


        this.body = body;
    }

    public void auth() throws Exception {
        this.authenticator = new Auth(config.getOptions(), authenticateMethod, authenticateRoute, cert);
    }

    public JsonObject send() throws Exception {

        Date expiredDate = this.authenticator.getExpires();

        if (this.authenticator.getExpires() == null || expiredDate.compareTo(new Date()) <= 0) {
            this.authenticator.authorize();
        }

        this.requester.addHeader("Authorization", "Bearer " + this.authenticator.getAccessToken());

        try {
            return this.requester.send(this.body);
        } catch (AuthorizationException e) {
            this.auth();
            this.authenticator.authorize();
            return this.requester.send(body);
        }
    }


    private SSLContext addCert(InputStream keyStoreData) throws Exception {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(keyStoreData, "".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, "".toCharArray());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
        return sslContext;
    }
}
