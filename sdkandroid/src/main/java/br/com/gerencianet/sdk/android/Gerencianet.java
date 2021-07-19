package br.com.gerencianet.sdk.android;

import android.content.res.AssetManager;

import java.util.Map;

public class Gerencianet extends Endpoints {
	public Gerencianet(Map<String, Object> options) throws Exception {
		super(options);
	}

	public Gerencianet(Map<String, Object> options, AssetManager cert) throws Exception {
		super(options, cert);
	}
}
