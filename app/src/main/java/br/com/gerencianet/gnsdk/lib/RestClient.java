package br.com.gerencianet.gnsdk.lib;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import br.com.gerencianet.gnsdk.config.Config;
import br.com.gerencianet.gnsdk.config.Constants;

/**
 * Created by francisco on 19/03/15.
 */
public class RestClient {

    private String baseUrl;
    private AsyncHttpClient client;

    public RestClient(Config config) {
        client = new AsyncHttpClient();

        if(config.isSandbox()) {
            baseUrl = Constants.BASE_URL_SANDBOX;
        } else {
            baseUrl = Constants.BASE_URL_PRODUCTION;
        }
    }

    public void get(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public String getAbsoluteUrl(String relativeUrl) {
        return baseUrl + relativeUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setClient(AsyncHttpClient client) {
        this.client = client;
    }
}
