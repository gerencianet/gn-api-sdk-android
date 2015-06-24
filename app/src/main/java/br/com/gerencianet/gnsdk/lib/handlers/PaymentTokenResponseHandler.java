package br.com.gerencianet.gnsdk.lib.handlers;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.gerencianet.gnsdk.interfaces.IGnListener;
import br.com.gerencianet.gnsdk.models.Error;
import br.com.gerencianet.gnsdk.models.PaymentToken;

/**
 * Created by francisco on 22/05/15.
 */
public class PaymentTokenResponseHandler extends JsonHttpResponseHandler {
    private IGnListener gnListener;

    public PaymentTokenResponseHandler(IGnListener gnListener) {
        this.gnListener = gnListener;
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
        gnListener.onError(new Error(errorResponse));
    }
}
