package com.funtv.utils;

import android.content.Context;
import android.media.AudioManager;

public class Volume {

	public static void AdjustVolume(Context context, int direction) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		if (audioManager != null)
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,direction, AudioManager.FX_FOCUS_NAVIGATION_UP);
	}
}
