package com.fongmi.android.tv.network.task;

import android.os.Handler;
import android.os.Looper;

import com.fongmi.android.tv.impl.AsyncCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckTask {

	private final ExecutorService executor;
	private final AsyncCallback callback;
	private final Handler handler;

	public static CheckTask create(AsyncCallback callback) {
		return new CheckTask(callback);
	}

	public CheckTask(AsyncCallback callback) {
		this.executor = Executors.newSingleThreadExecutor();
		this.handler = new Handler(Looper.getMainLooper());
		this.callback = callback;
	}

	public void run() {
		executor.submit(this::doInBackground);
	}

	private void doInBackground() {
		try {
			Request request = new Request.Builder().url("https://firebase.google.com/").build();
			Response response = new OkHttpClient().newCall(request).execute();
			onPostExecute(response.code() == 200);
		} catch (Exception e) {
			onPostExecute(false);
		}
	}

	private void onPostExecute(boolean success) {
		handler.post(() -> {
			if (callback != null) callback.onResponse(success);
		});
	}
}