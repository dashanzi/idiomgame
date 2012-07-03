package dashanzi.android.util;

import android.util.Log;

import com.iflytek.speech.SynthesizerPlayer;

import dashanzi.android.Constants;

public class VoiceUtil {
	private static final String tag = "VoiceUtil";

	/**
	 * 语音合成
	 * @param player 合成播放器
	 * @param voiceText 合成的文本信息
	 * @param gender 声音性别
	 */
	public static void SynthesizerVoice(final SynthesizerPlayer player,
			final String voiceText, final int gender) {

		if (player == null) {
			Log.e(tag, "player == null");
			return;
		}

		new Thread() {
			public void run() {
				switch (gender) {
				case Constants.Player.MAN:
					player.setVoiceName("xiaoyu");
					break;
				case Constants.Player.FEMALE:
					player.setVoiceName("xiaoyan");
					break;
				default:
					break;
				}
				player.playText(voiceText, null, null);
			};
		}.start();
	}

}
