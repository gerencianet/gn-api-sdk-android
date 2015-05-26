package br.com.gerencianet.gnsdk.models;

import org.json.JSONObject;

/**
 * Created by francisco on 20/03/15.
 */
public class Installment extends GnModel {
    private Integer parcels;
    private Integer value;
    private Boolean hasInterest;
    private String currency;

    public Integer getParcels() {
        return parcels;
    }

    public void setParcels(Integer parcels) {
        this.parcels = parcels;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Boolean getHasInterest() {
        return hasInterest;
    }

    public void setHasInterest(Boolean hasInterest) {
        this.hasInterest = hasInterest;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public void fromJson(JSONObject jsonObject) {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
