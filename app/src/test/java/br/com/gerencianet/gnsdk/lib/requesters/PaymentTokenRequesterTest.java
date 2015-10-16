package br.com.gerencianet.gnsdk.lib.requesters;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import junit.framework.Assert;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Created by francisco on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
public class PaymentTokenRequesterTest {
    private PaymentTokenRequester requester;
    private Config config;
    private CreditCard creditCard;

    private String pubKey;
    private Header[] headers;

    @Mock
    private RestClient client;

    @Mock
    private JsonHttpResponseHandler responseHandler;

    public PaymentTokenRequesterTest() {}

    @Before
    public void setup() throws JSONException {
        MockitoAnnotations.initMocks(this);

        config = new Config();
        config.setAccountCode("123");
        config.setSandbox(true);

        creditCard = new CreditCard();

        requester = new PaymentTokenRequester();
        requester.setCreditCard(creditCard);
        requester.setClient(client);
        requester.setResponseHandler(responseHandler);

        pubKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyvgUOi8YL/pjv9jHXYzUSFS9f\n" +
                "uX7FfgBAqRoR7H19SOkh0enmMWVQuZGfArdZ3LAdiJ4A290fc9nXYvzBIvIsSCsH\n" +
                "wJV1MlUr910Po0M+2NmMvpCS/VmRWB4zATAzR/4CAjq7JUgHcWe6VUiPCViOWifF\n" +
                "3XnS4BBpB4wX7+54KQIDAQAB\n" +
                "-----END PUBLIC KEY-----";
        headers = new Header[]{
                new BasicHeader("Content-Type", "application/json")
        };
    }

    @Test
    public void shouldRequestPublicKey() {
        requester.doPost();
        verify(client, Mockito.only()).get(
                anyString(),
                any(RequestParams.class),
                any(JsonHttpResponseHandler.class));
    }

    @Test
    public void shouldPostEncryptedCard() {
        try {
            JSONObject pubKeyResponse = new JSONObject("{\"code\": 200, \"data\": \"" + pubKey + "\"}");
            requester.onSuccess(200, headers, pubKeyResponse);
        } catch(JSONException e) {
            Assert.fail();
        }

        Assert.assertTrue(requester.getParams().has("data"));
        verify(client, Mockito.only()).post(
                anyString(),
                any(RequestParams.class),
                any(JsonHttpResponseHandler.class));
    }

    @Test
    public void shouldCallFailureResponse() {
        try {
            JSONObject pubKeyResponse = new JSONObject("{\"code\": 500}");
            requester.onSuccess(200, headers, pubKeyResponse);
        } catch(JSONException e) {
            Assert.fail();
        }

        verify(responseHandler, Mockito.only()).onFailure(
                anyInt(),
                any(Header[].class),
                any(Throwable.class),
                any(JSONObject.class));
    }

}
