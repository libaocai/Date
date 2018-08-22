package com.lamatech.seekserverproxy;

public class HttpConstants {

	public final static String HTTP_HEAD = "http://";

	public final static String HOST_PORT = "8080";
	// IP
	// public final static String HTTP_IP = "192.168.10.9:" + HOST_PORT;

	// public final static String HTTP_IP = "192.168.10.8";
	// 域名
	public final static String HTTP_IP = "meiliyue.caapa.org/index.php";

	public final static String HTTP_IP_NOINDEX = "meiliyue.caapa.org";
	

	public final static String HTTP_CONTEXT = "";

	public final static String HTTP_REQUEST = HTTP_HEAD + HTTP_IP
			+ HTTP_CONTEXT;

	public static  final int REQUEST_TIMEOUT = 1 * 1000;// 设置请求超时10秒钟 
    public static final int SO_TIMEOUT = 5 * 1000; // 设置等待数据超时时间10秒钟 
    public static final int type=-1;//请求Type

	public static String INTERVIEW = "/mobile/invite/index";
	public static String MOVEMENT = "/mobile/dynamics/index";
	public static String MINE = "/mobile/users/index";

	/**
	 * 用户协议
	 */
	public static String HTTP_AGREEMENT = HTTP_REQUEST + "agreement.jsp";

	/**
	 * 用户注册
	 */
	public static String HTTP_REGISTER = HTTP_REQUEST + "register";

	/**
	 * 用户密码找回
	 */
	public static String HTTP_FORGET_PWD = HTTP_REQUEST + "updatepwd";

	/**
	 * 用户密码修改
	 */
	public static String HTTP_CHANGE_PWD = HTTP_REQUEST + "updateoldpwd";

	/**
	 * 用户登录
	 */
	public static String HTTP_LOGIN = HTTP_REQUEST + "login";

	/**
	 * WX用户登录
	 */
	public static String HTTP_WLOGIN = HTTP_REQUEST + "wlogin";

	/**
	 * 修改用户信息
	 */
	public static String HTTP_UPDATE_USERINFO = HTTP_REQUEST + "setmember";

	/**
	 * 更新接单状态
	 */
	public static String HTTP_UPDATE_ORDER_FLAG = HTTP_REQUEST + "updateorderflag";
	
	/**
	 * 彻底删除用户
	 */
	public static String HTTP_DELETE_USER = HTTP_REQUEST + "deleteUserInfo";

	/**
	 * 获取用户信息
	 */
	public static String HTTP_GET_USERINFO = HTTP_REQUEST + "getmember";


	/**
	 * 获取历史订单
	 */
	public static String HTTP_GET_HISTORY_ORDERS = HTTP_REQUEST + "mynormallist";


	/**
	 * 获取未接订单
	 */
	public static String HTTP_GET_ORDERS = HTTP_REQUEST + "normallist";


	/**
	 * 接单操作
	 */
	public static String HTTP_GET_ORDER = HTTP_REQUEST + "normalreceive";

	/**
	 * 获取订单详情
	 */
	public static String HTTP_GET_ORDER_DETAIL = HTTP_REQUEST + "orderdetail";


	/**
	 * 发红包
	 */
	public static String HTTP_SEND_LUCKY_MONEY = HTTP_REQUEST + "promotionsend";

	/**
	 * 红包列表
	 */
	public static String HTTP_GET_LUCKY_MONEY_LIST = HTTP_REQUEST + "promotionlist";

	/**
	 * 红包详情
	 */
	public static String HTTP_GET_LUCKY_MONEY_DETAIL = HTTP_REQUEST + "promotiondetail";

	/**
	 * 红包领取
	 */
	public static String HTTP_GET_LUCKY_MONEY = HTTP_REQUEST + "promotionreceive";

	/**
	 * 提交建议
	 */
	public static String HTTP_SUMBIT_FEEDBACK = HTTP_REQUEST
			+ "feedback/add.do";

	public static String HTTP_HEAD_PIC = "http://meiliyue.caapa.org/";
	/**
	 * 发布信息
	 */
	public static String HTTP_SEND_MESSAGE = HTTP_REQUEST + "normalsend";

	/**
	 * 请求服务器进行推送
	 */
	public static String HTTP_PUSH_LET = HTTP_REQUEST + "pushlet";

	/**
	 * 接单人订单完成
	 *
	 */
	public static String HTTP_ORDER_OK = HTTP_REQUEST + "overreceive";

	/**
	 * 发单人订单完成
	 *
	 */
	public static String HTTP_ORDER_BEFORE_PAY = HTTP_REQUEST + "beforepay";

	/**
	 * 完成支付
	 */
	public static String HTTP_ORDER_PAY_OK = HTTP_REQUEST + "overpay";

	/**
	 * 更新wopenid
	 */
	public static String HTTP_SET_WOPENID = HTTP_REQUEST + "setwopenid";

	/**
	 * 更新红包金额
	 */
	public static String HTTP_UPDATE_LUCKY_MONEY = HTTP_REQUEST + "updatemoney";

	/**
	 * 甲方取消订单
	 */
	public static String HTTP_CANCEL_ORDER = HTTP_REQUEST + "normalcancel";

	/**
	 * 乙方取消订单
	 */
	public static String HTTP_RECEIVER_CANCEL_ORDER = HTTP_REQUEST + "rnormalcancel";

	/**
	 * 议价接口
	 */
	public static String HTTP_CHANGE_ORDER_PRICE = HTTP_REQUEST + "priceupdate";

