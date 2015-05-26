package br.com.gerencianet.gnsdk.lib.handlers;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import br.com.gerencianet.gnsdk.interfaces.IEndpoints;
import br.com.gerencianet.gnsdk.interfaces.IGnListener;
import br.com.gerencianet.gnsdk.models.CreditCard;
import br.com.gerencianet.gnsdk.models.Error;
import br.com.gerencianet.gnsdk.models.PaymentData;
import br.com.gerencianet.gnsdk.models.PaymentToken;
import br.com.gerencianet.gnsdk.models.PaymentType;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by francisco on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
public class PaymentDataResponseHandlerTest {

    private PaymentDataResponseHandler paymentDataResponseHandler;
    private JSONObject successResponse;
    private JSONObject errorResponse;
    private Header[] headers = new Header[1];

    @Mock
    private IGnListener gnListener;

    @Mock
    private PaymentType paymentType;

    @Mock
    IEndpoints endpoints;

    public PaymentDataResponseHandlerTest() throws JSONException {
        successResponse = new JSONObject();
        errorResponse = new JSONObject();
        successResponse = new JSONObject();
        errorResponse = new JSONObject();
        successResponse.put("code", 200);
        errorResponse.put("code", 500);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCallOnPaymentDataFetched() {
        paymentDataResponseHandler =
            new PaymentDataResponseHandler(
                gnListener, paymentType, false, endpoints);
        paymentDataResponseHandler
            .onSuccess(200, headers, successResponse);

        verify(gnListener, Mockito.only())
            .onPaymentDataFetched(any(PaymentData.class));
    }

    @Test
    public void shouldCallTryToGetPaymentDataAgain() {
        paymentDataResponseHandler =
                new PaymentDataResponseHandler(
                        gnListener, paymentType, true, endpoints);
        paymentDataResponseHandler
                .onFailure(401, headers, new Throwable(), successResponse);

        verify(gnListener, Mockito.never())
                .onPaymentDataFetched(any(PaymentData.class));
        verify(gnListener, Mockito.never())
                .onError(any(Error.class));
        verify(endpoints, Mockito.times(1))
                .clearAccessToken();
        verify(endpoints, Mockito.times(1))
                .getPaymentData(any(PaymentType.class));
    }

    @Test
    public void shouldCallOnErrorIfCodeIsNot200() {
        paymentDataResponseHandler =
                new PaymentDataResponseHandler(
                        gnListener, paymentType, false, endpoints);
        paymentDataResponseHandler
                .onSuccess(200, headers, errorResponse);

        verify(gnListener, Mockito.never())
                .onPaymentDataFetched(any(PaymentData.class));
        verify(gnListener, Mockito.only())
                .onError(any(Error.class));
    }

    @Test
    public void shouldCallOnErrorFromOnFailure() {
        paymentDataResponseHandler =
                new PaymentDataResponseHandler(
                        gnListener, paymentType, false, endpoints);
        paymentDataResponseHandler
                .onFailure(401, headers, new Throwable(), successResponse);

        verify(gnListener, Mockito.never())
                .onPaymentDataFetched(any(PaymentData.class));
        verify(gnListener, Mockito.only())
                .onError(any(Error.class));
        verify(endpoints, Mockito.never())
                .clearAccessToken();
        verify(endpoints, Mockito.never())
                .getPaymentData(any(PaymentType.class));
    }

    @Test
    public void shouldCatchJsonException() {
        paymentDataResponseHandler =
                new PaymentDataResponseHandler(
                        gnListener, paymentType, false, endpoints);
        paymentDataResponseHandler
                .onSuccess(200, headers, new JSONObject());

        verify(gnListener, Mockito.never())
                .onPaymentDataFetched(any(PaymentData.class));
        verify(gnListener, Mockito.only())
                .onError(any(Error.class));
    }
}
