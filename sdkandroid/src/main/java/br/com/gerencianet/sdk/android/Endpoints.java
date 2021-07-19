package br.com.gerencianet.sdk.android;


import android.content.res.AssetManager;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class Endpoints {
    private APIRequest requester;
    private Config config;
    private AssetManager cert;

    public Endpoints(Map<String, Object> options, AssetManager cert) throws Exception {
        this.cert = cert;
        this.load(options);
    }

    public Endpoints(Map<String, Object> options) throws Exception {
        this.load(options);
    }

    private void load(Map<String, Object> options) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream in = classLoader.getResourceAsStream("config.json");
        JsonReader reader = new JsonReader(new InputStreamReader(in));
        JsonObject config = new Gson().fromJson(reader, JsonObject.class);
        this.config = new Config(options, config);
    }

    public Map<String, Object> call(String endpoint, Map<String, String> params, Map<String, Object> mapBody)
            throws Exception {
        JsonObject body = new Gson().toJsonTree(mapBody).getAsJsonObject();
        JsonObject response = kernelCall(endpoint, params, body);
        Map<String, Object> map = new Gson().fromJson(response.toString(), Map.class);
        return map;
    }

    private JsonObject kernelCall(String endpointName, Map<String, String> params, JsonObject body) throws Exception {
        JsonObject endpoints = this.config.getEndpoints();

        if (!endpoints.has(endpointName))
            throw new Exception("nonexistent endpoint");

        JsonObject endpoint = endpoints.getAsJsonObject(endpointName);

        String route = getRoute(endpoint, params);

        route += getQueryString(params);

        if (this.requester == null)
            requester = new APIRequest(endpoint.get("method").getAsString(), route, body, this.config, this.cert);

        JsonObject response = this.requester.send();
        this.requester = null;

        return response;
    }

    private String getQueryString(Map<String, String> params) throws UnsupportedEncodingException {
        Set<Entry<String, String>> set = params.entrySet();
        StringBuilder query = new StringBuilder();
        for (Entry<String, String> entry : set) {
            if (query.length() > 0)
                query.append("&");
            else
                query.append("?");
            query.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return query.toString();
    }

    private String getRoute(JsonObject endpoint, Map<String, String> params) {
        Pattern pattern = Pattern.compile("/:(\\w+)/");
        String route = endpoint.get("route").getAsString();
        route += "/";
        Matcher matcher = pattern.matcher(route);
        while (matcher.find()) {
            String value = route.substring(matcher.start() + 2, matcher.end() - 1);
            if (params.containsKey(value)) {
                route = route.replace(":" + value, Objects.requireNonNull(params.get(value)));
                params.remove(value);
                matcher = pattern.matcher(route);
            }
        }
        route = route.substring(0, route.length() - 1);
        return route;
    }
}
