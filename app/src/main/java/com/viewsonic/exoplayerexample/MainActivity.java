package com.viewsonic.exoplayerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;

public class MainActivity extends AppCompatActivity {
	private PlayerView playerView;
	private SimpleExoPlayer player;
	private boolean playWhenReady = true;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		Runnable initExoThread = new Runnable() {
			@Override
			public void run() {
				playerView = findViewById(R.id.video_view);
				player = ExoPlayerFactory.newSimpleInstance(mContext);
				player.setPlayWhenReady(playWhenReady);
				DataSpec dataSpec = new DataSpec(Uri.parse("file:///android_asset/test1.mp4"));
				final AssetDataSource assetDataSource = new AssetDataSource(mContext);
				try {
					assetDataSource.open(dataSpec);
				} catch (AssetDataSource.AssetDataSourceException e) {
					e.printStackTrace();
				}
				DataSource.Factory factory = new DataSource.Factory() {
					@Override
					public DataSource createDataSource() {
						return assetDataSource;
					}
				};
				MediaSource mediaSource = new ExtractorMediaSource(assetDataSource.getUri(),
						factory, new DefaultExtractorsFactory(), null, null);

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						player.prepare(mediaSource);
						playerView.setPlayer(player);
					}
				});
			}
		};
		new Thread(initExoThread).start();
	}
}