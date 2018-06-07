package com.fongmi.android.tv.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fongmi.android.tv.App;

public class Prefers {

	private static final String SIZE = "size";
	private static final String DELAY = "delay";
	private static final String ENTER = "enter";

	private static SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(App.getInstance());
	}

	private static Integer getInt(String key, int defaultValue) {
		return getPreferences().getInt(key, defaultValue);
	}

	private static void putInt(String key, int value) {
		getPreferences().edit().putInt(key, value).apply();
	}

	private static Boolean getBoolean(String key) {
		return getPreferences().getBoolean(key, false);
	}

	private static void putBoolean(String key, boolean value) {
		getPreferences().edit().putBoolean(key, value).apply();
	}

	public static int getSize() {
		return getInt(SIZE, 0);
	}

	public static void putSize(int value) {
		putInt(SIZE, value);
	}

	static int getDelay() {
		return getInt(DELAY, 1);
	}

	static void putDelay(int value) {
		putInt(DELAY, value);
	}

	public static boolean isEnter() {
		return getBoolean(ENTER);
	}

	static void putEnter(boolean value) {
		putBoolean(ENTER, value);
	}
}
