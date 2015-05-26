package br.com.gerencianet.gnsdk.lib.listeners;

import junit.framework.Assert;

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
import br.com.gerencianet.gnsdk.interfaces.IGnListener;
import br.com.gerencianet.gnsdk.interfaces.IRequester;
import br.com.gerencianet.gnsdk.lib.Authenticator;
import br.com.gerencianet.gnsdk.lib.Endpoints;
import br.com.gerencianet.gnsdk.lib.requesters.PaymentTokenRequester;
import br.com.gerencianet.gnsdk.models.*;

import static org.mockito.Mockito.verify;

/**
 * Created by francisco on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
public class AuthListenerTest {
    private AuthListener authListener;

    @Mock
    private Token accessToken;

    @Mock
    private IGnListener listener;

    @Mock
    private IRequester requester;

    @Mock
    private br.com.gerencianet.gnsdk.models.Error error;

    public AuthListenerTest() {}

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        authListener = new AuthListener(listener);
        authListener.setRequester(requester);
        authListener.setAccessToken(null);
    }

    @Test
    public void shouldSetAccessTokenAndDoRequest() {
        authListener.onAccessGranted(accessToken);
        verify(requester, Mockito.only()).doPost(accessToken);
        Assert.assertNotNull(authListener.getAccessToken());
    }

    @Test
    public void shouldCallOnErrorFromListener() {
        authListener.onError(error);
        verify(listener, Mockito.only()).onError(error);
    }
}
