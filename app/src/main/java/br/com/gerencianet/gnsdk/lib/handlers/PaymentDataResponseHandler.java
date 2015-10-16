package br.com.gerencianet.gnsdk.lib.handlers;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.gerencianet.gnsdk.interfaces.IGnListener;
import br.com.gerencianet.gnsdk.models.*;
import br.com.gerencianet.gnsdk.models.Error;

/**
 * Created by francisco on 25/05/15.
 */
public class PaymentDataResponseHandler extends JsonHttpResponseHandler {

    private IGnListener gnListener;

    public PaymentDataResponseHandler(IGnListener gnListener) {
        this.gnListener = gnListener;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        try {
            if(response.getString("code").equals("200")) {
                PaymentData paymentData = new PaymentData(response);
                gnListener.onPaymentDataFetched(paymentData);
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
        JSONObject res = errorResponse;
        gnListener.onError(new Error(errorResponse));
    }
}
