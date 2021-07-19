package br.com.gerencianet.sdk.android;

import android.content.res.AssetManager;
import android.util.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import com.google.gson.JsonObject;
import br.com.gerencianet.sdk.android.exceptions.AuthorizationException;
import br.com.gerencianet.sdk.android.exceptions.GerencianetException;

public class Auth {
    private String accessToken;
    private Date expires;
    private final Request request;
    private final JsonObject authBody;
    private final String authCredentials;

    public Auth(Map<String, Object> credentials, String method, String authorizeRoute, AssetManager inputStream) throws Exception {

        if (!credentials.containsKey("clientId") || !credentials.containsKey("clientSecret")) {
            throw new Exception("Client_Id or Client_Secret not found");
        }

        String url = credentials.get("baseUri") + authorizeRoute;
        URL link = new URL(url);

        if (credentials.containsKey("certificadoPix")) {
            HttpsURLConnection client = (HttpsURLConnection) link.openConnection();
            client.setSSLSocketFactory(
                    this.addCert(inputStream.open((String) credentials.get("certificadoPix"))).getSocketFactory());
            this.request = new Request(method, client);

        } else {
            HttpURLConnection client = (HttpsURLConnection) link.openConnection();
            this.request = new Request(method, client);
        }

        if (credentials.containsKey("partnerToken")) {
            this.request.addHeader("partner-token", (String) credentials.get("partnerToken"));
        }

        authBody = new JsonObject();
        authBody.addProperty("grant_type", "client_credentials");

        String auth = credentials.get("clientId") + ":"
                + credentials.get("clientSecret");

        this.authCredentials = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
    }


    public void authorize() throws IOException, AuthorizationException, GerencianetException {
        this.request.addHeader("Authorization", "Basic " + this.authCredentials);
        JsonObject response = this.request.send(authBody);
        this.accessToken = response.get("access_token").getAsString();
        this.expires = new Date(new Date().getTime() + response.get("expires_in").getAsLong());
    }

    public Date getExpires() {
        return this.expires;
    }

    public String getAccessToken() {
        return this.accessToken;
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
