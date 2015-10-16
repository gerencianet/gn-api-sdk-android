package br.com.gerencianet.gnsdk.interfaces;

import br.com.gerencianet.gnsdk.models.CreditCard;
import br.com.gerencianet.gnsdk.models.PaymentType;

/**
 * Created by francisco on 19/03/15.
 */
public interface IEndpoints {
    public void getPaymentToken(CreditCard creditCard);
    public void getInstallments(PaymentType paymentType);
}
