package com.forcetech.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.forcetech.Port;
import com.forcetech.android.ForceTV;

public class P3PService extends Service {

	private ForceTV forceTV;

	@Override
	public IBinder onBind(Intent intent) {
		forceTV = new ForceTV();
		forceTV.start("p3p", Port.P3P);
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		if (forceTV != null) forceTV.stop();
		return super.onUnbind(intent);
	}
}
