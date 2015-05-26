package br.com.gerencianet.gnsdk.lib;

import com.loopj.android.http.JsonHttpResponseHandler;

import br.com.gerencianet.gnsdk.config.Config;
import br.com.gerencianet.gnsdk.interfaces.IAuth;
import br.com.gerencianet.gnsdk.interfaces.IAuthListener;
import br.com.gerencianet.gnsdk.interfaces.IEndpoints;
import br.com.gerencianet.gnsdk.interfaces.IGnListener;
import br.com.gerencianet.gnsdk.interfaces.IRequester;
import br.com.gerencianet.gnsdk.lib.handlers.PaymentDataResponseHandler;
import br.com.gerencianet.gnsdk.lib.listeners.AuthListener;
import br.com.gerencianet.gnsdk.lib.requesters.PaymentDataRequester;
import br.com.gerencianet.gnsdk.lib.requesters.PaymentTokenRequester;
import br.com.gerencianet.gnsdk.lib.handlers.PaymentTokenResponseHandler;
import br.com.gerencianet.gnsdk.models.CreditCard;
import br.com.gerencianet.gnsdk.models.PaymentType;
import br.com.gerencianet.gnsdk.models.Token;

/**
 * Created by francisco on 20/03/15.
 */
public class Endpoints implements IEndpoints {

    private IAuth authenticator;
    private IAuthListener authListener;
    private RestClient client;
    private IGnListener listener;
    private Token accessToken;

    private PaymentTokenRequester paymentTokenRequester;
    private PaymentTokenResponseHandler paymentTokenResponseHandler;

    private PaymentDataRequester paymentDataRequester;
    private PaymentDataResponseHandler paymentDataResponseHandler;

    public Endpoints(Config config, IGnListener listener) {
        this.listener = listener;
        authenticator = new Authenticator(config);
        client = new RestClient(config);
        authListener = new AuthListener(listener);
        paymentTokenRequester = new PaymentTokenRequester();
        paymentDataRequester = new PaymentDataRequester();
    }

    @Override
    public void getPaymentToken(CreditCard creditCard) {
        paymentTokenResponseHandler =
            new PaymentTokenResponseHandler(listener, creditCard, true, this);

        paymentTokenRequester.setCreditCard(creditCard);
        paymentTokenRequester.setClient(client);
        paymentTokenRequester.setResponseHandler(paymentTokenResponseHandler);
        authorize(paymentTokenRequester);
    }

    @Override
    public void getPaymentData(PaymentType paymentType) {
        paymentDataResponseHandler =
            new PaymentDataResponseHandler(listener, paymentType, true, this);

        paymentDataRequester.setPaymentType(paymentType);
        paymentDataRequester.setClient(client);
        paymentDataRequester.setResponseHandler(paymentDataResponseHandler);
        authorize(paymentDataRequester);
    }

    @Override
    public void clearAccessToken() {
        authListener.setAccessToken(null);
    }

    private void authorize(IRequester requester) {
        accessToken = authListener.getAccessToken();

        if(accessToken == null || accessToken.hasExpired()) {
            authListener.setRequester(requester);
            authenticator.setAuthListener(authListener);
            authenticator.getAccessToken();
        } else {
            requester.doPost(accessToken);
        }
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    public void setAuthListener(IAuthListener authListener) {
        this.authListener = authListener;
    }

    public void setPaymentTokenRequester(PaymentTokenRequester paymentTokenRequester) {
        this.paymentTokenRequester = paymentTokenRequester;
    }

    public void setPaymentDataRequester(PaymentDataRequester paymentDataRequester) {
        this.paymentDataRequester = paymentDataRequester;
    }
}
