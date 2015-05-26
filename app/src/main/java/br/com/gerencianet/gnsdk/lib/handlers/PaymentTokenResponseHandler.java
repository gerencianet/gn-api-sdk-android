package br.com.gerencianet.gnsdk.lib.handlers;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.gerencianet.gnsdk.config.Constants;
import br.com.gerencianet.gnsdk.interfaces.IEndpoints;
import br.com.gerencianet.gnsdk.interfaces.IGnListener;
import br.com.gerencianet.gnsdk.models.CreditCard;
import br.com.gerencianet.gnsdk.models.Error;
import br.com.gerencianet.gnsdk.models.PaymentToken;

/**
 * Created by francisco on 22/05/15.
 */
public class PaymentTokenResponseHandler extends JsonHttpResponseHandler {
    private CreditCard creditCard;
    private Boolean handle401;
    private IGnListener gnListener;
    private IEndpoints endpoints;

    public PaymentTokenResponseHandler(IGnListener gnListener,
        CreditCard creditCard, Boolean handle401, IEndpoints endpoints) {
        this.handle401 = handle401;
        this.creditCard = creditCard;
        this.gnListener = gnListener;
        this.endpoints = endpoints;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        try {
            if(response.getString("code").equals("200")) {
                PaymentToken paymentToken = new PaymentToken(response);
                gnListener.onPaymentTokenFetched(paymentToken);
            } else {
                gnListener.onError(new Error(response));
            }
        } catch (JSONException e) {
            Error error = new Error();
            error.setCode(500);
            error.setMessage(e.getMessage());
            gnListener.onError(error);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        if(statusCode == 401 && handle401) {
            this.handle401 = false;
            Log.d(Constants.TAG, "Unauthorized request. Handling 401 error. Redoing post to get payment token");
            endpoints.clearAccessToken();
            endpoints.getPaymentToken(creditCard);
        } else {
            gnListener.onError(new Error(errorResponse));
        }
    }
}
