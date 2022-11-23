package com.fongmi.android.tv.impl;

import androidx.annotation.NonNull;

import com.fongmi.android.tv.bean.Config;
import com.google.android.exoplayer2.PlaybackException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class AsyncCallback implements ValueEventListener, Callback {

	public void onResponse(boolean success) {
	}

	public void onResponse(String result) {
	}

	public void onResponse(Config config) {
	}

	public void onError(PlaybackException error) {
	}

	@Override
	public void onFailure(@NonNull Call call, @NonNull IOException e) {
	}

	@Override
	public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
	}

	@Override
	public void onDataChange(@NonNull DataSnapshot data) {
		onResponse(Config.get(data));
	}

	@Override
	public void onCancelled(@NonNull DatabaseError data) {
	}
}
