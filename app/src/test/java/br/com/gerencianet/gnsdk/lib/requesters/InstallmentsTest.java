package br.com.gerencianet.gnsdk.lib.requesters;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import br.com.gerencianet.gnsdk.config.Config;
import br.com.gerencianet.gnsdk.lib.RestClient;
import br.com.gerencianet.gnsdk.models.PaymentType;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Created by francisco on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
public class InstallmentsTest {
    private PaymentType paymentType;
    private InstallmentsRequester requester;

    @Mock
    private Config config;

    @Mock
    private RestClient client;

    @Mock
    private JsonHttpResponseHandler responseHandler;

    public InstallmentsTest() {}

    @Before
    public void setup() throws JSONException {
        MockitoAnnotations.initMocks(this);

        config = new Config();
        config.setAccountCode("123");
        config.setSandbox(true);

        paymentType = new PaymentType();

        requester = new InstallmentsRequester();
        requester.setPaymentType(paymentType);
        requester.setClient(client);
        requester.setResponseHandler(responseHandler);
    }

    @Test
    public void shouldDoGetWithPayloadData() {
        requester.doPost();

        RequestParams params = requester.getParams();
        Assert.assertEquals(params.has("brand"), true);
        Assert.assertEquals(params.has("total"), true);

        verify(client, Mockito.only()).get(
                anyString(),
                any(RequestParams.class),
                any(JsonHttpResponseHandler.class));
    }
}
