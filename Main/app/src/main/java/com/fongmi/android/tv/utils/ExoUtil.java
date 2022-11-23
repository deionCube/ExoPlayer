package com.fongmi.android.tv.utils;

import android.net.Uri;

import com.fongmi.android.tv.App;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSource;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.MimeTypes;

public class ExoUtil {

	private static final long VOD_DURATION = 10 * 60 * 1000; //10 minute
	private static DatabaseProvider database;
	private static Cache cache;

	public static boolean isVoD(long duration) {
		return duration > VOD_DURATION;
	}

	public static MediaSource getSource(String userAgent, String url) {
		DataSource.Factory factory = url.startsWith("rtmp") ? new RtmpDataSource.Factory() : getDataSourceFactory(userAgent);
		MediaItem.Builder builder = new MediaItem.Builder().setUri(Uri.parse(url));
		if (url.contains("php") || url.contains("m3u8")) builder.setMimeType(MimeTypes.APPLICATION_M3U8);
		return new DefaultMediaSourceFactory(factory).createMediaSource(builder.build());
	}

	private static synchronized DataSource.Factory getHttpDataSourceFactory(String userAgent) {
		return new DefaultHttpDataSource.Factory().setUserAgent(userAgent).setConnectTimeoutMs(5000).setReadTimeoutMs(5000).setAllowCrossProtocolRedirects(true);
	}

	private static synchronized DataSource.Factory getDataSourceFactory(String userAgent) {
		DefaultDataSource.Factory upstreamFactory = new DefaultDataSource.Factory(App.get(), getHttpDataSourceFactory(userAgent));
		return buildReadOnlyCacheDataSource(upstreamFactory, getCache());
	}

	private static CacheDataSource.Factory buildReadOnlyCacheDataSource(DataSource.Factory upstreamFactory, Cache cache) {
		return new CacheDataSource.Factory().setCache(cache).setUpstreamDataSourceFactory(upstreamFactory).setCacheWriteDataSinkFactory(null).setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
	}

	private static synchronized DatabaseProvider getDatabase() {
		if (database == null) database = new StandaloneDatabaseProvider(App.get());
		return database;
	}

	private static synchronized Cache getCache() {
		if (cache == null) cache = new SimpleCache(FileUtil.getCacheDir(), new NoOpCacheEvictor(), getDatabase());
		return cache;
	}
}
