package br.com.gerencianet.gnsdk.lib.requesters;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.gerencianet.gnsdk.config.Config;
import br.com.gerencianet.gnsdk.config.Constants;
import br.com.gerencianet.gnsdk.interfaces.IRequester;
import br.com.gerencianet.gnsdk.lib.RSA;
import br.com.gerencianet.gnsdk.lib.RestClient;
import br.com.gerencianet.gnsdk.models.*;
import br.com.gerencianet.gnsdk.models.Error;

/**
 * Created by francisco on 22/05/15.
 */
public class PaymentTokenRequester extends JsonHttpResponseHandler implements IRequester {
    private JsonHttpResponseHandler responseHandler;
    private RequestParams params;
    private RestClient client;
    private CreditCard creditCard;

    public PaymentTokenRequester() {}

    @Override
    public void doPost() {
        PublicKeyRequester pubKeyRequester = new PublicKeyRequester();
        pubKeyRequester.setClient(client);
        pubKeyRequester.setResponseHandler(this);
        pubKeyRequester.doPost();
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

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        try {
            if(response.getString("code").equals("200")) {
                String pubKey = response.getString("data");
                String creditCardJson = creditCard.toJson().toString();
                String encryptedCard = RSA.encrypt(creditCardJson, pubKey);

                params = new RequestParams();
                params.add("data", encryptedCard);
                client.post(Constants.ROUTE_SAVE_CARD, params, responseHandler);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            onFailure(statusCode, headers, null, response);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        responseHandler.onFailure(statusCode, headers, throwable, errorResponse);
    }
}