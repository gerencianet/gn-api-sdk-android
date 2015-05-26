package br.com.gerencianet.gnsdk.models;

import org.json.JSONObject;

/**
 * Created by francisco on 25/05/15.
 */
public abstract class GnModel {
    public abstract void fromJson(JSONObject jsonObject);
    public abstract JSONObject toJson();
}
