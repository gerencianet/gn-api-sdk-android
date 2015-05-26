package br.com.gerencianet.gnsdk.lib.requesters;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import br.com.gerencianet.gnsdk.lib.RestClient;
import br.com.gerencianet.gnsdk.models.PaymentType;
import br.com.gerencianet.gnsdk.models.Token;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by francisco on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
public class PaymentDataRequesterTest {
    private Token accessToken;
    private PaymentDataRequester requester;

    @Mock
    private RestClient client;

    @Mock
    private PaymentType paymentType;

    @Mock
    private JsonHttpResponseHandler responseHandler;

    public PaymentDataRequesterTest() {}

    @Before
    public void setup() throws JSONException {
        MockitoAnnotations.initMocks(this);
        requester = new PaymentDataRequester();
        accessToken = new Token();
        accessToken.setAccessToken("123");
        accessToken.setExpiresAt(360);
        requester.setPaymentType(paymentType);
        requester.setClient(client);
        requester.setResponseHandler(responseHandler);
        when(paymentType.toJson())
            .thenReturn(new JSONObject());
    }

    @Test
    public void shouldDoPostWithAccessTokenAndPayloadData() {
        requester.doPost(accessToken);

        RequestParams params = requester.getParams();
        Assert.assertEquals(params.has("access_token"), true);
        Assert.assertEquals(params.has("data"), true);

        verify(client, Mockito.only()).post(
                anyString(),
                any(RequestParams.class),
                any(JsonHttpResponseHandler.class));

        verify(paymentType, Mockito.only()).toJson();
    }
}
