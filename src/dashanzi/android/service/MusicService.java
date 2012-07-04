package dashanzi.android.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import dashanzi.android.R;

/**
 * 背景音乐
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

public class MusicService extends Service {

	private static final String tag = "MusicService";
	private MediaPlayer mediaPlayer;

	@Override
	public void onCreate() {
		super.onCreate();
		if (mediaPlayer != null) {
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}

		// initial mediaPlayer
		mediaPlayer = new MediaPlayer();
		mediaPlayer = MediaPlayer.create(this, R.raw.music);
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		if (mediaPlayer == null) {
			Log.e(tag, "111  mediaPlayer is null !");
			return;
		}
		String flag = intent.getStringExtra("flag");
		if (flag == null) {
			Log.e(tag, "222  flag is null !");
			return;
		}
		if (flag.equals("pause")) {
			mediaPlayer.pause();
		} else if (flag.equals("play")) {
			if(!mediaPlayer.isPlaying()){
				mediaPlayer.start();
			}
		}
	}

	@Override
	public void onDestroy() {
		Log.e(tag, "MusicService onDestroy");
		if (mediaPlayer != null) {
//			mediaPlayer.release();
			mediaPlayer.stop();
			mediaPlayer = null;
		}
		super.onDestroy();
	}

}
