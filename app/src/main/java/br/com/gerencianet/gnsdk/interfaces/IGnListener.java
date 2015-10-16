package br.com.gerencianet.gnsdk.interfaces;

import br.com.gerencianet.gnsdk.models.Error;
import br.com.gerencianet.gnsdk.models.PaymentData;
import br.com.gerencianet.gnsdk.models.PaymentToken;

/**
 * Created by francisco on 19/03/15.
 */
public interface IGnListener {
    public void onInstallmentsFetched(PaymentData paymentData);
    public void onPaymentTokenFetched(PaymentToken paymentToken);
    public void onError(Error error);
}
