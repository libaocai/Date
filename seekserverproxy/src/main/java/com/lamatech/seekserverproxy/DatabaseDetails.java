package com.lamatech.seekserverproxy;

import android.net.Uri;

/**
 * 此类里面记录了数据库的表结构信息，及访问表需要的URI 里面的每个内部类对应一张表，所有的值都定义为静态常量
 * <p/>
 * 这些东西还是在类中定义完比较好，在引用一次properties 感觉也没有解决混乱的问题
 * <p/>
 * <p/>
 */
public class DatabaseDetails {

	/**
	 * URI definitions
	 */
	public static final String AUTHORITY = "com.lamatech.seek.provider";

	/**
	 * The scheme part for this provider's URI
	 */
	private static final String SCHEME = "content://";

	/**
	 * The default sort order for this table
	 */
	public static final String DEFAULT_SORT_ORDER = "ID ASC";

	/**
	 * 0-relative position of a note ID segment in the path part of a note ID
	 * URI
	 */
	public static final int _ID_PATH_POSITION = 1;

	/**
	 * Seek表
	 */
	public static final class Seek {
		/**
		 * Path part for the Notes URI
		 */
		private static final String PATH_SEEK= "/Seek";

		/**
		 * Path part for the Note ID URI
		 */
		private static final String PATH_SEEK_ID = "/Seek/";

		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY
				+ PATH_SEEK);

		/**
		 * The content URI base for a single note. Callers must append a numeric
		 * note id to this Uri to retrieve a note
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME
				+ AUTHORITY + PATH_SEEK_ID);

		public static final String TABLE_NAME = "Seek";

		public static final String _ID = "id";

		public static final String COLUMN_NAME_DATE = "date";

		public static final String COLUMN_NAME_TIME = "time";

		// 留言的时间戳
		public static final String COLUMN_NAME_TIMESTAMP = "timestamp";

		// 留言标题，评论和赞为空
		public static final String COLUMN_NAME_TITLE = "title";

		// 信息内容，赞为空
		public static final String COLUMN_NAME_DETAIL = "detail";

		// 国家
		public static final String COLUMN_NAME_COUNTRY = "country";

		// 省
		public static final String COLUMN_NAME_PROVINCE = "province";

		// 市
		public static final String COLUMN_NAME_CITY = "city";

		// 区
		public static final String COLUMN_NAME_AREA = "area";

		// 百度地图地址
		public static final String COLUMN_NAME_ADDRESS = "address";

		// 自定义位置
		public static final String COLUMN_NAME_BUILDING = "building";

		// 纬度
		public static final String COLUMN_NAME_LNG = "lng";

		// 经度
		public static final String COLUMN_NAME_LAT = "lat";

		// 信息位置 国家
		public static final String COLUMN_NAME_MSG_COUNTRY = "msg_country";

		// 信息位置 省
		public static final String COLUMN_NAME_MSG_PROVINCE = "msg_province";

		// 信息位置 市
		public static final String COLUMN_NAME_MSG_CITY = "msg_city";

		// 信息位置 区
		public static final String COLUMN_NAME_MSG_DISTRICT = "msg_district";

		// 信息位置 百度地图地址
		public static final String COLUMN_NAME_MSG_ADDRESS = "msg_address";

		// 信息位置 自定义位置
		public static final String COLUMN_NAME_MSG_BUILDING = "msg_building";

		// 信息位置 纬度
		public static final String COLUMN_NAME_MSG_LNG = "msg_lng";

		// 信息位置 经度
		public static final String COLUMN_NAME_MSG_LAT = "msg_lat";

		// 信息所属的用户
		public static final String COLUMN_NAME_USER = "user";

		// 信息所属的用户的名字
		public static final String COLUMN_NAME_NAME = "name";

		// 信息所属的用户的头像
		public static final String COLUMN_NAME_HEADPIC = "headpic";

		// 父信息，新帖为空
		public static final String COLUMN_NAME_FATHER = "father";

		// 信息类型，新帖、赞、评论
		public static final String COLUMN_NAME_MODULE = "module";

		// 点赞数量
		public static final String COLUMN_NAME_COMMENTCOUNT = "commentcount";

		// 评论数量
		public static final String COLUMN_NAME_LIKECOUNT = "likecount";

		// 留言中图片
		public static final String COLUMN_NAME_PIC1 = "pic1";
		public static final String COLUMN_NAME_PIC2 = "pic2";
		public static final String COLUMN_NAME_PIC3 = "pic3";
		public static final String COLUMN_NAME_PIC4 = "pic4";
		// 留言中语音
		public static final String COLUMN_NAME_AUDIO = "audio";
		// 语音长度
		public static final String COLUMN_NAME_AUDIO_LENGTH = "audio_length";

		public static final String COLUMN_NAME_LAST_UPDATE_TIME = "last_update_time";

		// 隐私属性
		public static final String COLUMN_NAME_SECRET = "secret";

	}

	/**
	 * normal order info
	 */

