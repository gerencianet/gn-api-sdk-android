package br.com.gerencianet.gnsdk.lib.requesters;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import br.com.gerencianet.gnsdk.config.Constants;
import br.com.gerencianet.gnsdk.interfaces.IRequester;
import br.com.gerencianet.gnsdk.lib.RestClient;
import br.com.gerencianet.gnsdk.models.PaymentType;
import br.com.gerencianet.gnsdk.models.Token;

/**
 * Created by francisco on 22/05/15.
 */
public class PaymentDataRequester implements IRequester {
    private JsonHttpResponseHandler responseHandler;
    private RequestParams params;
    private RestClient client;
    private PaymentType paymentType;

    public PaymentDataRequester() {}

    @Override
    public void doPost(Token accessToken) {
        JSONObject type = paymentType.toJson();
        params = new RequestParams();
        params.add("data", type.toString());
        params.add("access_token", accessToken.getHash());
        client.post(Constants.ROUTE_PAYMENT_DATA, params, responseHandler);
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

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
}