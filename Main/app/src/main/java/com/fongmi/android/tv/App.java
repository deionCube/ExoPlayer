package com.fongmi.android.tv;

import android.app.Application;

import com.fongmi.android.tv.source.Force;

public class App extends Application {

	private static App instance;

	public App() {
		instance = this;
	}

	public static App get() {
		return instance;
	}
}