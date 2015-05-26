package br.com.gerencianet.gnsdk.lib;

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
import br.com.gerencianet.gnsdk.lib.listeners.AuthListener;
import br.com.gerencianet.gnsdk.lib.requesters.PaymentDataRequester;
import br.com.gerencianet.gnsdk.lib.requesters.PaymentTokenRequester;
import br.com.gerencianet.gnsdk.models.CreditCard;
import br.com.gerencianet.gnsdk.models.PaymentType;
import br.com.gerencianet.gnsdk.models.Token;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by francisco on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
public class EndpointsTest {
    private Endpoints endpoints;
    private AuthListener authListener;

    @Mock
    private Config config;

    @Mock
    private CreditCard creditCard;

    @Mock
    private PaymentType paymentType;

    @Mock
    private IGnListener mockListener;

    @Mock
    private Authenticator authenticator;

    @Mock
    private PaymentTokenRequester paymentTokenRequester;

    @Mock
    private PaymentDataRequester paymentDataRequester;

    @Mock
    private Token accessToken;

    public EndpointsTest() {}

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        endpoints = new Endpoints(config, mockListener);
        authListener = new AuthListener(mockListener);
        authListener.setAccessToken(accessToken);
        endpoints.setAuthenticator(authenticator);
        endpoints.setAuthListener(authListener);
        endpoints.setPaymentTokenRequester(paymentTokenRequester);
        endpoints.setPaymentDataRequester(paymentDataRequester);

        when(accessToken.getHash())
            .thenReturn("230948PWQOFJKEP439CFKMG0439ITD");
        when(accessToken.hasExpired())
            .thenReturn(false);

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                authListener.onAccessGranted(accessToken);
                return null;
            }
        }).when(authenticator).getAccessToken();
    }

    @Test
    public void shouldGetAccessTokenThenPaymentToken() {
        endpoints.clearAccessToken();
        endpoints.getPaymentToken(creditCard);
        verify(authenticator, Mockito.times(1)).getAccessToken();
        verify(paymentTokenRequester, Mockito.times(1)).doPost(accessToken);
    }

    @Test
    public void shouldDirectlyGetPaymentToken() {
        endpoints.getPaymentToken(creditCard);
        verify(authenticator, Mockito.times(0)).getAccessToken();
        verify(paymentTokenRequester, Mockito.times(1)).doPost(accessToken);
    }

    @Test
    public void shouldDirectlyGetPaymentData() {
        endpoints.getPaymentData(paymentType);
        verify(authenticator, Mockito.times(0)).getAccessToken();
        verify(paymentDataRequester, Mockito.times(1)).doPost(accessToken);
    }

    @Test
    public void shouldGetTwoPaymentTokensWithTheSameAccessToken() {
        endpoints.clearAccessToken();
        endpoints.getPaymentToken(creditCard);
        endpoints.getPaymentToken(creditCard);
        verify(paymentTokenRequester, Mockito.times(2)).doPost(accessToken);
        verify(authenticator, Mockito.times(1)).getAccessToken();
    }
}
