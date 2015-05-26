package br.com.gerencianet.gnsdk.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.gerencianet.gnsdk.config.Constants;

/**
 * Created by francisco on 20/03/15.
 */
public class Token extends GnModel {

    private String accessToken;
    private long expiresAt;

    public Token(){}

    public Token(JSONObject jsonObject) {
        if(jsonObject != null)
            fromJson(jsonObject);
    }

    public String getHash() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean hasExpired() {
        Log.d(Constants.TAG, "Time now: " + System.currentTimeMillis());
        Log.d(Constants.TAG, "Expire at: " + expiresAt);
        return System.currentTimeMillis() >= expiresAt;
    }

    @Override
    public void fromJson(JSONObject jsonObject) {
        try {
            accessToken = jsonObject.getString("access_token");
            expiresAt = System.currentTimeMillis() + 3600000;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
