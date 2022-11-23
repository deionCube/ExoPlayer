package com.forcetech;

import android.net.Uri;

public class Port {

	public static int FORCE = 9001;
	public static int MTV = 9002;
	public static int P2P = 9906;
	public static int P3P = 9907;
	public static int P4P = 9908;
	public static int P5P = 9909;
	public static int P6P = 9910;
	public static int P7P = 9911;
	public static int P8P = 9912;
	public static int P9P = 9913;

	public static int get(String url) {
		switch (Uri.parse(url).getScheme()) {
			case "P2p":
				return FORCE;
			case "p2p":
				return P2P;
			case "p3p":
				return P3P;
			case "p4p":
				return P4P;
			case "p5p":
				return P5P;
			case "p6p":
				return P6P;
			case "p7p":
				return P7P;
			case "p8p":
				return P8P;
			case "p9p":
				return P9P;
			case "mitv":
				return MTV;
			default:
				return -1;
		}
	}
}
