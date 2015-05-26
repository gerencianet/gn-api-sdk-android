package br.com.gerencianet.gnsdk.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by francisco on 20/03/15.
 */
public class PaymentType extends GnModel {
    private String name;
    private Integer total;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public void fromJson(JSONObject jsonObject) {
    }

    @Override
    public JSONObject toJson() {
        JSONObject type = new JSONObject();

        try {
            type.put("type", name);
            type.put("total", total);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return type;
    }
}
