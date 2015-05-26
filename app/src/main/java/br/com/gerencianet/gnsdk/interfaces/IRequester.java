package br.com.gerencianet.gnsdk.interfaces;

import br.com.gerencianet.gnsdk.models.Token;

/**
 * Created by francisco on 22/05/15.
 */
public interface IRequester {
    public void doPost(Token accessToken);
}
