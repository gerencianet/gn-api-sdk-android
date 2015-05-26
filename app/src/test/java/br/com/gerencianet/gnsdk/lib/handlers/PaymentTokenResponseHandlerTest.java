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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;

import br.com.gerencianet.gnsdk.config.Config;
import br.com.gerencianet.gnsdk.interfaces.IEndpoints;
import br.com.gerencianet.gnsdk.interfaces.IGnListener;
import br.com.gerencianet.gnsdk.lib.Authenticator;
import br.com.gerencianet.gnsdk.lib.Endpoints;
import br.com.gerencianet.gnsdk.lib.listeners.AuthListener;
import br.com.gerencianet.gnsdk.lib.requesters.PaymentTokenRequester;
import br.com.gerencianet.gnsdk.models.*;
import br.com.gerencianet.gnsdk.models.Error;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by francisco on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
public class PaymentTokenResponseHandlerTest {

    private PaymentTokenResponseHandler paymentTokenResponseHandler;
    private JSONObject successResponse;
    private JSONObject errorResponse;
    private Header[] headers = new Header[1];

    @Mock
    private IGnListener gnListener;

    @Mock
    private CreditCard creditCard;

    @Mock
    IEndpoints endpoints;

    public PaymentTokenResponseHandlerTest() throws JSONException {
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
    public void shouldCallOnPaymentTokenFetched() {
        paymentTokenResponseHandler =
            new PaymentTokenResponseHandler(
                gnListener, creditCard, false, endpoints);
        paymentTokenResponseHandler
            .onSuccess(200, headers, successResponse);

        verify(gnListener, Mockito.only())
            .onPaymentTokenFetched(any(PaymentToken.class));
    }

    @Test
    public void shouldCallTryToGetPaymentTokenAgain() {
        paymentTokenResponseHandler =
                new PaymentTokenResponseHandler(
                        gnListener, creditCard, true, endpoints);
        paymentTokenResponseHandler
                .onFailure(401, headers, new Throwable(), successResponse);

        verify(gnListener, Mockito.never())
                .onPaymentTokenFetched(any(PaymentToken.class));
        verify(gnListener, Mockito.never())
                .onError(any(Error.class));
        verify(endpoints, Mockito.times(1))
                .clearAccessToken();
        verify(endpoints, Mockito.times(1))
                .getPaymentToken(any(CreditCard.class));
    }

    @Test
    public void shouldCallOnErrorIfCodeIsNot200() {
        paymentTokenResponseHandler =
                new PaymentTokenResponseHandler(
                        gnListener, creditCard, false, endpoints);
        paymentTokenResponseHandler
                .onSuccess(200, headers, errorResponse);

        verify(gnListener, Mockito.never())
                .onPaymentTokenFetched(any(PaymentToken.class));
        verify(gnListener, Mockito.only())
                .onError(any(Error.class));
    }

    @Test
    public void shouldCallOnErrorFromOnFailure() {
        paymentTokenResponseHandler =
                new PaymentTokenResponseHandler(
                        gnListener, creditCard, false, endpoints);
        paymentTokenResponseHandler
                .onFailure(401, headers, new Throwable(), successResponse);

        verify(gnListener, Mockito.never())
                .onPaymentTokenFetched(any(PaymentToken.class));
        verify(gnListener, Mockito.only())
                .onError(any(Error.class));
        verify(endpoints, Mockito.never())
                .clearAccessToken();
        verify(endpoints, Mockito.never())
                .getPaymentToken(any(CreditCard.class));
    }

    @Test
    public void shouldCatchJsonException() {
        paymentTokenResponseHandler =
                new PaymentTokenResponseHandler(
                        gnListener, creditCard, false, endpoints);
        paymentTokenResponseHandler
                .onSuccess(200, headers, new JSONObject());

        verify(gnListener, Mockito.never())
                .onPaymentTokenFetched(any(PaymentToken.class));
        verify(gnListener, Mockito.only())
                .onError(any(Error.class));
    }
}
