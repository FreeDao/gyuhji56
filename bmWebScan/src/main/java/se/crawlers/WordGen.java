package se.crawlers;

import java.io.IOException;

import com.bmtech.utils.io.LineWriter;

public class WordGen {
	static String words = "主办单位 活动时间 主讲嘉宾 活动地点 报名地址 活动安排 活动流程 活动信息 活动入场 圆桌论坛 圆桌会 圆桌讨论 活动主办 活动协办 八十沙龙 深圳站 安卓巴士 特邀嘉宾 主题演讲 知名投资人 圆桌论坛 活动结束 体验会  专业讲座 演讲主题 沙龙 创新沙龙 创业沙龙  活动详情 活动内容 嘉宾演讲 主讲嘉宾 创始人 联合创始人 活动咨询 限额 ihuodong 免费票 联合主办 本届论坛 投资对接 项目路演 路演活动专场活动 专场项目 项目路演 路演活动 投资对接 创业者项目 俱乐部 天使投资 天使基金 现场邀请 发起人 初创团队 股权分配 现场光临 举办地址 活动路演 活动议程 项目路演 对接交流 举办地址 活动发起人 活动宗旨 领袖峰会 技术团队 出席嘉宾 特邀嘉宾 主持人 主办方 报名参与 参与活动 主题分享 嘉宾互动 活动简介 活动环节 创业导师 主题沙龙 创业导师 投资机构 互动内容 创业服务机构 活动详情 创想会 创想汇 分享嘉宾 活动名称 活动时间 主办单位 承办单位 协办单位 活动详情 活动地点 活动内容 活动形式 项目代表 活动规模 活动费用 活动地点 主讲人 活动流程 具体日程 交流会 创业故事 创业团队 活动嘉宾 互动环节 创业热情 创业激情 嘉宾交流 演讲嘉宾 讲演嘉宾 代表致辞 创业沙龙 创业项目 报名方式 主办方 主题论坛 会议流程 参会人员 开放日 日程安排 报名方式 我要报名 免费票 签到入场 主题分享 圆桌座谈 嘉宾简介 活动流程 创业者专场 投资团队 主题演讲 主持人 论坛主题 联合创始人 大学生创业 圆桌论坛 创业大咖 邀请嘉宾 创业经验 项目路演 投资对接 现场邀请 创业节 参会人员 与会人员 投资机构 沙龙 发起人 创业 创业者 圆桌 费用 主题  专场 初创者 咖啡 签到 binggo 议程 报名 路演 峰会 车库咖啡 嘉宾";
	static String city = "上海 广州 深圳 天津 南京 武汉 沈阳 西安 成都 重庆 杭州 青岛 大连  宁波 济南 哈尔滨 长春 厦门 郑州  长沙 福州 乌鲁木齐  昆明  兰州  苏州  无锡 南昌  贵阳 南宁 合肥 太原 石家庄 呼和浩特 佛山 东莞   唐山  烟台  泉州  包头";

	public static void main(String[] args) throws IOException {
		String[] wordArr = words.replace('\t', ' ').replaceAll(" +", " ")
				.split(" ");
		String[] cityArr = city.replace('\t', ' ').replaceAll(" +", " ")
				.split(" ");
		LineWriter lw = new LineWriter("./wordsCity.txt", false);
		for (String x : wordArr) {
			lw.writeLine(x);
		}

		for (String x : wordArr) {
			for (String y : cityArr) {
				lw.writeLine(x + " " + y);
			}
		}
		lw.close();
	}
}
