package com.funtv.utils;

public interface Constans {

	public static final int CLARITY_DEFAULT_DELAY_DIMISS_TIME = 5 * 1000;

	public static final int NOT_IN_KERNEL = -1;

	public static final int NOT_COMPLETE_IN_EKRNEL = 0;

	public static final int COMPLETED_IN_KERNEL = 1;

	public static final int NOT_PLAY_STATE= 0;

	public static final String CLARITY_FLAG="jm=";//区分清晰度字段

	public static final String HIGH_TV ="@";//@表示高清

	public static final String SUPER_TV ="+";//+表示超清

	public static final String TV="L";//L表示流畅

	public static final String DVD_TV="D";//D表示dvd

	public static final String SUPER_1080_TV="S";//s表示1080p

	public static final String MEIDA_NAME_FLAG="fn=";//电视剧的媒体名字字段

	public static final String MOIVE_NAME_FLAG="mn=";//电影媒体名字字段

	public static final String SPLIT_LINE ="|";//各个字段的分隔符

	public static final String VIDEO_TYPE="vt=";//区分是电视剧和电影字段

	public static final String MOIVE_TYPE="0";//表示电影

	public static final String EPISODE_TYPE="1";//表示电视剧

	public static final String LIVE_TYPE="2";//表示直播

	public static final String FSUDID ="fsudid";//表示设备唯一ID号

	public static final String FF_BUFFER="fbuffer";//首次缓冲上报

	public static final String BOOT_START="bootstrap";//启动上报

	public static final String PLAY_TIME= "playtm";//播放时长上报；

	public static final String DRAG_BUFFER="dbuffer";//拖动缓冲上报

	public static final String STUCK_BUFFER="stuck";//拖动缓冲上报

	public static final String PAGELOADERR="pageloaderr"; // 网页加载出错上报

	public static final String MEDIA_CARTOON = "cartoon";//卡通

	public static final String MEDIA_MOVIE = "movie";// 电影

	public static final String MEDIA_TV = "tv";// 电视

	public static final String MEDIA_VARIETY = "variety";// 综艺

	public static final String MEDIA_SVIDEO = "svideo";// 小视频

	public static final String MEDIA_LIVE = "live";// 直播

	public static final String CLARITY_SHOT ="0";//shot

	public static final String CLARITY_TV ="1";//tv

	public static final String CLARITY_DVD = "2";//dvd

	public static final String CLARITY_HIGHDVD = "3";//highdvd

	public static final String CLARITY_SUPERDVD = "4";// superdvd

	public static final String CLARITY_S_SUPERDVD = "5";// s_super_dvd

	public static final int SUPPORT_MIN_SKD_VERSION = 8;

	public static final String RELATED_MEDIA_LINK_URL = "related_media_link_url";

	public static final String RELATED_MEDIA_ACTIONG = "com.fun.tv.open.media";


	public static final String ACTION_CHECK_SERVER_TIME = "com.fun.tv.server.time";

	public static final String CHECK_SERVER_TIME = "server_time";

	public static final String VIDEO_TYPE_LONG="1";//长视频

	public static final String VIDEO_TYPE_SHORT="2";//段视频

	public static final String VIDEO_TYPE_LIVE="3";//直播

	public final static String CRASH_INFO = "crash_tv_";//app崩溃的文件头部标识

	public static final String APP_TYPE = "funtv";//app的类型

	public static final String DEV_TYPE = "tv"; // 设备类型(dev)：<aphone/apad/iphone/ipad/tv>_<操作系统>_<设备型号>

	public static final String CRASH_REPORT_INFO="reported_error";//崩溃上报用sp存储的key值

	public static final String CRASH_REPORT_FLAG="error";//是否崩溃上报的一个标记

	public static final long KEY_DELAY = 1000;//延迟执行key事件时间间隔

	public static final Object TAG_VOLLEY = "com.fun.tv.tag.volley";

	public static final String REPORT_VERSION="1";//上报协议版本

	public static final String ACTION_P2PKERNEL_START ="com.fun.tv.P2PSTART";

	public static final int PLAY_STEP_LONG = 10;
	public static final int PLAY_STEP_MIDDLE = 5;
	public static final int PLAY_STEP_SHORT = 1;

	public static final int FAST_SPEED_LONG = 6;
	public static final int MEDIUM_SPEED_LONG = 4;
	public static final int SLOW_SPEED_LONG = 1;

	public static final int FAST_SPEED_MIDDLE = 4;
	public static final int MEDIUM_SPEED_MIDDLE = 2;
	public static final int SLOW_SPEED_MIDDLE = 1;

	public static final int FAST_SPEED_SHORT = 5;
	public static final int MEDIUM_SPEED_SHORT = 2;
	public static final int SLOW_SPEED_SHORT = 1;

	public static final int SHORT_MOVIE_LENGTH = 600;
	public static final int LONG_MOVIE_LENGTH = 3600;
}