	/**
	 * 对接单人服务进行评价
	 */
	public static String HTTP_SET_SCORE = HTTP_REQUEST + "membereva";

	/**
	 * 申请退款
	 */
	public static String HTTP_REFUND = HTTP_REQUEST + "refund";

	/**
	 * 微信登录绑定手机
	 */
	public static String HTTP_BIND_PHONE = HTTP_REQUEST + "bindphone";

	/**
	 * 提现
	 */
	public static String HTTP_GET_CASH = HTTP_REQUEST + "getcash";

	/**
	 * 申请余额退款
	 */
	public static String HTTP_ACCOUNT_REFUND = HTTP_REQUEST + "yuerefund";

	/**
	 * 获取信息
	 */
	public static String HTTP_GET_MESSAGES = HTTP_REQUEST + "getMessages";

	/**
	 * 支付密码获取
	 */
	public static String HTTP_GET_PAY_PWD = HTTP_REQUEST + "findpaypw";

	/**
	 * 支付密码设置
	 */
	public static String HTTP_SET_PAY_PWD = HTTP_REQUEST + "addpaypw";


	/**
	 * 请求优惠券
	 */
	public static String HTTP_GET_COUPON = HTTP_REQUEST + "couponreceive";

	/**
	 * 获取优惠券列表
	 */
	public static String HTTP_GET_COUPONS_LIST = HTTP_REQUEST + "mycouponlist";

	/**
	 * 获取可用优惠券数量
	 */
	public static String HTTP_GET_COUPONS_COUNT = HTTP_REQUEST + "couponused";

	/**
	 * 上传头像文件
	 */
	public static String HTTP_UPLOAD_PIC_FILE =  HTTP_HEAD + HTTP_IP + "/Api/user/uploadFile";

    /**
     * 上传文件
     */
    public static String HTTP_UPLOAD_FILE = HTTP_HEAD + HTTP_IP + "/Api/Common/uploadFile";
	
	/**
	 * 上传留言中图片文件和语音
	 */
	public static String HTTP_UPLOAD_MESSAGE_FILE = HTTP_REQUEST + "uploadMessageRecourse";
	
	/**
	 * 获取文件
	 */
	public static String HTTP_DOWNLOWN_FILE = HTTP_HEAD + HTTP_IP + "/LamaSeek/";
	
	/**
	 * 获取message中的图片文件或语音文件
	 */
	public static String HTTP_DOWNLOWN_MESSAGE_FILE = HTTP_DOWNLOWN_FILE + "message/";
	
	/**
	 * 上传新留言地址
	 */
	public static String HTTP_ADD_LOCATION = HTTP_REQUEST + "addLocation";
	
	/**
	 * 获取服务地址
	 */
	public static String HTTP_GET_LOCATIONS = HTTP_REQUEST + "getLocations";


	/**
	 * 获取服务类型和地址
	 */
	public static String HTTP_GET_LOCATIONS_AND_SERVICE_TYPES = HTTP_REQUEST + "getmessage";

	/**
	 * 根据经纬度获取留言
	 */
	public static String HTTP_GET_MESSAGES_BY_LL = HTTP_REQUEST + "getMessagesByll";
	
	/**
	 * 增加收藏
	 */
	public static String HTTP_ADD_COLLECTION = HTTP_REQUEST + "addCollection";
	
	/**
	 * 获取收藏
	 */
	public static String HTTP_GET_COLLECTIONS = HTTP_REQUEST + "getCollections";
	
	/**
	 * 取消收藏
	 */
	public static String HTTP_DEL_COLLECTIONS = HTTP_REQUEST + "deleteCollection";

	/**
	 * 超时通知
	 */
	public static String HTTP_TIMEOUT = HTTP_REQUEST + "timeout";

	
	/**
	 * 反馈信息
	 */
	public static String HTTP_ADD_FEEDBACK = HTTP_REQUEST + "addFeedback";
	
	/**
	 * 获取反馈信息
	 */
	public static String HTTP_GET_FEEDBACKSS = HTTP_REQUEST + "getFeedbacks";
	
	/**
	 * 根据经纬度获取留言（分页）
	 */
	public static String HTTP_GET_MESSAGES_PAGE_BY_LL = HTTP_REQUEST + "getIndexMessagesByll";

	/**
	 * 根据经纬度获取留言（分页）2
	 */
	public static String HTTP_GET_MESSAGES_PAGE_BY_LL2 = HTTP_REQUEST + "getIndexMessagesByll2";
	
	
	/**
	 * 删除留言
	 */
	public static String HTTP_DEL_MESSAGES = HTTP_REQUEST + "deleteMessage";
	
	/**
	 * 修改密码
	 */
	public static String HTTP_UPDATE_PWD = HTTP_REQUEST + "updatePassword";
	
	/**
	 * 获取本用户所有赞或评论
	 */
	public static String HTTP_GET_LIST_BY_MODULE = HTTP_REQUEST + "getMessagesByUserAndMod";
	
	/**
	 * 获取超级用户列表
	 */
	public static String HTTP_GET_ADMIN_USER = HTTP_REQUEST + "getAdminUser";
	
	/**
	 * 获取最新版本信息
	 */
	public static String HTTP_GET_NEW_VERSION_INFO =HTTP_HEAD + HTTP_IP + "/LamaSeek/version/version.txt";

	/**
	 * 获取聚合版本
	 */
	public static String HTTP_GET_JUHE_VERSION_INFO = HTTP_REQUEST + "upload/version/juhe.json";
}
