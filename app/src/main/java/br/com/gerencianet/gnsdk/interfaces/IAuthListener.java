package br.com.gerencianet.gnsdk.interfaces;

import br.com.gerencianet.gnsdk.models.Error;
import br.com.gerencianet.gnsdk.models.Token;

/**
 * Created by francisco on 20/03/15.
 */
public interface IAuthListener {
    public void onAccessGranted(Token token);
    public void onError(Error error);
    public void setRequester(IRequester requester);
    public Token getAccessToken();
    public void setAccessToken(Token accessToken);
}
