package br.com.gerencianet.gnsdk.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by francisco on 20/03/15.
 */
public class PaymentData extends GnModel {
    private String name;
    private String brand;
    private String currency;
    private Integer total;
    private Integer rate;
    private Integer interestPercentage;
    private List<Installment> installments;

    public PaymentData(JSONObject jsonObject) {
        this.installments = new ArrayList<Installment>();

        if(jsonObject != null)
            this.fromJson(jsonObject);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public List<Installment> getInstallments() {
        return installments;
    }

    public void setInstallments(List<Installment> installments) {
        this.installments = installments;
    }

    public Integer getInterestPercentage() {
        return interestPercentage;
    }

    public void setInterestPercentage(Integer interestPercentage) {
        this.interestPercentage = interestPercentage;
    }

    @Override
    public void fromJson(JSONObject jsonObject) {
        try {
            if(!jsonObject.has("data"))
                return;

            jsonObject = jsonObject.getJSONObject("data");

            if(jsonObject.has("name")) {
                name = jsonObject.getString("name");
            }

            if(jsonObject.has("total")) {
                total = jsonObject.getInt("total");
            }

            if(jsonObject.has("rate")) {
                rate = jsonObject.getInt("rate");
            }

            if(jsonObject.has("currency")) {
                currency = jsonObject.getString("currency");
            }

            if(jsonObject.has("interest_percentage")) {
                interestPercentage = jsonObject.getInt("interest_percentage");
            }

            if(jsonObject.has("installments")) {
                JSONArray arr = jsonObject.getJSONArray("installments");
                for (int i =0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    Installment installment = new Installment();
                    installment.setCurrency(obj.getString("currency"));
                    installment.setHasInterest(obj.getBoolean("has_interest"));
                    installment.setParcels(obj.getInt("installment"));
                    installment.setValue(obj.getInt("value"));
                    installments.add(installment);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
