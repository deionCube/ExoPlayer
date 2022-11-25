package com.fongmi.android.tv;

import android.app.Application;

import com.fongmi.android.tv.source.Force;
import com.fongmi.android.tv.source.ZLive;

public class App extends Application {

	private static App instance;

	public App() {
		instance = this;
	}

	public static App get() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Force.get().init();
	}
}