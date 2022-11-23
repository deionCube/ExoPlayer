package com.fongmi.android.tv.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.BuildConfig;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.impl.AsyncCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileUtil {

	private static final String TAG = FileUtil.class.getSimpleName();

	public static File getCacheDir() {
		return App.get().getExternalCacheDir();
	}

	public static File getCacheFile(String fileName) {
		return new File(getCacheDir(), fileName);
	}

	public static void clearDir(File dir) {
		if (dir == null) return;
		if (dir.isDirectory()) for (File file : dir.listFiles()) clearDir(file);
		if (dir.delete()) Log.d(TAG, "Deleted:" + dir.getPath());
	}

	private static String getMimeType(String fileName) {
		String mimeType = URLConnection.guessContentTypeFromName(fileName);
		return TextUtils.isEmpty(mimeType) ? "*/*" : mimeType;
	}

	public static String getAssets(String fileName) {
		try {
			return getInputStream(App.get().getAssets().open(fileName));
		} catch (Exception e) {
			return "";
		}
	}

	private static String getInputStream(InputStream is) throws Exception {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String text;
		while ((text = br.readLine()) != null) sb.append(text).append("\n");
		br.close();
		return sb.toString();
	}

	private static Uri getShareUri(File file) {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.N ? Uri.fromFile(file) : FileProvider.getUriForFile(App.get(), App.get().getPackageName() + ".provider", file);
	}

	public static void checkUpdate(long version) {
		if (version > BuildConfig.VERSION_CODE) {
			Notify.show(R.string.app_update);
			download();
		} else {
			clearDir(getCacheDir());
		}
	}

	private static void download() {
		new OkHttpClient().newCall(new Request.Builder().url("").build()).enqueue(new AsyncCallback() {
			@Override
			public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
				if (!response.isSuccessful()) return;
				File file = getCacheFile("update.apk");
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(response.body().bytes());
				fos.close();
				open(file);
			}
		});
	}

	private static void open(File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		intent.setDataAndType(getShareUri(file), FileUtil.getMimeType(file.getName()));
		App.get().startActivity(intent);
	}
}
