package com.fongmi.android.tv.bean;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.fongmi.android.tv.network.task.EpgTask;
import com.fongmi.android.tv.utils.Prefers;
import com.fongmi.android.tv.utils.Utils;

import java.util.Locale;

@Entity
public class Channel extends Bean {

	@NonNull
	@PrimaryKey
	private String number;
	private String core;
	private String epg;
	private String url;
	private String ua;
	private boolean dynamic;

	@Ignore
	private Epg data;
	@Ignore
	private Type type;

	public static Channel create(String number) {
		return new Channel(String.format(Locale.getDefault(), "%03d", Integer.valueOf(number)));
	}

	public Channel() {
		this("");
	}

	@Ignore
	public Channel(@NonNull String number) {
		this.number = number;
	}

	@NonNull
	public String getNumber() {
		return number;
	}

	public void setNumber(@NonNull String number) {
		this.number = number;
	}

	public String getCore() {
		return TextUtils.isEmpty(core) ? "exo" : core;
	}

	public void setCore(String core) {
		this.core = core;
	}

	public String getEpg() {
		return epg;
	}

	public void setEpg(String epg) {
		this.epg = epg;
	}

	public String getUrl() {
		return TextUtils.isEmpty(url) ? "" : url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUa() {
		return TextUtils.isEmpty(ua) ? Utils.getUserAgent() : ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public Epg getData() {
		return data == null ? new Epg() : data;
	}

	public void setData(Epg data) {
		this.data = data;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getScheme() {
		return Uri.parse(getUrl()).getScheme().toLowerCase();
	}

	public boolean isTVBus() {
		return getScheme().equals("tvbus");
	}

	public boolean isForce() {
		return getScheme().startsWith("p") || getScheme().equals("mitv");
	}

	public boolean isZLive() {
		return getScheme().equals("zlive");
	}

	public void putKeep() {
		Prefers.putKeep(getNumber());
	}

	public String getDigital() {
		return Integer.valueOf(getNumber()).toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Channel)) return false;
		Channel it = (Channel) obj;
		return getNumber().equals(it.getNumber());
	}
}
