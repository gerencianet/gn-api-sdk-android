package br.com.gerencianet.sdk.android;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.spongycastle.crypto.AsymmetricBlockCipher;
import org.spongycastle.crypto.engines.RSAEngine;
import org.spongycastle.crypto.params.AsymmetricKeyParameter;
import org.spongycastle.crypto.util.PublicKeyFactory;

public class PaymentToken {

      private final Config config;
      private Request request;

      public PaymentToken(Config config) {
            this.config = config;
      }

      public JsonObject generate(Map<String, Object> card) throws Exception {
            card.put("salt", getTokenizer().get("data").getAsString());
            JsonObject key = getKey();
            String encrypt = this.encrypt(new Gson().toJson(card), key.get("data").getAsString());
            Map<String, String> body = new HashMap<>();
            body.put("data", encrypt);
            return saveCard(new Gson().toJsonTree(body).getAsJsonObject());
      }

      private JsonObject saveCard(JsonObject data) throws Exception {
            String urlString;
            if ((boolean) this.config.getOptions().get("sandbox"))
                  urlString = this.config.getOptions().get("baseUri") + "/v1/card";
            else
                  urlString = "https://tokenizer.gerencianet.com.br/card";

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("account-code", Objects.requireNonNull(this.config.getOptions().get("accountId")).toString());
            this.request = new Request("POST", conn);
            return this.request.send(data);

      }

      private JsonObject getKey() throws Exception {
            JsonObject endpoints = this.config.getEndpoints();
            JsonObject endpoint = endpoints.getAsJsonObject("pubKey");
            String urlString = this.config.getOptions().get("baseUri")
                        + endpoint.get("route").getAsString()
                        + this.config.getOptions().get("accountId");
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            this.request = new Request("GET", conn);
            return this.request.send(null);
      }

      private JsonObject getTokenizer() throws Exception {
            String url = this.config.getOptions().get("tokenizerUrl") + "";
            URL link = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setRequestProperty("account-code", (String) this.config.getOptions().get("accountId"));
            this.request = new Request("GET", conn);
            return this.request.send(null);
      }

      public String encrypt(String data, String key) throws Exception {
            key = key.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\s", "");
            AsymmetricKeyParameter asymmetricKeyParameter = PublicKeyFactory.createKey(Base64.decode(key, Base64.NO_WRAP));
            AsymmetricBlockCipher asymmetricBlockCipher = new RSAEngine();
            asymmetricBlockCipher = new org.spongycastle.crypto.encodings.PKCS1Encoding(asymmetricBlockCipher);
            asymmetricBlockCipher.init(true, asymmetricKeyParameter);
            byte[] dataBytes = data.getBytes();
            byte[] hexEncodedCipher = asymmetricBlockCipher.processBlock(dataBytes, 0, dataBytes.length);
            return Base64.encodeToString(hexEncodedCipher, Base64.NO_WRAP);
      }

}
