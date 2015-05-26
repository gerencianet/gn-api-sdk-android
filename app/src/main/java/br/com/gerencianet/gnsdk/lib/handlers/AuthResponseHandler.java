package br.com.gerencianet.gnsdk.lib.handlers;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.gerencianet.gnsdk.config.Constants;
import br.com.gerencianet.gnsdk.interfaces.IAuthListener;
import br.com.gerencianet.gnsdk.models.Error;
import br.com.gerencianet.gnsdk.models.Token;

/**
 * Created by francisco on 25/05/15.
 */
public class AuthResponseHandler extends JsonHttpResponseHandler {
    private IAuthListener authListener;

    public AuthResponseHandler(IAuthListener authListener) {
        this.authListener = authListener;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        if(statusCode == 200) {
            Token token = new Token(response);
            authListener.onAccessGranted(token);
            Log.d(Constants.TAG, "Granted: " + token.getHash());
        } else {
            authListener.onError(new Error(response));
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        authListener.onError(new Error(errorResponse));
    }
}
