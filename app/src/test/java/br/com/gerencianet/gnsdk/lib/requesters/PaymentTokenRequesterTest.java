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

import br.com.gerencianet.gnsdk.config.Config;
import br.com.gerencianet.gnsdk.lib.RestClient;
import br.com.gerencianet.gnsdk.models.CreditCard;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by francisco on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
public class PaymentTokenRequesterTest {
    private PaymentTokenRequester requester;
    private Config config;

    @Mock
    private RestClient client;

    @Mock
    private CreditCard creditCard;

    @Mock
    private JsonHttpResponseHandler responseHandler;

    public PaymentTokenRequesterTest() {}

    @Before
    public void setup() throws JSONException {
        MockitoAnnotations.initMocks(this);

        config = new Config();
        config.setAccountCode("123");
        config.setSandbox(true);

        requester = new PaymentTokenRequester(config);
        requester.setCreditCard(creditCard);
        requester.setClient(client);
        requester.setResponseHandler(responseHandler);
        when(creditCard.toJson())
            .thenReturn(new JSONObject());
    }

    @Test
    public void shouldDoPostWithAccountCodeAndPayloadData() {
        requester.doPost();

        RequestParams params = requester.getParams();
        Assert.assertEquals(params.has("account_code"), true);
        Assert.assertEquals(params.has("data"), true);

        verify(client, Mockito.only()).post(
                anyString(),
                any(RequestParams.class),
                any(JsonHttpResponseHandler.class));

        verify(creditCard, Mockito.only()).toJson();
    }
}
