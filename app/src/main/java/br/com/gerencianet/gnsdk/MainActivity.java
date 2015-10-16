package br.com.gerencianet.gnsdk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import br.com.gerencianet.gnsdk.config.Config;
import br.com.gerencianet.gnsdk.config.Constants;
import br.com.gerencianet.gnsdk.interfaces.IGnListener;
import br.com.gerencianet.gnsdk.lib.Endpoints;
import br.com.gerencianet.gnsdk.models.CreditCard;
import br.com.gerencianet.gnsdk.models.Error;
import br.com.gerencianet.gnsdk.models.PaymentData;
import br.com.gerencianet.gnsdk.models.PaymentToken;
import br.com.gerencianet.gnsdk.models.PaymentType;


public class MainActivity extends Activity implements IGnListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Config config = new Config();
        config.setAccountCode("YOUR_ACCOUNT_CODE");
        config.setSandbox(true);

        CreditCard creditCard = new CreditCard();
        creditCard.setBrand("visa");
        creditCard.setCvv("123");
        creditCard.setNumber("4012001038443335");
        creditCard.setExpirationMonth("05");
        creditCard.setExpirationYear("2018");

        PaymentType paymentType = new PaymentType();
        paymentType.setName("visa");
        paymentType.setTotal(10000);

        Endpoints gnClient = new Endpoints(config, this);

        gnClient.getPaymentToken(creditCard);
        gnClient.getInstallments(paymentType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInstallmentsFetched(PaymentData paymentData) {
        Log.d(Constants.TAG, "Payment Data: " + new Gson().toJson(paymentData));
    }

    @Override
    public void onPaymentTokenFetched(PaymentToken paymentToken) {
        Log.d(Constants.TAG, "Token: " + paymentToken.getHash());
    }

    @Override
    public void onError(Error gnError) {
        Log.d(Constants.TAG, "Error: " + new Gson().toJson(gnError));
    }
}
