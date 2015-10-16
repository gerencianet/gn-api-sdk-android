package br.com.gerencianet.gnsdk.lib.requesters;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import br.com.gerencianet.gnsdk.config.Constants;
import br.com.gerencianet.gnsdk.interfaces.IRequester;
import br.com.gerencianet.gnsdk.lib.RestClient;
import br.com.gerencianet.gnsdk.models.PaymentType;

/**
 * Created by francisco on 22/05/15.
 */
public class InstallmentsRequester implements IRequester {
    private JsonHttpResponseHandler responseHandler;
    private RequestParams params;
    private RestClient client;
    private PaymentType paymentType;

    public InstallmentsRequester() {}

    @Override
    public void doPost() {
        params = new RequestParams();
        params.add("brand", paymentType.getName());
        params.add("total", paymentType.getTotal().toString());
        client.get(Constants.ROUTE_PAYMENT_DATA, params, responseHandler);
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