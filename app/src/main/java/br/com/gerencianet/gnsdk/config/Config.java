package br.com.gerencianet.gnsdk.config;

/**
 * Created by francisco on 19/03/15.
 */
public class Config {
    private String accountCode;
    private boolean sandbox;

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public boolean isSandbox() {
        return sandbox;
    }

    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }
}
