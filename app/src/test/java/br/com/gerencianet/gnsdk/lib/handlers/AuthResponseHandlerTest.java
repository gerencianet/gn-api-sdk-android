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

import br.com.gerencianet.gnsdk.lib.listeners.AuthListener;
import br.com.gerencianet.gnsdk.models.*;
import br.com.gerencianet.gnsdk.models.Error;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by francisco on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
public class AuthResponseHandlerTest {
    private AuthResponseHandler authResponseHandler;
    private JSONObject successResponse;
    private JSONObject errorResponse;
    private Header[] headers = new Header[1];

    @Mock
    private AuthListener authListener;

    @Mock
    private Token accessToken;

    public AuthResponseHandlerTest() throws JSONException {
        successResponse = new JSONObject();
        errorResponse = new JSONObject();
        successResponse.put("code", 200);
        errorResponse.put("code", 500);
        successResponse.put("access_token", "123");
        errorResponse.put("access_token", "123");
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCallOnAccessGranted() {
        authResponseHandler = new AuthResponseHandler(authListener);
        authResponseHandler.onSuccess(200, headers, successResponse);

        verify(authListener, Mockito.only())
            .onAccessGranted(any(Token.class));

        verify(authListener, Mockito.never())
            .onError(any(Error.class));
    }

    @Test
    public void shouldCallOnErrorWhenStatusCodeIsNot200() {
        authResponseHandler = new AuthResponseHandler(authListener);
        authResponseHandler.onSuccess(500, headers, errorResponse);

        verify(authListener, Mockito.never())
            .onAccessGranted(any(Token.class));

        verify(authListener, Mockito.only())
            .onError(any(Error.class));

    }

    @Test
    public void shouldCallOnErrorIfOnFailureWasTriggered() {
        authResponseHandler = new AuthResponseHandler(authListener);
        authResponseHandler.onFailure(500, headers, new Throwable(), successResponse);

        verify(authListener, Mockito.never())
                .onAccessGranted(any(Token.class));

        verify(authListener, Mockito.only())
                .onError(any(Error.class));
    }
}
