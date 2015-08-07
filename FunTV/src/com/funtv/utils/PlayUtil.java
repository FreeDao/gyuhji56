package com.funtv.utils;

import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.widget.TextView;

import com.funtv.R;
import com.funtv.FunApplication;
import com.funtv.player.entity.EpisodeInfo;
import com.funtv.player.entity.PlayerInfo;

public class PlayUtil {

	private static final String TAG = "PlayUtil";

	@SuppressLint("DefaultLocale")
	public static void updateTextViewWithTimeFormat(TextView view, int second) {
		int hh = second / 3600;
		int mm = second % 3600 / 60;
		int ss = second % 60;
		String strTemp = null;
		if (0 != hh) {
			strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
		} else {
			strTemp = String.format("%02d:%02d", mm, ss);
		}
		view.setText(strTemp);
	}

	public static String getVideoTitle(String meadiaName, EpisodeInfo episodeInfo) {
		String videoTitle = "";
		if (!StringUtil.isEmpty(meadiaName)) {
			StringBuffer nameBuffer = new StringBuffer();
			nameBuffer.append(meadiaName);
			nameBuffer.append(" ");
			if (null != episodeInfo) {
				String taskName = episodeInfo.getTaskname();
				if (!StringUtil.isEmpty(taskName) && !meadiaName.equals(taskName)) {
					nameBuffer.append(taskName);
				}
			}
			videoTitle = nameBuffer.toString();
		}
		return videoTitle;
	}

	public static boolean isLive(String mediaType){
		boolean isLive = false;
		if(!StringUtil.isEmpty(mediaType)){
			if(mediaType.equals(Constans.MEDIA_LIVE)){
				isLive = true;
				return isLive;
			}
		}
		return isLive;
	}

	public static String getMediaName(String fspUrl) {
		String mediaName = "";
		if (StringUtil.isEmpty(fspUrl)) {
			return mediaName;
		}
		mediaName = getQueryParameter(Constans.MOIVE_NAME_FLAG, Constans.SPLIT_LINE, fspUrl);
		return mediaName;
	}

