package com.fongmi.android.tv.bean;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.room.Ignore;

import com.fongmi.android.tv.utils.Utils;

public class Bean {

	@Ignore
	private boolean select;
	private String name;
	private String logo;

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public String getName() {
		return TextUtils.isEmpty(name) ? "" : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return TextUtils.isEmpty(logo) ? "" : logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void loadLogo(ImageView view) {
		view.setVisibility(getLogo().isEmpty() ? View.GONE : View.VISIBLE);
		if (!getLogo().isEmpty()) Utils.loadImage(getLogo(), view);
	}
}
