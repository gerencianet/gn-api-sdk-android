package br.com.gerencianet.gnsdk.lib.requesters;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.gerencianet.gnsdk.lib.RestClient;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Created by thomaz on 10/16/15.
 */
public class PublicKeyRequesterTest {

    private PublicKeyRequester requester;

    @Mock
    private RestClient client;

    @Mock
    private JsonHttpResponseHandler responseHandler;

    public PublicKeyRequesterTest() {}

    @Before
    public void setup() throws JSONException {
        MockitoAnnotations.initMocks(this);

        requester = new PublicKeyRequester();
        requester.setClient(client);
        requester.setResponseHandler(responseHandler);
    }

    @Test
    public void shouldDoGet() {
        requester.doPost();

        verify(client, Mockito.only()).get(
                anyString(),
                any(RequestParams.class),
                any(JsonHttpResponseHandler.class));
    }

}
