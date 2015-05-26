package br.com.gerencianet.gnsdk.lib.listeners;

import br.com.gerencianet.gnsdk.interfaces.IAuthListener;
import br.com.gerencianet.gnsdk.interfaces.IGnListener;
import br.com.gerencianet.gnsdk.interfaces.IRequester;
import br.com.gerencianet.gnsdk.models.Error;
import br.com.gerencianet.gnsdk.models.Token;

/**
 * Created by francisco on 22/05/15.
 */
public class AuthListener implements IAuthListener {
    private Token accessToken;
    private IGnListener listener;
    private IRequester requester;

    public AuthListener(IGnListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAccessGranted(Token newAccessToken) {
        accessToken = newAccessToken;
        requester.doPost(accessToken);
    }

    @Override
    public void onError(Error error) {
        listener.onError(error);
    }

    @Override
    public Token getAccessToken() {
        return accessToken;
    }

    @Override
    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }

    public void setRequester(IRequester requester) {
        this.requester = requester;
    }
}
