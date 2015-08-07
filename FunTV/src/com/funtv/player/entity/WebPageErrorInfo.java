package com.funtv.player.entity;

public class WebPageErrorInfo {
//	字段数	列名称	列代号	类型	列说明
//	1		protocol	字符串	该协议的输出版本号，每个协议不管任何一部分作变动，该版本号都要升级跟踪
//	2		rprotocol	字符串	日志请求协议版本号，由前端发送，表明前端发送的版本号
//	3		time	字符串	日志记录时间
//	4		ip	字符串	用户ip
	
//	---------------以上字段由服务器上报，下面字段客户端上报-----------------
//	5		dev	字符串	设备类型)：<aphone/apad/iphone/ipad/tv>_<操作系统>_<设备型号>
//	6		mac	字符串	长度为16的大写字符串
//	7		ver	字符串	app版本号，类ip地址的字符串
//	8		nt	字符串	网络类型：1—wifi，2--3g，3—其它，4——以太网
//	9		lt	字符串	加载时间：单位ms，从开始加载到侦测到失败的时长
//	10		code	字符串	失败代码：客户端取得的失败代码
//	11		desc	字符串	失败说明:客户端取得的失败说明信息
//	12		url	字符串	发生加载失败的页面url
//	13		fudid	字符串	长度为64位的数字与字母混排字符串，唯一标识设备
//	14		apptype	字符串	用于标识app的类型
	
	private long lt;

	private int code;
	
	private String desc;
	
	private String url;

	public long getLt() {
		return lt;
	}

	public void setLt(long lt) {
		this.lt = lt;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
