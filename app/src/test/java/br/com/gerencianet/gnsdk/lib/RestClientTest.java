package br.com.gerencianet.gnsdk.lib;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import br.com.gerencianet.gnsdk.config.Config;
import br.com.gerencianet.gnsdk.config.Constants;

import static org.mockito.Mockito.verify;

/**
 * Created by francisco on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
public class RestClientTest {
    private RestClient restClient;
    private Config config;

    @Mock
    private JsonHttpResponseHandler responseHandler;

    @Mock
    private RequestParams requestParams;

    @Mock
    private AsyncHttpClient client;

    public RestClientTest(){}

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        config = new Config();
        config.setSandbox(true);
        config.setAccountCode("");
    }

    @Test
    public void shouldUseSandboxUrl() {
        restClient = new RestClient(config);
        Assert.assertEquals(restClient.getBaseUrl(), Constants.BASE_URL_SANDBOX);
    }

    @Test
    public void shouldUseProductionUrl() {
        config.setSandbox(false);
        restClient = new RestClient(config);
        Assert.assertEquals(restClient.getBaseUrl(), Constants.BASE_URL_PRODUCTION);
    }

    @Test
    public void shouldCallPost() {
        restClient = new RestClient(config);
        restClient.setClient(client);
        restClient.post(Constants.ROUTE_PAYMENT_DATA, requestParams, responseHandler);
        String route = restClient.getAbsoluteUrl(Constants.ROUTE_PAYMENT_DATA);
        verify(client, Mockito.only()).post(route, requestParams, responseHandler);
    }

    @Test
    public void shouldCallGet() {
        restClient = new RestClient(config);
        restClient.setClient(client);
        restClient.get(Constants.ROUTE_PAYMENT_DATA, requestParams, responseHandler);
        String route = restClient.getAbsoluteUrl(Constants.ROUTE_PAYMENT_DATA);
        verify(client, Mockito.only()).get(route, requestParams, responseHandler);
    }
}