	public static final class NormalOrder {
		public static final String COLUMN_NAME_PKID = "pkid";
		public static final String COLUMN_NAME_MID = "mid";
		public static final String COLUMN_NAME_TIME = "time";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_CONTENT = "content";
		public static final String COLUMN_NAME_TYPE = "type";
		public static final String COLUMN_NAME_PRICE = "price";
		public static final String COLUMN_NAME_RMID = "rmid";
		public static final String COLUMN_NAME_RTIME = "rtime";
		public static final String COLUMN_NAME_STATE = "state";
		public static final String COLUMN_NAME_LNG = "longitude";
		public static final String COLUMN_NAME_LAT = "latitude";
		public static final String COLUMN_NAME_LOCATION = "location";
		public static final String COLUMN_NAME_COUNT = "count";
		public static final String COLUMN_NAME_REASON = "reason";
		public static final String COLUMN_NAME_SERVICE_TIME = "stime";
		public static final String COLUMN_NAME_OUT_TRADE_NO = "out_trade_no";
		public static final String COLUMN_NAME_PAY_TYPE = "paytype";

	}

	/**
	 * promotion info
	 */

	public static final class PromotionOrder {
		public static final String COLUMN_NAME_PKID = "pkid";
		public static final String COLUMN_NAME_MID = "mid";
		public static final String COLUMN_NAME_TIME = "time";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_CONTENT = "content";
		public static final String COLUMN_NAME_COUNT = "count";
		public static final String COLUMN_NAME_MONEY = "money";
		public static final String COLUMN_NAME_CLICKRATIO = "clickratio";
		public static final String COLUMN_NAME_PIC = "pic";
		public static final String COLUMN_NAME_TRADE_NO = "paymentno";

	}
	/**
	 * Users表
	 */
	public static final class Users {
		/**
		 * Path part for the Notes URI
		 */
		private static final String PATH_Users= "/Users";

		/**
		 * Path part for the Note ID URI
		 */
		private static final String PATH_Users_ID = "/Users/";
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY
				+ PATH_Users);

