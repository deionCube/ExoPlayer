package com.fongmi.android.tv.network.task;

import android.os.Handler;
import android.os.Looper;

import com.fongmi.android.tv.R;
import com.fongmi.android.tv.bean.Channel;
import com.fongmi.android.tv.bean.Epg;
import com.fongmi.android.tv.impl.AsyncCallback;
import com.fongmi.android.tv.network.Connector;
import com.fongmi.android.tv.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EpgTask {

    private final SimpleDateFormat formatDate;
    private final SimpleDateFormat formatTime;
    private ExecutorService executor;
    private AsyncCallback callback;
    private final Handler handler;

    public static EpgTask create(AsyncCallback callback) {
        return new EpgTask(callback);
    }

    public EpgTask(AsyncCallback callback) {
        this.formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        this.formatTime = new SimpleDateFormat("yyyy-MM-ddHH:mm", Locale.getDefault());
        this.executor = Executors.newSingleThreadExecutor();
        this.handler = new Handler(Looper.getMainLooper());
        this.callback = callback;
    }

    public EpgTask run(Channel item) {
        if (item.getEpg().isEmpty()) callback.onResponse(Utils.getString(R.string.channel_epg));
        else executor.submit(() -> doInBackground(item));
        return this;
    }

    private void doInBackground(Channel item) {
        try {
            String date = formatDate.format(new Date());
            String epg = String.format("https://epg.112114.xyz/?ch=%s&date=%s", item.getEpg(), date);
            String result = Connector.link(epg).getResult();
            item.setData(Epg.objectFrom(result, formatTime));
            onPostExecute(item.getData().getEpg());
        } catch (Exception e) {
            onPostExecute(Utils.getString(R.string.channel_epg));
        }
    }

    private void onPostExecute(String result) {
        handler.post(() -> {
            if (callback != null) callback.onResponse(result);
        });
    }

    public void cancel() {
        if (executor != null) executor.shutdownNow();
        executor = null;
        callback = null;
    }
}