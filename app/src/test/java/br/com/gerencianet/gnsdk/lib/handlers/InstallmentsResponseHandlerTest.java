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
import br.com.gerencianet.gnsdk.models.Error;
import br.com.gerencianet.gnsdk.models.PaymentData;
import br.com.gerencianet.gnsdk.models.PaymentType;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by francisco on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
public class InstallmentsResponseHandlerTest {

    private InstallmentsResponseHandler installmentsResponseHandler;
    private JSONObject successResponse;
    private JSONObject errorResponse;
    private Header[] headers = new Header[1];

    @Mock
    private IGnListener gnListener;

    @Mock
    private PaymentType paymentType;

    @Mock
    IEndpoints endpoints;

    public InstallmentsResponseHandlerTest() throws JSONException {
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
        installmentsResponseHandler =
            new InstallmentsResponseHandler(gnListener);
        installmentsResponseHandler
            .onSuccess(200, headers, successResponse);

        verify(gnListener, Mockito.only())
            .onInstallmentsFetched(any(PaymentData.class));
    }

    @Test
    public void shouldCallOnErrorIfCodeIsNot200() {
        installmentsResponseHandler =
                new InstallmentsResponseHandler(gnListener);
        installmentsResponseHandler
                .onSuccess(200, headers, errorResponse);

        verify(gnListener, Mockito.never())
                .onInstallmentsFetched(any(PaymentData.class));
        verify(gnListener, Mockito.only())
                .onError(any(Error.class));
    }

    @Test
    public void shouldCallOnErrorFromOnFailure() {
        installmentsResponseHandler =
                new InstallmentsResponseHandler(gnListener);
        installmentsResponseHandler
                .onFailure(401, headers, new Throwable(), successResponse);

        verify(gnListener, Mockito.never())
                .onInstallmentsFetched(any(PaymentData.class));
        verify(gnListener, Mockito.only())
                .onError(any(Error.class));
        verify(endpoints, Mockito.never())
                .getInstallments(any(PaymentType.class));
    }

    @Test
    public void shouldCatchJsonException() {
        installmentsResponseHandler =
                new InstallmentsResponseHandler(gnListener);
        installmentsResponseHandler
                .onSuccess(200, headers, new JSONObject());

        verify(gnListener, Mockito.never())
                .onInstallmentsFetched(any(PaymentData.class));
        verify(gnListener, Mockito.only())
                .onError(any(Error.class));
    }
}
