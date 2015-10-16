package br.com.gerencianet.gnsdk.lib.requesters;

import com.loopj.android.http.JsonHttpResponseHandler;

import br.com.gerencianet.gnsdk.config.Constants;
import br.com.gerencianet.gnsdk.interfaces.IRequester;
import br.com.gerencianet.gnsdk.lib.RestClient;

/**
 * Created by thomaz on 10/16/15.
 */
public class PublicKeyRequester implements IRequester {

    private JsonHttpResponseHandler responseHandler;
    private RestClient client;

    public PublicKeyRequester() {}

    @Override
    public void doPost() {
        client.get(Constants.ROUTE_PUBLIC_KEY, null, responseHandler);
    }

    public void setResponseHandler(JsonHttpResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public void setClient(RestClient client) {
        this.client = client;
    }
}
