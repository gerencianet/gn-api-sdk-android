package br.com.gerencianet.sdk.android;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private final static String version = "1.0.2";
    private final Map<String, Object> conf = new HashMap<>();
    private JsonObject endpoints;
    private JsonObject urls;

    public Config(Map<String, Object> options, JsonObject config) throws Exception {

        if (config.has("ENDPOINTS")) {
            this.endpoints = (JsonObject) config.get("ENDPOINTS");
            if (setPix(options)) {
                if (this.endpoints.has("PIX")) {
                    this.endpoints = (JsonObject) this.endpoints.get("PIX");
                }
            } else {
                if (this.endpoints.has("DEFAULT")) {
                    this.endpoints = (JsonObject) this.endpoints.get("DEFAULT");
                }
            }
        } else
            throw new Exception("Problems to get ENDPOINTS in file config.json");

        if (config.has("URL")) {
            this.urls = (JsonObject) config.get("URL");
            options.put("tokenizer", this.urls.get("TOKENIZER").getAsString());
            if (setPix(options)) {
                if (this.urls.has("PIX")) {
                    this.urls = (JsonObject) this.urls.get("PIX");
                }
            } else if (this.urls.has("DEFAULT")) {
                this.urls = (JsonObject) this.urls.get("DEFAULT");
            }
        } else
            throw new Exception("Problems to get URLs in file config.json");

        this.setConf(options);
    }

    public JsonObject getEndpoints() {
        return endpoints;
    }

    public void setConf(Map<String, Object> options) {
        boolean sandbox = false;
        boolean debug = false;

        if (options.containsKey("sandbox"))
            sandbox = (Boolean) options.get("sandbox");
        if (options.containsKey("debug"))
            debug = (Boolean) options.get("debug");

        this.conf.put("sandbox", sandbox);
        this.conf.put("debug", debug);

        if (options.containsKey("client_id"))
            this.conf.put("clientId", options.get("client_id"));
        if (options.containsKey("client_secret"))
            this.conf.put("clientSecret", options.get("client_secret"));
        if (options.containsKey("pix_cert"))
            this.conf.put("certificadoPix", options.get("pix_cert"));
        if (options.containsKey("partner_token"))
            this.conf.put("partnerToken", options.get("partner_token"));
        if (options.containsKey("account_id"))
            this.conf.put("accountId", options.get("account_id"));
        if (options.containsKey("tokenizer"))
            this.conf.put("tokenizerUrl", options.get("tokenizer"));
        if (options.containsKey("url")) {
            this.conf.put("baseUri", options.get("url"));
        } else {
            String baseUri = this.urls.get("production").getAsString();
            if (sandbox)
                baseUri = this.urls.get("sandbox").getAsString();

            this.conf.put("baseUri", baseUri);
        }

        if (options.containsKey("x-skip-mtls-checking")) {
            this.conf.put("headers", options.get("x-skip-mtls-checking"));
        }
    }

    public Map<String, Object> getOptions() {
        return this.conf;
    }

    public static String getVersion() {
        return Config.version;
    }

    public boolean setPix(Map<String, Object> options) {
        return options.containsKey("pix_cert");
    }

}