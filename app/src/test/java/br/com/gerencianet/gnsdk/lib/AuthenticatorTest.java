package br.com.gerencianet.gnsdk.lib;

import com.loopj.android.http.RequestParams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import br.com.gerencianet.gnsdk.config.Config;
import br.com.gerencianet.gnsdk.interfaces.IAuthListener;
import br.com.gerencianet.gnsdk.lib.handlers.AuthResponseHandler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by francisco on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
public class AuthenticatorTest {
    private Authenticator authenticator;

    @Mock
    private IAuthListener listener;

    @Mock
    private RestClient restClient;

    @Mock
    private Config config;

    public AuthenticatorTest(){}

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        authenticator = new Authenticator(config);
        authenticator.setClient(restClient);
        authenticator.setAuthListener(listener);
    }

    @Test
    public void shouldCallPost() {
        authenticator.getAccessToken();
        verify(restClient, Mockito.only()).post(
            any(String.class),
            any(RequestParams.class),
            any(AuthResponseHandler.class));
    }
}
