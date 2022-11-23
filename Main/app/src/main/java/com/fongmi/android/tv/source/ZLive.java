package com.fongmi.android.tv.source;

import android.os.Handler;
import android.os.Looper;

import com.fongmi.android.tv.impl.AsyncCallback;
import com.fongmi.android.tv.utils.FileUtil;
import com.google.android.exoplayer2.PlaybackException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ZLive {

	private final String BASE = "http://127.0.0.1:6677/stream/";
	private final OkHttpClient client;
	private final Handler handler;
	private AsyncCallback callback;
	private boolean init;

	private static class Loader {
		static volatile ZLive INSTANCE = new ZLive();
	}

	public static ZLive get() {
		return Loader.INSTANCE;
	}

	public ZLive() {
		this.handler = new Handler(Looper.getMainLooper());
		this.client = new OkHttpClient();
	}

	public void init() {
		try {
			com.east.android.zlive.ZLive.INSTANCE.OnLiveStart(6677);
			init = true;
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void start(AsyncCallback callback, String source) {
		if (!init) init();
		this.callback = callback;
		this.onPrepare(source);
	}

	public void destroy() {
		try {
			if (init) com.east.android.zlive.ZLive.INSTANCE.OnLiveStop();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private String getLive(String uuid) {
		return BASE + "live?uuid=" + uuid;
	}

	private String getOpen(String uuid) {
		return BASE + "open?uuid=" + uuid;
	}

	public void onPrepare(String source) {
		String[] split = source.split("/");
		String server = split[2];
		String uuid = split[3];
		String param = "&group=5850&mac=00:00:00:00:00:00&dir=";
		String result = getLive(uuid) + "&server=" + server + param + FileUtil.getCacheDir().getAbsolutePath();
		handler.post(() -> onResponse(result));
		connect(getOpen(uuid));
	}

	private void connect(String url) {
		try {
			client.newCall(new Request.Builder().url(url).build()).execute();
		} catch (Exception e) {
			handler.post(this::onError);
		}
	}

	private void onResponse(String result) {
		if (callback != null) callback.onResponse(result);
	}

	private void onError() {
		if (callback != null) callback.onError(new PlaybackException(null, null, 0));
	}
}
