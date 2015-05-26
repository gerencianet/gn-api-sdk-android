package br.com.gerencianet.gnsdk.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by francisco on 23/03/15.
 */
public class Error extends GnModel {
    private Integer code;
    private String message;

    public Error(){}

    public Error(JSONObject jsonObject) {
        if(jsonObject != null)
            fromJson(jsonObject);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void fromJson(JSONObject jsonObject) {
        try {
            if(jsonObject.has("code")) {
                this.code = jsonObject.getInt("code");
            }

            if(jsonObject.has("error_description")) {
                this.message = jsonObject.getString("error_description");
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
