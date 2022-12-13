package com.fongmi.android.tv.source;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.impl.AsyncCallback;
import com.forcetech.Port;
import com.google.android.exoplayer2.PlaybackException;
import com.gsoft.mitv.MainActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Force {

	private final OkHttpClient client;
	private final Handler handler;
	private AsyncCallback callback;
	private boolean init;

	private static class Loader {
		static volatile Force INSTANCE = new Force();
	}

	public static Force get() {
		return Loader.INSTANCE;
	}

	public Force() {
		this.handler = new Handler(Looper.getMainLooper());
		this.client = new OkHttpClient();
	}

	public void init() {
		App.get().bindService(new Intent(App.get(), MainActivity.class), mConn, Context.BIND_AUTO_CREATE);
	}

	public void start(AsyncCallback callback, String source) {
		if (!init) init();
		while (!init) SystemClock.sleep(10);
		this.callback = callback;
		this.onPrepare(source);
	}

	public void destroy() {
		try {
			if (init) App.get().unbindService(mConn);
			init = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onPrepare(String source) {
		int port = Port.get(source);
		Uri uri = Uri.parse(source);
		String id = uri.getLastPathSegment();
		String cmd = "http://127.0.0.1:" + port + "/cmd.xml?cmd=switch_chan&server=" + uri.getHost() + ":" + uri.getPort() + "&id=" + id;
		String result = "http://127.0.0.1:" + port + "/" + id;
		handler.post(() -> onResponse(result));
		connect(cmd);
	}

	private void connect(String url) {
		try {
			client.newCall(new Request.Builder().url(url).header("user-agent", "MTV").build()).execute();
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

	private final ServiceConnection mConn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			init = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			init = false;
		}
	};
}
