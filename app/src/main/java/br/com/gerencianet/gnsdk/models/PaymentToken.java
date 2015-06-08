package br.com.gerencianet.gnsdk.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by francisco on 20/03/15.
 */
public class PaymentToken extends GnModel {
    private String hash;

    public PaymentToken(JSONObject jsonObject) {
        fromJson(jsonObject);
    }

    public String getHash() {
        return hash;
    }

    @Override
    public void fromJson(JSONObject jsonObject) {
        try {
            if(!jsonObject.has("data"))
                return;

            jsonObject = jsonObject.getJSONObject("data");

            if(!jsonObject.has("card"))
                return;

            jsonObject = jsonObject.getJSONObject("card");
            hash = jsonObject.getString("payment_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
