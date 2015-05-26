package br.com.gerencianet.gnsdk.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by francisco on 20/03/15.
 */
public class CreditCard extends GnModel {

    private String number;
    private String brand;
    private String expirationMonth;
    private String expirationYear;
    private String cvv;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public void fromJson(JSONObject jsonObject) {

    }

    @Override
    public JSONObject toJson() {
        JSONObject card = new JSONObject();

        try {
            card.put("brand", getBrand());
            card.put("number", getNumber());
            card.put("cvv", getCvv());
            card.put("expiration_month", getExpirationMonth());
            card.put("expiration_year", getExpirationYear());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return card;
    }
}
