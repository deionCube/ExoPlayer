package com.fongmi.android.tv.network;

import com.fongmi.android.tv.utils.Utils;
import com.google.common.net.HttpHeaders;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Connector {

	private HttpURLConnection conn;
	private final String url;

	public static Connector link(String url) throws IOException {
		return new Connector(url).connect();
	}

	public Connector(String url) {
		this.url = url;
	}

	public Connector connect() throws IOException {
		conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setInstanceFollowRedirects(false);
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
		conn.setUseCaches(false);
		conn.setDoInput(false);
		conn.setDoInput(true);
		conn.connect();
		return this;
	}

	public String getResult() throws IOException {
		String line;
		StringBuilder result = new StringBuilder();
		InputStream in = new BufferedInputStream(conn.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		while ((line = bufferedReader.readLine()) != null) result.append(line);
		in.close();
		return result.toString();
	}

	public String getPath() throws IOException {
		if (isRedirect()) return conn.getHeaderField("Location");
		return url;
	}

	public boolean isRedirect() throws IOException {
		return conn.getResponseCode() / 100 == 3;
	}
}
