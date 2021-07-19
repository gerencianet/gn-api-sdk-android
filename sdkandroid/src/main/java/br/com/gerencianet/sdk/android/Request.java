  package br.com.gerencianet.sdk.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.gerencianet.sdk.android.exceptions.AuthorizationException;
import br.com.gerencianet.sdk.android.exceptions.GerencianetException;

public class Request {

	private final HttpURLConnection client;

	public Request(String method, HttpURLConnection conn) throws IOException {
		this.client = conn;
		this.client.setRequestProperty("Content-Type", "application/json");
		this.client.setRequestProperty("charset", "UTF-8");
		this.client.setRequestProperty("api-sdk", "android-" + Config.getVersion());

		if (method.toUpperCase().equals("PATCH")) {
			this.client.setRequestProperty("X-HTTP-Method-Override", "PATCH");
			this.client.setRequestMethod("POST");
		} else {
			this.client.setRequestMethod(method.toUpperCase());
		}

	}

	public void addHeader(String key, String value) {
		client.setRequestProperty(key, value);
	}

	public JsonObject send(JsonObject requestOptions)
			throws AuthorizationException, GerencianetException, IOException {

		if (!client.getRequestMethod().toLowerCase().equals("get")) {
			byte[] postDataBytes;
			postDataBytes = requestOptions.toString().getBytes("UTF-8");
			this.client.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			client.setDoOutput(true);
			OutputStream os = client.getOutputStream();
			os.write(postDataBytes);
			os.flush();
			os.close();
		}
		int responseCode = client.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
			InputStream responseStream = client.getInputStream();
			Reader reader = new InputStreamReader(responseStream, "UTF-8");
			return new Gson().fromJson(reader, JsonObject.class);
		} else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED
				|| responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
			throw new AuthorizationException();
		} else {
			InputStream responseStream = client.getErrorStream();
			Reader reader = new InputStreamReader(responseStream, "UTF-8");
			JsonObject result = new Gson().fromJson(reader, JsonObject.class);
			throw new GerencianetException(result);
		}
	}
}