	public static String getQueryParameter(String startSrc, String endSrc, String url) {
		String param = "";
		if (StringUtil.isEmpty(startSrc) || StringUtil.isEmpty(endSrc) || StringUtil.isEmpty(url)) {
			return param;
		}
		try {
			int startPos = url.indexOf(startSrc);
			if (startPos != -1) {
				url = url.substring(startPos);
				int endPos = url.indexOf(endSrc);
				if (endPos != -1) {
					url = url.substring(startSrc.length(), endPos);
				} else {
					url = url.substring(startSrc.length(), url.length());
				}
				param = URLDecoder.decode(url, "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!StringUtil.isEmpty(param))
			param = param.trim();
		return param;
	}

	/**
	 *  追加FSP短链接缺少的字段
	 *
	 *  sz=表示每段文件的长度，如果是单段的，ts和sz字段相等的
	 *  vt=0 (0:表示电影,vt=1表示连续剧.vt=2表示直播)
	 *	m=0 （原意指mid）
	 *	ts=sz (文件总长度(单位字节))
	 *	td=t*1000 (单位毫秒, 表示播放时长, 如果t不存在, 则填0)
	 *	crt=0 （0：表示非版权片）
	 *	idx=1 （表示连续剧剧集的位置，只对电视剧集有效）
	 *	jm=表示清晰度加上mid @表示高清，+表示超清，L表流畅，D表dvd，S表1080P
	 *
	 * @param fsp
	 * @param calarty
	 * @param playerInfo
	 * @return
	 */
	public static String addLackFspField(String fsp,String calarty,PlayerInfo playerInfo) {
		String finlaCompleteFsp = "";

		if (StringUtil.isEmpty(fsp)) {
			return finlaCompleteFsp;
		}

		if (fsp.contains("|vt=2")) {
			return fsp;
		}

		if (fsp.contains("|t=")) {
			fsp = fsp.replace("|t=", "|td=");
		}

		StringBuilder stringBuilder = new StringBuilder(fsp);
		if (!fsp.contains("|td=")) {
			stringBuilder.append(Constans.SPLIT_LINE);
			stringBuilder.append("td=0");
		}

		if (!fsp.contains("|torrent=")) {
			stringBuilder.append(Constans.SPLIT_LINE);
			stringBuilder.append("torrent=null.fsp");
		}

		if (!fsp.contains("|ts=")) {
			stringBuilder.append(Constans.SPLIT_LINE);
			stringBuilder.append("ts=");
			if(fsp.contains("|sz=")){
				stringBuilder.append(getQueryParameter("sz=", Constans.SPLIT_LINE, fsp));
			}else{
				stringBuilder.append("0");
				stringBuilder.append(Constans.SPLIT_LINE);
				stringBuilder.append("sz=0");
			}
		}

		if (!fsp.contains("|m=")) {
			stringBuilder.append(Constans.SPLIT_LINE);
			if(null != playerInfo && !StringUtil.isEmpty(playerInfo.getMid())){
				stringBuilder.append("m=");
				stringBuilder.append(playerInfo.getMid());
			}else{
				stringBuilder.append("m=109725");
			}
		}
		if (!fsp.contains("|vt=")) {//0:表示电影,vt=1表示连续剧.vt=2表示直播
			stringBuilder.append(Constans.SPLIT_LINE);
			if(null != playerInfo && !StringUtil.isEmpty(playerInfo.getMtype())){
				stringBuilder.append("vt=");
				if(playerInfo.getMtype().equals(Constans.MEDIA_MOVIE)){
					stringBuilder.append(0);
				}else if(playerInfo.getMtype().equals(Constans.MEDIA_TV)){
					stringBuilder.append(1);
				}else if(playerInfo.getMtype().equals(Constans.MEDIA_LIVE)){
					stringBuilder.append(2);
				}else{
					stringBuilder.append(0);
				}
			}else{
				stringBuilder.append("vt=0");
			}
		}
		if (!fsp.contains("|crt=")) {
			stringBuilder.append(Constans.SPLIT_LINE);
			stringBuilder.append("crt=0");
		}
		if (!fsp.contains("|idx=")) {
			stringBuilder.append(Constans.SPLIT_LINE);
			stringBuilder.append("idx=1");
		}
		if (!fsp.contains("|jm=")) {
			stringBuilder.append(Constans.SPLIT_LINE);
			if(!StringUtil.isEmpty(calarty)){
				stringBuilder.append("jm=");
				stringBuilder.append(getP2PClartityFlag(calarty));
			}else{
				stringBuilder.append("jm=+");
			}
		}
		finlaCompleteFsp = stringBuilder.toString().trim();
		return finlaCompleteFsp;
	}

	public static String getQueryParameter(String fspUrl, String parameter) {
		String str = "";
		if (StringUtil.isEmpty(fspUrl)) {
			return parameter;
		}
		try {
			Uri uri = Uri.parse(fspUrl);
			String queryParameter = uri.getQueryParameter(parameter);
			if (null != queryParameter) {
				str = URLDecoder.decode(str, "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!StringUtil.isEmpty(str))
			str = str.trim();
		return str;

	}

	public static String getP2PClartityFlag(String defaultClarity) {
		String clartityFlag = "";
		if (StringUtil.isEmpty(defaultClarity)) {
			return clartityFlag;
		}
		Context context = FunApplication.getInstance().getApplicationContext();
		String[] mPinfoDefinitions = context.getResources().getStringArray(R.array.video_rate);
		if (defaultClarity.equals(mPinfoDefinitions[0])) {
			clartityFlag = Constans.SUPER_1080_TV;
		} else if (defaultClarity.equals(mPinfoDefinitions[1])) {
			clartityFlag = Constans.SUPER_TV;
		} else if (defaultClarity.equals(mPinfoDefinitions[2])) {
			clartityFlag = Constans.HIGH_TV;
		} else if (defaultClarity.equals(mPinfoDefinitions[3])) {
			clartityFlag = Constans.DVD_TV;
		} else if (defaultClarity.equals(mPinfoDefinitions[4])) {
			clartityFlag = Constans.TV;
		} else if (defaultClarity.equals(mPinfoDefinitions[5])) {
			clartityFlag = Constans.TV;
		}
		return clartityFlag;
	}

	public static void playTone(Context context, int sound) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

		int ringerMode = audioManager.getRingerMode();
		if ((ringerMode == AudioManager.RINGER_MODE_SILENT) || (ringerMode == AudioManager.RINGER_MODE_VIBRATE)) {
			return;
		}
		audioManager.playSoundEffect(sound, audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
	}
}
