package com.fongmi.android.tv.source;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.bean.Config.Core;
import com.fongmi.android.tv.impl.AsyncCallback;
import com.google.android.exoplayer2.PlaybackException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvbus.engine.Listener;
import com.tvbus.engine.TVCore;

public class TVBus implements Listener {

    private final Handler handler;
    private AsyncCallback callback;
    private TVCore tvcore;

    private static class Loader {
        static volatile TVBus INSTANCE = new TVBus();
    }

    public static TVBus get() {
        return Loader.INSTANCE;
    }

    public TVBus() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void init(Core core) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) return;
        tvcore = new TVCore().listener(this);
        tvcore.auth(core.getAuth()).name(core.getName()).pass(core.getPass()).broker(core.getBroker());
        tvcore.serv(0).play(8902).mode(1).init(App.get());
    }

    public void start(AsyncCallback callback, String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) return;
        setCallback(callback);
        tvcore.start(url);
    }

    public void stop() {
        if (tvcore != null) tvcore.stop();
    }

    public void destroy() {
        if (tvcore != null) tvcore.quit();
    }

    private void setCallback(AsyncCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onPrepared(String result) {
        JsonObject json = new Gson().fromJson(result, JsonObject.class);
        if (json.get("hls") == null) return;
        handler.post(() -> onResponse(json.get("hls").getAsString()));
    }

    @Override
    public void onStop(String result) {
        JsonObject json = new Gson().fromJson(result, JsonObject.class);
        int errno = json.get("errno").getAsInt();
        if (errno < 0) handler.post(this::onError);
    }

    private void onResponse(String result) {
        if (callback != null) callback.onResponse(result);
    }

    private void onError() {
        if (callback != null) callback.onError(new PlaybackException(null, null, 0));
    }

    @Override
    public void onInited(String result) {
    }

    @Override
    public void onStart(String result) {
    }

    @Override
    public void onInfo(String result) {
    }

    @Override
    public void onQuit(String result) {
    }
}
