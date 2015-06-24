package br.com.gerencianet.gnsdk.lib;

import br.com.gerencianet.gnsdk.config.Config;
import br.com.gerencianet.gnsdk.interfaces.IEndpoints;
import br.com.gerencianet.gnsdk.interfaces.IGnListener;
import br.com.gerencianet.gnsdk.lib.handlers.PaymentDataResponseHandler;
import br.com.gerencianet.gnsdk.lib.requesters.PaymentDataRequester;
import br.com.gerencianet.gnsdk.lib.requesters.PaymentTokenRequester;
import br.com.gerencianet.gnsdk.lib.handlers.PaymentTokenResponseHandler;
import br.com.gerencianet.gnsdk.models.CreditCard;
import br.com.gerencianet.gnsdk.models.PaymentType;

/**
 * Created by francisco on 20/03/15.
 */
public class Endpoints implements IEndpoints {

    private RestClient client;
    private IGnListener listener;

    private PaymentTokenRequester paymentTokenRequester;
    private PaymentTokenResponseHandler paymentTokenResponseHandler;

    private PaymentDataRequester paymentDataRequester;
    private PaymentDataResponseHandler paymentDataResponseHandler;

    public Endpoints(Config config, IGnListener listener) {
        this.listener = listener;
        client = new RestClient(config);
        paymentTokenRequester = new PaymentTokenRequester(config);
        paymentDataRequester = new PaymentDataRequester(config);
    }

    @Override
    public void getPaymentToken(CreditCard creditCard) {
        paymentTokenResponseHandler =
            new PaymentTokenResponseHandler(listener);

        paymentTokenRequester.setCreditCard(creditCard);
        paymentTokenRequester.setClient(client);
        paymentTokenRequester.setResponseHandler(paymentTokenResponseHandler);
        paymentTokenRequester.doPost();
    }

    @Override
    public void getPaymentData(PaymentType paymentType) {
        paymentDataResponseHandler =
            new PaymentDataResponseHandler(listener);

        paymentDataRequester.setPaymentType(paymentType);
        paymentDataRequester.setClient(client);
        paymentDataRequester.setResponseHandler(paymentDataResponseHandler);
        paymentDataRequester.doPost();
    }

    public void setPaymentTokenRequester(PaymentTokenRequester paymentTokenRequester) {
        this.paymentTokenRequester = paymentTokenRequester;
    }

    public void setPaymentDataRequester(PaymentDataRequester paymentDataRequester) {
        this.paymentDataRequester = paymentDataRequester;
    }
}