		/**
		 * The content URI base for a single note. Callers must append a numeric
		 * note id to this Uri to retrieve a note
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME
				+ AUTHORITY + PATH_Users_ID);

		public static final String TABLE_NAME = "User";

		public static final String _ID = "id";

		public static final String COLUMN_NAME_ID = "username";

		public static final String COLUMN_NAME_PWD = "password";

		public static final String COLUMN_NAME_PKID = "pkid";

		public static final String COLUMN_NAME_NAME = "name";

		public static final String COLUMN_NAME_HEADPIC = "photo";

		// 生日
		public static final String COLUMN_NAME_BIRTHDAY = "birthday";

		// 性别
		public static final String COLUMN_NAME_GENDER = "sex";

		// 心情
		public static final String COLUMN_NAME_MOOD = "mood";

		public static final String COLUMN_NAME_EMAIL = "email";

		public static final String COLUMN_NAME_PHONE = "iphone";

		public static final String COLUMN_NAME_ACCOUNT = "account";

		public static final String COLUMN_NAME_LAT = "latitude";
		public static final String COLUMN_NAME_LNG = "longitude";

		public static final String COLUMN_NAME_LOCATION = "location";

		public static final String COLUMN_NAME_FLAG = "orderflag";

		public static final String COLUMN_NAME_WOPENID = "wopenid";

		public static final String COLUMN_NAME_SCORE = "score";

		// 家乡 省
		public static final String COLUMN_NAME_PROVINCE = "province";

		// 家乡 市
		public static final String COLUMN_NAME_CITY = "city";

		// 性格颜色
		public static final String COLUMN_NAME_COLOR = "color";

		// 星座
		public static final String COLUMN_NAME_CONSTELLATION = "constellation";

		// 血型
		public static final String COLUMN_NAME_BLOODTYPE = "bloodtype";

		// 上次签到时间
		public static final String COLUMN_NAME_SIGNTIME = "signtime";

		
		// 徽章字段
		public static final String COLUMN_NAME_BADGE_BUTTIM_STRING = "badgebuttomstring";
		// 与我相关小红点状态值
		public static final String COLUMN_NAME_RED_POINT_STATE = "redpointstate";
		// 等级
		public static final String COLUMN_NAME_LEVEL = "level";
		// 今天经验值
		public static final String COLUMN_NAME_TODAY_EXPERIENCE = "todayexperience";
		// 经验值
		public static final String COLUMN_NAME_EXPERIENCE = "experience";
		//发布留言的数量
		public static final String COLUMN_NAME_BOARDCOUNT = "boardcount";
		//每日有效发布留言数量
		public static final String COLUMN_NAME_DAYBOAEDCOUNT = "dayboardcount";
		//浏览地点数量
		public static final String COLUMN_NAME_BROWSECOUNT = "browsecount";
		//评论数量
		public static final String COLUMN_NAME_DISCUSSCOUNT = "discusscount";
		//被评论数量
		public static final String COLUMN_NAME_DISCUSSEDCOUNT = "discussedcount";
		//每日有效评论数量
		public static final String COLUMN_NAME_DAYDISCUSSCOUNT = "daydiscusscount";
		//每日有效评论数量
		public static final String COLUMN_NAME_DAYSHARECOUNT = "daysharecount";
		// 设备标识  （友盟需要）
		public static final String COLUMN_NAME_DEVICE_TOKEN = "devicetokens";
		
		// 留言隐秘属性
		public static final String COLUMN_NAME_MESSAGE_SECRET = "message_secret";
		// 评论隐秘属性
		public static final String COLUMN_NAME_COMMENT_SECRET = "comment_secret";
		// 有新消息
		public static final String COLUMN_NAME_NEW_MESSAGE = "new_message";
	}

	/**
	 * Locations表
	 */
	public static final class Locations {
		/**
		 * Path part for the Notes URI
		 */
		private static final String PATH_Locations= "/Locations";

		/**
		 * Path part for the Note ID URI
		 */
		private static final String PATH_Locations_ID = "/Locations/";
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY
				+ PATH_Locations);

		/**
		 * The content URI base for a single note. Callers must append a numeric
		 * note id to this Uri to retrieve a note
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME
				+ AUTHORITY + PATH_Locations_ID);

		public static final String TABLE_NAME = "Location";

		public static final String _ID = "id";

		// 家乡 省
		public static final String COLUMN_NAME_PROVINCE = "province";

		// 家乡 市
		public static final String COLUMN_NAME_CITY = "city";

		// 区
		public static final String COLUMN_NAME_DISTRICT = "district";

		// 百度地图地址
		public static final String COLUMN_NAME_ADDRESS = "address";

		// 位置
		public static final String COLUMN_NAME_BUILDING = "building";

		// 自定义位置
		public static final String COLUMN_NAME_CUSTOM_BUILDING = "custom_building";

		// 纬度
		public static final String COLUMN_NAME_LNG = "lng";

		// 经度
		public static final String COLUMN_NAME_LAT = "lat";

	}

	/**
	 * Locations表
	 */
	public static final class History {
		/**
		 * Path part for the Notes URI
		 */
		private static final String PATH_History= "/History";

