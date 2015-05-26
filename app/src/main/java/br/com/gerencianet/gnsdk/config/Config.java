package br.com.gerencianet.gnsdk.config;

/**
 * Created by francisco on 19/03/15.
 */
public class Config {

    private String clientId;
    private String clientSecret;
    private String grantType = "client_credentials";
    private boolean sandbox;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public boolean isSandbox() {
        return sandbox;
    }

    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }

    public String getGrantType() {
        return grantType;
    }
}
