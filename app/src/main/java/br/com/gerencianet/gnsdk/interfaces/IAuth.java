package br.com.gerencianet.gnsdk.interfaces;

/**
 * Created by francisco on 19/03/15.
 */
public interface IAuth {
    public void getAccessToken();
    public void setAuthListener(IAuthListener listener);
}
