package br.com.gerencianet.gnsdk.lib.requesters;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import br.com.gerencianet.gnsdk.config.Config;
import br.com.gerencianet.gnsdk.config.Constants;
import br.com.gerencianet.gnsdk.interfaces.IRequester;
import br.com.gerencianet.gnsdk.lib.RestClient;
import br.com.gerencianet.gnsdk.models.CreditCard;

/**
 * Created by francisco on 22/05/15.
 */
public class PaymentTokenRequester implements IRequester {
    private JsonHttpResponseHandler responseHandler;
    private RequestParams params;
    private RestClient client;
    private CreditCard creditCard;
    private Config config;

    public PaymentTokenRequester(Config config) {
        this.config = config;
    }

    @Override
    public void doPost() {
        JSONObject card = creditCard.toJson();
        params = new RequestParams();
        params.add("data", card.toString());
        params.add("account_code", config.getAccountCode());
        client.post(Constants.ROUTE_SAVE_CARD, params, responseHandler);
    }

    public void setResponseHandler(JsonHttpResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public RequestParams getParams() {
        return params;
    }

    public void setClient(RestClient client) {
        this.client = client;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
}