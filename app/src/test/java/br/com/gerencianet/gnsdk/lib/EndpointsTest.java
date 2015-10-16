package br.com.gerencianet.gnsdk.lib;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import br.com.gerencianet.gnsdk.config.Config;
import br.com.gerencianet.gnsdk.interfaces.IGnListener;
import br.com.gerencianet.gnsdk.lib.requesters.InstallmentsRequester;
import br.com.gerencianet.gnsdk.lib.requesters.PaymentTokenRequester;
import br.com.gerencianet.gnsdk.models.CreditCard;
import br.com.gerencianet.gnsdk.models.PaymentType;

import static org.mockito.Mockito.verify;

/**
 * Created by francisco on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
public class EndpointsTest {
    private Endpoints endpoints;

    @Mock
    private Config config;

    @Mock
    private CreditCard creditCard;

    @Mock
    private PaymentType paymentType;

    @Mock
    private IGnListener mockListener;

    @Mock
    private PaymentTokenRequester paymentTokenRequester;

    @Mock
    private InstallmentsRequester installmentsRequester;

    public EndpointsTest() {}

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        endpoints = new Endpoints(config, mockListener);
        endpoints.setPaymentTokenRequester(paymentTokenRequester);
        endpoints.setInstallmentsRequester(installmentsRequester);
    }

    @Test
    public void shouldGetPaymentToken() {
        endpoints.getPaymentToken(creditCard);
        verify(paymentTokenRequester, Mockito.times(1)).doPost();
    }

    @Test
    public void shouldGetInstallments() {
        endpoints.getInstallments(paymentType);
        verify(installmentsRequester, Mockito.times(1)).doPost();
    }
}
