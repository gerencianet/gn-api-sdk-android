package br.com.gerencianet.gnsdk.lib;

import com.loopj.android.http.RequestParams;

import br.com.gerencianet.gnsdk.config.Config;
import br.com.gerencianet.gnsdk.config.Constants;
import br.com.gerencianet.gnsdk.interfaces.IAuth;
import br.com.gerencianet.gnsdk.interfaces.IAuthListener;
import br.com.gerencianet.gnsdk.lib.handlers.AuthResponseHandler;

/**
 * Created by francisco on 19/03/15.
 */
public class Authenticator implements IAuth {
    private RestClient client;
    private Config config;
    private IAuthListener authListener;

    public Authenticator(Config config) {
        this.config = config;
        client = new RestClient(config);
    }

    @Override
    public void getAccessToken() {
        client.post(Constants.ROUTE_AUTHORIZE, getParams(),
            new AuthResponseHandler(authListener));
    }

    private RequestParams getParams() {
        RequestParams params = new RequestParams();
        params.add("client_id", config.getClientId());
        params.add("client_secret", config.getClientSecret());
        params.add("grant_type", config.getGrantType());
        return params;
    }

    public void setAuthListener(IAuthListener authListener) {
        this.authListener = authListener;
    }

    public void setClient(RestClient client) {
        this.client = client;
    }
}