		/**
		 * Path part for the Note ID URI
		 */
		private static final String PATH_History_ID = "/History/";
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY
				+ PATH_History);

		/**
		 * The content URI base for a single note. Callers must append a numeric
		 * note id to this Uri to retrieve a note
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME
				+ AUTHORITY + PATH_History_ID);

		public static final String TABLE_NAME = "History";
		public static final String _ID = "id";

		public static final String COLUMN_NAME_DATE = "date";

		public static final String COLUMN_NAME_TIME = "time";

		// 留言的时间戳
		public static final String COLUMN_NAME_TIMESTAMP = "timestamp";

		// 留言标题，评论和赞为空
		public static final String COLUMN_NAME_TITLE = "title";

		// 信息内容，赞为空
		public static final String COLUMN_NAME_DETAIL = "detail";

		// 国家
		public static final String COLUMN_NAME_COUNTRY = "country";

		// 省
		public static final String COLUMN_NAME_PROVINCE = "province";

		// 市
		public static final String COLUMN_NAME_CITY = "city";

		// 区
		public static final String COLUMN_NAME_AREA = "area";

		// 百度地图地址
		public static final String COLUMN_NAME_ADDRESS = "address";

		// 自定义位置
		public static final String COLUMN_NAME_BUILDING = "building";

		// 纬度
		public static final String COLUMN_NAME_LNG = "lng";

		// 经度
		public static final String COLUMN_NAME_LAT = "lat";

		// 信息位置 国家
		public static final String COLUMN_NAME_MSG_COUNTRY = "msg_country";

		// 信息位置 省
		public static final String COLUMN_NAME_MSG_PROVINCE = "msg_province";

		// 信息位置 市
		public static final String COLUMN_NAME_MSG_CITY = "msg_city";

		// 信息位置 区
		public static final String COLUMN_NAME_MSG_DISTRICT = "msg_district";

		// 信息位置 百度地图地址
		public static final String COLUMN_NAME_MSG_ADDRESS = "msg_address";

		// 信息位置 自定义位置
		public static final String COLUMN_NAME_MSG_BUILDING = "msg_building";

		// 信息位置 纬度
		public static final String COLUMN_NAME_MSG_LNG = "msg_lng";

		// 信息位置 经度
		public static final String COLUMN_NAME_MSG_LAT = "msg_lat";

		// 信息所属的用户
		public static final String COLUMN_NAME_USER = "user";

		// 信息所属的用户的名字
		public static final String COLUMN_NAME_NAME = "name";

		// 信息所属的用户的头像
		public static final String COLUMN_NAME_HEADPIC = "headpic";

		// 父信息，新帖为空
		public static final String COLUMN_NAME_FATHER = "father";

		// 信息类型，新帖、赞、评论
		public static final String COLUMN_NAME_MODULE = "module";

		// 点赞数量
		public static final String COLUMN_NAME_COMMENTCOUNT = "commentcount";

		// 评论数量
		public static final String COLUMN_NAME_LIKECOUNT = "likecount";

		// 留言中图片
		public static final String COLUMN_NAME_PIC1 = "pic1";
		public static final String COLUMN_NAME_PIC2 = "pic2";
		public static final String COLUMN_NAME_PIC3 = "pic3";
		public static final String COLUMN_NAME_PIC4 = "pic4";
		// 留言中语音
		public static final String COLUMN_NAME_AUDIO = "audio";
		// 语音长度
		public static final String COLUMN_NAME_AUDIO_LENGTH = "audio_length";

		public static final String COLUMN_NAME_LAST_UPDATE_TIME = "last_update_time";
		public static final String COLUMN_NAME_READ_TIME = "read_time";
	}
	public static class MODUL {
		public static final int MAIN = 0;
		public static final int LIKE = 1;
		public static final int COMMENT = 2;
	}

}
