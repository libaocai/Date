package com.lamatech.seekserverproxy;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by libaocai on 11/03/16.
 */
public class ServerProxy {

	private static Context mContext;
	private static ServerProxy mServerProxy;

	private ServerProxy() {

	}

	/**
	 * 存储请求服务器返回结果
	 * 
	 * @author libaocai
	 *
	 */
	class Result {
		String result;
		boolean isGet;
	}

	public static ServerProxy getServerProxyInstance(Context context) {
		mContext = context;
		if (mServerProxy == null) {
			mServerProxy = new ServerProxy();
		}

		return mServerProxy;
	}


	public static final String DEF_CHATSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";


	/**
	 * 账号登录
	 * @param userid
	 * @param pwd
	 * @param device_token
	 * @return
	 */
	public static String login(String userid, String pwd, String device_token) {
		HashMap map = new HashMap();
		map.put("mobile", userid);
		map.put("password", pwd);
		//map.put("devicetokens", device_token);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/Auth/login", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "login error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	public static String checkRegister(String mobile) {
        String result;
        HashMap map = new HashMap();
        map.put("mobile", mobile);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "/Api/Auth/isRegister", map, "POST");
        } catch (Exception e) {
            Log.e("seek", "isRegister error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }

        return result;
    }

	public static String getLocationsJson() {
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/region/getJson", null, "POST");
		} catch (Exception e) {
			Log.e("seek", "getjson error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 *  获取飞机数量
	 * @param user_id
	 * @return
	 */
	public static String getPlaneCount(String user_id) {
		String result;
		HashMap map = new HashMap();
		map.put("user_id", user_id);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/Index/rocketNum", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "get plane count  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}


	/**
	 * 第三方登录
	 * @param map
	 * @return
	 */
	public static String thirdLogin(HashMap map){
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/Auth/third_login", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "/Api/Auth/third_login error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 第三方检测是否注册
	 * @param map
	 * @return
	 */
	public static String thirdIsRegister(HashMap map){
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/Auth/thirdIsRegister", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "/Api/Auth/thirdIsRegister error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}


	/**
	 * 账号注册
	 * @param map
     *
    map.put("mobile", mobile);
    map.put("password", pwd);
    map.put("code", code);
    map.put("nickname", nickname);
    map.put("sex", sex);
    map.put("birthday", birthday_day);
    map.put("country", country);
    map.put("province", province_code);
    map.put("city", city_code);
    map.put("qq", qq_number);
	 * @return
	 */
	public static String register(HashMap map) {
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/Auth/register", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "register error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	public static String thirdRegister(HashMap map) {
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/Auth/third_register", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "third_register error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}


	/**
	 * 请求发送手机验证码
	 * @param phone
	 * @return
	 */
	public static String getPhoneCode(String phone) {
		HashMap map = new HashMap();
		map.put("mobile", phone);
		map.put("sence", 1);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/Auth/sendMobileCode", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "get phone code error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取融云token
	 * @param user_id
	 * @return
	 */
	public static String getToken(String user_id) {
		HashMap map = new HashMap();
		map.put("user_id", user_id);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/Rongyun/getToken", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "get phone code error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 查找uuid
	 * @param user_id
	 * @return
	 */
	public static String searchFriend(String user_id, String uuid) {
		HashMap map = new HashMap();
		map.put("user_id", user_id);
		map.put("uuid", uuid);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/user/searchFriend", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "seach friend code error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 验证手机验证码
	 * @param phone
	 * @param code
	 * @return
	 */
	public static String checkPhoneCode(String phone, String code) {
		HashMap map = new HashMap();
		map.put("mobile", phone);
		map.put("code", code);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/Auth/checkMobileCode", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "check phone code error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

    /**
     * 获取在线朋友
     * @param map
     * @return
     */

	public static String getOnlineFriends(HashMap map) {
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "/Api/Index/index", map, "POST");
        } catch (Exception e) {
            Log.e("seek", "get on line friends error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }

        return result;
    }


	/**
	 * 请求来访者
	 * @param user_id
	 * @return
	 */
	public static String getVisitors(String user_id, String type, String page) {
		HashMap map = new HashMap();
		map.put("user_id", user_id);
		map.put("type", type);
		map.put("page", page);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/Auth/sendMobileCode", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "get visitors  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取用户信息
	 * @param user_id
	 * @return
	 */
	public static String getUserInfo(String user_id) {
		HashMap map = new HashMap();
		map.put("user_id", user_id);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/user/getUserInfo", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "get userinfo  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取消息列表
	 * @param user_id
	 * @return
	 */
	public static String getMessages(String user_id,  String page) {
		HashMap map = new HashMap();
		map.put("user_id", user_id);
		map.put("page", page);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/User/message", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "get messages  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取帮助列表
	 * @return
	 */
	public static String getHelp() {
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/User/questions", null, "POST");
		} catch (Exception e) {
			Log.e("seek", "get help list  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}
	/**
	 * 置顶操作
	 * @param user_id
	 * @return
	 */
	public static String setOnTop(String user_id) {
		HashMap map = new HashMap();
		map.put("user_id", user_id);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/Index/setTop", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "set on top  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 更新用户地址
	 * @param map
	 * @return
	 */

	public static String updateLocation(HashMap map) {
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/User/changeLocation", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "update location error error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 更改用户等级
	 * @return
	 */

	public static String changeLevel(String user_id, String level) {
		HashMap map = new HashMap();
		map.put("user_id", user_id);
		map.put("level", level);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/Vip/change", map, "POST");
		} catch (Exception e) {
			Log.e("seek", "change level error error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}


	/**
	 * 获取服务信息
	 * @param id  1：服务条款，2：使用须知，3：关于我们
	 * @return
	 */

	public static String getsfile(String id) {
		HashMap map = new HashMap();
		map.put("id", id);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getsfile", map, "GET");
		} catch (Exception e) {
			Log.e("seek", "get  file  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}


	/**
	 *  自动匹配订单设置
	 * @param mid 用户id
	 * @param automatchflag 自动配单设置 1自动匹配，0不自动匹配 "" 查询配置
	 * @return
	 */
	public static String saveautomatch(String mid, String automatchflag) {
		HashMap map = new HashMap();
		map.put("mid", mid);
		map.put("automatchflag", automatchflag);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "saveautomatch", map, "GET");
		} catch (Exception e) {
			Log.e("seek", "saveautomatch error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}


	/**
	 * 符合意愿的订单
	 * @param mid
	 * @return
	 */
	public static String wishorderlist(String mid) {
		HashMap map = new HashMap();
		map.put("mid", mid);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "wishorderlist", map, "GET");
		} catch (Exception e) {
			Log.e("seek", "wishorderlist  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 符合接单的人员名单
	 * @param mid
	 * @return
	 */
	public static String wishreceivelist(String orderid, String mid) {
		HashMap map = new HashMap();
		map.put("mid", mid);
		map.put("orderid", orderid);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "wishreceivelist", map, "GET");
		} catch (Exception e) {
			Log.e("seek", "wishreceivelist  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 *  设置接单意愿
	 * @param mid
	 * @param wishtype
	 * @param wishaddress
	 * @param wishlatitude
	 * @param wishlongitude
	 * @return
	 */
	public static String setwishtype(String mid, String wishtype, String wishaddress, String wishlatitude, String wishlongitude) {
		HashMap map = new HashMap();
		map.put("mid", mid);
		map.put("wishtype", wishtype);
		map.put("wishlatitude", wishlatitude);
		map.put("wishlongitude", wishlongitude);
		map.put("wishaddress", wishaddress);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "setwishtype", map, "GET");
		} catch (Exception e) {
			Log.e("seek", "setwishtype  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取最新订单
	 * @param pkid
	 * @return
	 */
	public static String getCurrentOrder(String pkid) {
		HashMap get_map = new HashMap<String, Object>();
		get_map.put("mid", pkid);

		String result = "";
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getnewsendorder", get_map, "GET");
		} catch (Exception e) {
			Log.e("seek", "get current order error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		Log.e("seek", "the  current order is " + result);

		List<Map<String,Object>> data = new ArrayList<>();
		if(result == null) {
			return null;
		}

		return result;
	}


	/**
	 * 获取全部未被抢的订单列表
	 * @param filed
	 * @param pkid
	 * @return
	 */
	public static String getOrders(String filed, String pkid) {
		HashMap get_map = new HashMap<String, Object>();
		get_map.put("filed", filed);

		String result = "";
		try {
			result = net(HttpConstants.HTTP_REQUEST, "orderlist", get_map, "GET");
		} catch (Exception e) {
			Log.e("seek", "get orders error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		Log.e("seek", "the  orders is " + result);

		List<Map<String,Object>> data = new ArrayList<>();
		if(result == null) {
			return null;
		}

		return result;
	}

	/**
	 * 获取历史订单列表
	 * @param pkid
	 * @param inforflag 0是接   1是发
	 * @return
	 */
	public static String getHistoryOrders(String pkid, String inforflag) {
		HashMap map = new HashMap();
		map.put("mid", pkid);
		map.put("inforflag", inforflag);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "myhistorylist", map, "GET");
			Log.e("seek", "the hisrory orders is " + result);
			JSONObject resJson = new JSONObject(result);
			return resJson.getString("normallist");
		} catch (Exception e) {
			Log.e("seek", "get history orders error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		Log.e("seek", "the hisrory orders is " + result);

		return result;
	}


	/**
	 * 发单
	 * @param map
	mid:发单人mid
	message_type:信息类型
	message_time1:信息时间1
	use_address1:可用地区1
	aid1:信息地点1
	content:信息内容
	use_address2:用地区2
	message_time2:信息时间2
	aid2:信息地点2
	commission:佣金


	filed:发单区域
	flagkey:用户标识


	couponprice：使用优惠券的金额
	surplusprice：除去优惠券金额，应付的实际金额
	 * @return
	 */
	public static String sendNormalOrder(Map map) {
		try {
			return net(HttpConstants.HTTP_REQUEST, "sendorder", map, "GET");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "{\"code\":\"-11\",\"msg\":\"失败\"}";
	}

    /**
     * 食堂发单
     *
     * @param map
     *
    srid：食堂id
    srname：食堂名称
    content：内容
    sendcount：数量
    validitydate：有效期
    price：单价
    image1：图片1
    image2：图片2
    image3：图片3
     *
     * @return
     */
    public static String sendMessHallOrder(Map map) {
        try {
            return net(HttpConstants.HTTP_REQUEST, "sendrestaurantorder", map, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"code\":\"-11\",\"msg\":\"失败\"}";
    }

    /**
     * 获取订餐列表
     * @return
     */
    public static String getBookDinnerList() {
        try {
            return net(HttpConstants.HTTP_REQUEST, "restaurantSendlist_re", null, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"code\":\"-11\",\"msg\":\"失败\"}";

    }

    /**
     * 获取已订餐列表
     * @return
     */
    public static String getBookedDinnerList(String pkid) {
        HashMap map = new HashMap();
        map.put("id", pkid);
        try {
            return net(HttpConstants.HTTP_REQUEST, "restaurantrelist", map, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"code\":\"-11\",\"msg\":\"失败\"}";

    }


    /**
     * 获取食堂发单列表
     * @return
     */
    public static String getMessHallList(String pkid) {
        HashMap map = new HashMap();
        map.put("id", pkid);
        try {
            return net(HttpConstants.HTTP_REQUEST, "restaurantsendlist", map, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"code\":\"-11\",\"msg\":\"失败\"}";

    }

    /**
     * 获取食堂每单的接单列表
     * @return
     */
    public static String getDinnerOrderList(String id) {
        HashMap map = new HashMap();
        map.put("id", id);
        try {
            return net(HttpConstants.HTTP_REQUEST, "restaurantorderlist", map, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"code\":\"-11\",\"msg\":\"失败\"}";

    }
    /**
     * 提交订餐
     srid 餐厅id
     sroid 订单id
     rmid 接单人
     iphone 电话
     address 地址
     count 份数
     * @param map
     * @return
     */
    public static String receiverestaurant(HashMap map) {
        try {
            return net(HttpConstants.HTTP_REQUEST, "receiverestaurant", map, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"code\":\"-11\",\"msg\":\"失败\"}";

    }
    /**
     * 获取食堂订单详情
     * @param id
     * @return
     */
    public static String getMessHallOrderDetail(String id) {
        HashMap map = new HashMap();
        map.put("sroid", id);
        try {
            return net(HttpConstants.HTTP_REQUEST, "restaurantorderdetail", map, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"code\":\"-11\",\"msg\":\"失败\"}";
    }

    /**
     * 获取订餐订单详情
     * @param id
     * @return
     */
    public static String getDinnerOrderDetail(String id) {
        HashMap map = new HashMap();
        map.put("id", id);
        try {
            return net(HttpConstants.HTTP_REQUEST, "restaurantredetail", map, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"code\":\"-11\",\"msg\":\"失败\"}";
    }


    /**
     * 食堂申请退款
     * @param id
     * @return
     */
    public static String restaurantrefund(String id) {
        HashMap map = new HashMap();
        map.put("id", id);
        try {
            return net(HttpConstants.HTTP_REQUEST, "restaurantrefund", map, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"code\":\"-11\",\"msg\":\"失败\"}";
    }

    /**
     * 充值成功
     * @param id
     * @return
     */
    public static String restauranttopup(String id, String paymentno) {
        HashMap map = new HashMap();
        map.put("id", id);
        map.put("out_trade_no", paymentno);
        try {
            return net(HttpConstants.HTTP_REQUEST, "restauranttopup", map, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"code\":\"-11\",\"msg\":\"失败\"}";
    }

    /**
     * 订餐送达
     * @param id
     * @return
     */
    public static String bookedDinnerDone(String id) {
        HashMap map = new HashMap();
        map.put("id", id);
        try {
            return net(HttpConstants.HTTP_REQUEST, "restaurantorderdone", map, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"code\":\"-11\",\"msg\":\"失败\"}";
    }

    /**
     * 食堂送达
     * @param id
     * @return
     */
    public static String messHallOrderDone(String id) {
        HashMap map = new HashMap();
        map.put("id", id);
        try {
            return net(HttpConstants.HTTP_REQUEST, "restaurantorderdelivery", map, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"code\":\"-11\",\"msg\":\"失败\"}";
    }

    /**
	 * 给朋友发单
	 * @param map
	 * @return
	 */
	public static String sendFriendOrder(Map map) {
		try {
			return net(HttpConstants.HTTP_REQUEST, "sendorderfriend", map, "GET");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "{\"code\":\"-11\",\"msg\":\"失败\"}";
	}

	/**
	 * 发单者取消订单
	 * @param pkid
	 * @param reason
	 * @return
	 */
	public static String cancelOrder(String pkid, String reason) {
		String result;
		HashMap map = new HashMap();
		map.put("iid", pkid);
		map.put("reason", reason);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "cancelorder", map, "GET");
		} catch (Exception e) {
			Log.e("seek", "cancel order  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 接单者退单
	 * @param pkid
	 * @param rmid
	 * @return
	 */
	public static String rcancelOrder(String pkid, String rmid, String musername, String reason) {
		String result;
		HashMap map = new HashMap();
		map.put("iid", pkid);
		map.put("rmid", rmid);
		map.put("musername", musername);
		map.put("reason", reason);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "rcancelorder", map, "GET");
		} catch (Exception e) {
			Log.e("seek", "cancel order  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 接单操作
	 * @param pkid
	 * @param mid
	 * @return
	 */
	public static String getOrder(String pkid, String mid) {
		String result;
		HashMap<String, Object> map = new HashMap<>();
		map.put("pkid", pkid);
		map.put("mid", mid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getorder", map, "GET");
		} catch (Exception e) {
			Log.e("seek", "get order error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 系统匹配接单
	 * @param pkid
	 * @param mid
	 * @return
	 */
	public static String getOrderMatch(String pkid, String mid) {
		String result;
		HashMap<String, Object> map = new HashMap<>();
		map.put("pkid", pkid);
		map.put("mid", mid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getordermatch", map, "GET");
		} catch (Exception e) {
			Log.e("seek", "get order match error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;
	}


	/**
     *
     * @param map
    pkid：用户id
    title：标题
    content：内容
    count：红包金额
    money：单个红包金额
     * @return
     */
    public static String sendLuckyMoney(Map<String, Object> map) {

        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "sendluckymoney", map, "GET");
        } catch (Exception e) {
            Log.e("seek", "sendLuckyMoney error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "send lucky money is " + result);

        return result;
    }


    /**
     * 抢红包
     * @param map
     * piid：信息id
     * mid：领取人id
     * @return
     */
    public static String getLuckyMoney(Map<String, Object> map) {

        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "getluckymoney", map, "GET");
        } catch (Exception e) {
            Log.e("seek", "get lucky money error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "get lucky money is " + result);

        return result;
    }


    /**
     * 完善更新用户信息
     * @param pkid
     * @param map
    photo：照片
    name：姓名
    iphone：电话
    sex：行呗
    email：电邮
    mood：心情
     * @return
     */
    public static String updateUserInfo(String pkid, Map<String, Object> map) {
        String result;
        map.put("pkid", pkid);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "updateuserinfo", map, "GET");
        } catch (Exception e) {
            Log.e("seek", "update userinf erro error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取用户信息
     * @param pkid
     * @return
     */
    public static Map<String, Object>  getUserInfoGet(String pkid) {
        HashMap map = new HashMap();
        map.put("mid", pkid);
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "getuserinfo", map, "GET");
        } catch (Exception e) {
            Log.e("seek", "get user info error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }

        Log.e("seek", "get user info , result is  " + result);

        /*
        {"sex":"","score":"0","rcount":0,"code":"0","iphone":"13041009481",
        "msg":"成功","photo":"","password":"D8578EDF8458CE06FBC5BB76A58C5CA4",
        "wopenid":"","idVerification":"0","username":"13041009481","orderflag":"1",
        "name":"13041009481","account":"0.0","longitude":"","latitude":""}
         */
        HashMap<String, Object> resultMap = new HashMap<>();
		if(result == null) {
			return null;
		}
        try {
            JSONObject resultObject = new JSONObject(result);
            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_ID, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_ID));
            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_ACCOUNT, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_ACCOUNT));
            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_NAME, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_NAME));
            if(resultObject.get(DatabaseDetails.Users.COLUMN_NAME_NAME).equals("")) {
				resultMap.put(DatabaseDetails.Users.COLUMN_NAME_NAME, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_ID));
			}
            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_HEADPIC, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_HEADPIC));
            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_FLAG, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_FLAG));
            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_GENDER, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_GENDER));
//            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_EMAIL, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_EMAIL));
            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_PHONE, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_PHONE));
            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_LAT, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_LAT));
            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_LNG, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_LNG));
//            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_LOCATION, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_LOCATION));
//            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_PKID, pkid);
            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_WOPENID, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_WOPENID));
            resultMap.put(DatabaseDetails.Users.COLUMN_NAME_SCORE, resultObject.get(DatabaseDetails.Users.COLUMN_NAME_SCORE)+"");
			resultMap.put("use_address", resultObject.get("use_address"));
			resultMap.put("idVerification", resultObject.getString("idVerification"));
			resultMap.put("age", resultObject.getString("age"));
			resultMap.put("wishtype", resultObject.getString("wish_type"));
			resultMap.put("automatchflag", resultObject.getString("automatchflag"));
			resultMap.put("wishaddress", resultObject.getString("wish_address"));
			resultMap.put("sharecode", resultObject.getString("sharecode"));
			try {
				resultMap.put("restaurantflag", resultObject.getString("restaurantflag"));
			} catch (Exception e) {
				resultMap.put("restaurantflag", "1");
			}
        } catch (JSONException e) {
            Log.e("seek", "get user info error");
            e.printStackTrace();
        }

        return resultMap;
    }

    /**
     * 接单开关（是否接收新单推送）
     * @param pkid
     * @param flag  1接单  0不接单
     * @return
     */
    public static String updateOrderFlag(String pkid, String  flag) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mid", pkid);
        map.put("orderflag", flag);
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "updateorderflag", map, "GET");
        } catch (Exception e) {
            Log.e("seek", "update order flag  error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "update order flag  is " + result);

        return result;

    }

    /**
     * 获取红包列表
     * @param mid
     * @return
     */
    public static List<Map<String,Object>> getLuckyMoneyList(String mid) {
        String result;
        HashMap searchmap = new HashMap();
        searchmap.put("mid", mid);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "getluckymoneylist", searchmap, "GET");
        } catch (Exception e) {
            Log.e("seek", "get lucky money list error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "get money list is " + result);
        /**
         * {"rmoney":1,"count":2, // 红包总数量
         * "rcount":1, // 领了几个
         * "ycount":1, // 剩几个
         * "flag":0,   // 0是没领，1是领了
         *
         * "pic":"/photo/1484876768713.jpg","clickratio":5,"content":"同意","time":"1484876791513","title":"咯",,"smoney":1,"money":1,"pkid":"297e99b659b98a150159b98dbede0003","mid":"297e99b659a540030159a60577340000","allmoney":2}
         */
        JSONObject resultJsons = null;
        List<Map<String,Object>> data = new ArrayList<>();
        if(result == null) {
            Log.e("seek", "get money list error, the result is null ");
            return data;
        }
        try {
            resultJsons = new JSONObject(result);
            JSONArray resultJson = resultJsons.getJSONArray("promotionlist");
            for(int i = 0;i<resultJson.length();i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                String clickratio = "0";
                try {
                    clickratio = resultJson.getJSONObject(i).getString(DatabaseDetails.PromotionOrder.COLUMN_NAME_CLICKRATIO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String pic = "";
                try {
                    pic = resultJson.getJSONObject(i).getString(DatabaseDetails.PromotionOrder.COLUMN_NAME_PIC);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put("hot", clickratio);
                map.put("pic", pic);
                map.put("title", resultJson.getJSONObject(i).getString(DatabaseDetails.PromotionOrder.COLUMN_NAME_TITLE));
                map.put("fee", resultJson.getJSONObject(i).getString(DatabaseDetails.PromotionOrder.COLUMN_NAME_MONEY)+"元");
                map.put("pkid", resultJson.getJSONObject(i).getString(DatabaseDetails.PromotionOrder.COLUMN_NAME_PKID));
                map.put("count", resultJson.getJSONObject(i).getInt("count"));  //总数量
                map.put("rcount", resultJson.getJSONObject(i).getInt("rcount"));// 领了几个
                map.put("ycount", resultJson.getJSONObject(i).getInt("ycount"));// 剩几个
                map.put("flag", resultJson.getJSONObject(i).getInt("flag")); // 是否领过
                data.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * 获取订单详情
     * @param pkid
     * @return
     */
    public static String getOrderDetail(String pkid) {
        String result;
        HashMap<String, Object> map = new HashMap<>();
        map.put("iid", pkid);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "orderdetail", map, "GET");
        } catch (Exception e) {
            Log.e("seek", "get order Detail error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return result;
    }

    /**
     *  更新经纬度
     *
     * @return
     */
    public static String updateLocation(String pkid, String lat, String lng) {
		HashMap map = new HashMap();
		map.put("pkid", pkid);
		map.put("latitude", lat);
		map.put("longitude", lng);
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "getlocation", map, "GET");
        } catch (Exception e) {
            Log.e("seek", "update location error error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return result;
    }

	/**
	 *  推送
	 * @param map
	 * @return
	 */
	public static String pushLet(Map map) {
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "pushlet", map, "GET");
		} catch (Exception e) {
			Log.e("seek", "push let error error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;
	}

    /**
     * 基础信息读取（location、服务类型,订单类型）
     * @return
     */
    public static Map<String,List<String>> getLocationsAndServiceTypes() {

        HashMap map = new HashMap();
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "getmessage", null, "GET");
            JSONObject resJson = new JSONObject(result);
            JSONArray locations = resJson.getJSONArray("addresses");
            JSONArray types = resJson.getJSONArray("ordertype");
            JSONArray service = resJson.getJSONArray("service");
            List<String > list = new ArrayList<>();
            for(int i=0;i<locations.length();i++) {
                list.add(locations.getJSONObject(i).toString());
            }
            map.put("locations", list);
            list = new ArrayList<>();
            for(int i=0;i<types.length();i++) {
                list.add(types.getJSONObject(i).toString());
            }
            map.put("types", list);
            for(int i=0;i<service.length();i++) {
                list.add(service.getJSONObject(i).toString());
            }
            map.put("service", list);
            Log.e("seek", "locations and service typs result is " + result);
        } catch (Exception e) {
            Log.e("seek", "get locations and service types error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }

        return map;
    }

	/**
	 * 获取基本品类
	 * @return
	 */
	public static String getMessage() {
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getmessage", null, "GET");
			return result;
		} catch (Exception e) {
			Log.e("seek", "get  message ok  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		Log.e("seek", "get message ok  is " + result);

		return result;
	}

	/**
	 * 获取子品类
	 * @return
	 */
	public static String getSubcategory(String fid) {
		String result;
		HashMap map = new HashMap();
		map.put("fid", fid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getsubcategory", map, "GET");
			//return result;
		} catch (Exception e) {
			Log.e("seek", "get  subcategory ok  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		Log.e("seek", "get subcategory ok  is " + result);

		return result;
	}


	/**
	 * 获取子品类热词
	 * @return
	 */
	public static String getHotWords(String mid, String subid) {
		String result;
		HashMap map = new HashMap();
		map.put("mid", mid);
		map.put("sc_subid", subid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "gethotword", map, "GET");
			//return result;
		} catch (Exception e) {
			Log.e("seek", "get  hot words   error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		Log.e("seek", "get hot words ok  is " + result);

		return result;
	}
    /**
     * 接单者完成订单（状态3 待支付/已送达）
     * @param flagkey
     * @param pkid
     * @param mid
     * @return
     */
    public static String setOrderOk(String flagkey, String pkid, String mid, String rmid) {
        HashMap map = new HashMap();
        map.put("pkid", pkid);
        map.put("flagkey", flagkey);
        map.put("mid", mid);
        map.put("rmid", rmid);
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "receiverdone", map, "GET");
            JSONObject resJson = new JSONObject(result);
            return result;
        } catch (Exception e) {
            Log.e("seek", "set  order ok  error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "set order ok  is " + result);

        return result;
    }

    /**
     * 订餐支付成功，订单完成
     * @param pkid
     * @param flagkey
     * @param paytype
     * @param paymentno
     * @return
     */
    public static String setMessHallPayOk(String pkid, String flagkey, String paytype, String paymentno, String couponprice) {
        HashMap map = new HashMap();
        map.put("pkid", pkid);
        map.put("flagkey", flagkey);
        map.put("paytype", paytype);
        map.put("paymentno", paymentno);
        map.put("couponprice", couponprice);
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "restaurantpaydone", map, "GET");
            Log.e("seek", "restaurantpaydone pay ok result is " + result);
            JSONObject resJson = new JSONObject(result);
            Log.e("seek", "restaurantpaydone pay ok result is " + result);
            return result;
        } catch (Exception e) {
            Log.e("seek", "set  restaurantpaydone pay ok  error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "set restaurantpaydone pay ok  is " + result);

        return result;
    }

    /**
     * 支付成功，订单完成
     * @param pkid
     * @param flagkey
     * @param paytype
     * @param paymentno
     * @return
     */
    public static String setOrderPayOk(String pkid, String flagkey, String paytype, String paymentno, String couponprice) {
        HashMap map = new HashMap();
        map.put("pkid", pkid);
        map.put("flagkey", flagkey);
        map.put("paytype", paytype);
        map.put("paymentno", paymentno);
        map.put("couponprice", couponprice);
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "paydone", map, "GET");
            Log.e("seek", "order pay ok result is " + result);
            JSONObject resJson = new JSONObject(result);
            Log.e("seek", "order pay ok result is " + result);
            return result;
        } catch (Exception e) {
            Log.e("seek", "set  order pay ok  error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "set order pay ok  is " + result);

        return result;
    }

    /**
     * 获取红包详情
     * @param pkid
     * @param mid
     * @return
     */
    public static String getLuckyMoneyDetail(String pkid, String mid) {

        Map<String, Object> map = new HashMap<>();
        map.put("pkid", pkid);
        map.put("mid", mid);
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "luckymoneydetail", map, "GET");
        } catch (Exception e) {
            Log.e("seek", "get lucky money detail error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "get lucky money detail is " + result);

        return result;
    }

    /**
     * 设置用户的微信的openid
     * @param pkid
     * @param wopenid
     * @return
     */
    public static String setWOpenid(String pkid, String wopenid) {
        String result;
        HashMap map = new HashMap();
        map.put("pkid", pkid);
        map.put("wopenid", wopenid);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "setwopenid", map, "GET");
        } catch (Exception e) {
            Log.e("seek", "set wopenid  error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }

        Log.e("seek", "set wopenid result is " + result);
        return result;
    }

    /**
     * 忘记密码之后的改变密码
     * @param userid
     * @param pwd
     * @return
     */
    public static String forgetPwd(String userid, String pwd, String code) {
		HashMap map = new HashMap();
		map.put("username", userid);
		map.put("pwd", pwd);
		map.put("code", code);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "forgetpwd", map, "GET");
			Log.e("seek", "change pwd(forget) result is " + result);
		} catch (Exception e) {
			Log.e("seek", "change pwd(forget) error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

    /**
     * 根据旧密码修改新密码
     * @param pkid
     * @param old_pwd
     * @param new_pwd
     * @return
     */
	public static String changePwd(String pkid, String old_pwd, String new_pwd) {
		HashMap map = new HashMap();
		map.put("pkid", pkid);
		map.put("oldpwd", old_pwd);
		map.put("newpwd", new_pwd);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "updatepwd", map, "GET");
			Log.e("seek", "change pwd(not forgot) result is " + result);
		} catch (Exception e) {
			Log.e("seek", "change pwd(not forgot) error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}

    /**
     * 给接单人进行评分和评语
     * @param mid
     * @param rid
     * @param iid
     * @param score
     * @param remark
     * @return
     */
    public static String setScore(String mid, String rid, String iid, String score, String remark) {
        HashMap map = new HashMap();
        map.put("mid", mid);
        map.put("rid", rid);
        map.put("iid", iid);
        map.put("score", score);
        map.put("remark", remark);
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "membereva", map, "GET");
            JSONObject resJson = new JSONObject(result);
            Log.e("seek", "set score result is " + result);
            return result;
        } catch (Exception e) {
            Log.e("seek", "set  score  error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "set oscore  is " + result);

        return result;
    }

	/**
	 * 设置通知接收的类型
	 * @param mid
	 * @param ordertype
	 * @return
	 */

	public static String setOrderType(String mid, String ordertype) {
		HashMap map = new HashMap();
		map.put("mid", mid);
		map.put("ordertype", ordertype);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "setordertype", map, "GET");
			JSONObject resJson = new JSONObject(result);
			Log.e("seek", "set order type is " + result);
			return result;
		} catch (Exception e) {
			Log.e("seek", "set  order type  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		Log.e("seek", "set order type  is " + result);

		return result;
	}

	/**
	 * 获取通知接收的类型
	 * @param mid
	 * @return
	 */

	public static String getOrderType(String mid) {
		HashMap map = new HashMap();
		map.put("mid", mid);
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getordertype", map, "GET");
			JSONObject resJson = new JSONObject(result);
			Log.e("seek", "get order type is " + result);
			return result;
		} catch (Exception e) {
			Log.e("seek", "get  order type  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		Log.e("seek", "get order type  is " + result);

		return result;
	}

    /**
     * 提现
     * @param pkid
     * @param money
     * @return
     */
    public static String getCash(String pkid, String money) {
        HashMap map = new HashMap();
        map.put("mid", pkid);
        map.put("price", money);
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "getcash", map, "GET");
            Log.e("seek", "get cash is " + result);
            JSONObject resJson = new JSONObject(result);
            return result;
        } catch (Exception e) {
            Log.e("seek", "get cash error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "get cash result is " + result);

        return result;
    }

    /**
     * 绑定手机号
     * @param wopenid
     * @param username
     * @param password
     * @param iphone
     * @return
     */
    public static String bindPhone(String wopenid, String username, String password, String iphone, String phonecode) {
        String result;
        HashMap map = new HashMap();
        map.put("username", username);
        map.put("wopenid", wopenid);
        map.put("password", password);
        map.put("iphone", iphone);
        map.put("phonecode", phonecode);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "bindphone", map, "GET");
        } catch (Exception e) {
            Log.e("seek", "bind phone   error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }

        Log.e("seek", "bind phone result is " + result);
        return result;
    }

    /**
     * 订单超时申请
     * @return
     */
    public static String timeout() {
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "timeout", null, "GET");
        } catch (Exception e) {
            Log.e("seek", "timeout   error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 支付密码是否存在
     * @param mid 用户的pkid
     * @return 0存在 1不存在 2失败
     */
    public static int getPayPWD(String mid) {
        String result;
        HashMap map = new HashMap();
        map.put("pkid", mid);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "findpaypw", map, "GET");
            Log.e("seek", "get pay pwd result is " + result);
            JSONObject json = new JSONObject(result);
            return json.getInt("code");
        } catch (Exception e) {
            Log.e("seek", "get pay pwd  error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return -11;
    }

    /**
     * 设置支付密码
     * @param mid
     * @param pwd
     * @return 0成功 1失败
     */
    public static int setPayPWD(String mid, String pwd) {
        String result;
        HashMap map = new HashMap();
        map.put("pkid", mid);
        map.put("password", pwd);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "addpaypw", map, "GET");
            Log.e("seek", "set pay pwd result is " + result);
            JSONObject json = new JSONObject(result);
            return json.getInt("code");
        } catch (Exception e) {
            Log.e("seek", "set pay pwd  error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return -11;
    }


	/**
	 * 验证支付密码
	 * @param mid
	 * @param pwd
	 * @return 0正确 1错误
	 */
    public static int verifyPayPWD(String mid, String pwd) {
		String result;
		HashMap map = new HashMap();
		map.put("mid", mid);
		map.put("paywd", pwd);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "verifypaypw", map, "GET");
			Log.e("seek", "verify pay pwd result is " + result);
			JSONObject json = new JSONObject(result);
			return json.getInt("code");
		} catch (Exception e) {
			Log.e("seek", "verify pay pwd  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return -11;
	}
    /**
     * 通知服务器获得优惠券
     * @param mid
     * @param flagkey
     * @return
     */
    public static String getCoupon(String mid, String flagkey) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mid", mid);
        map.put("flagkey", flagkey);
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "getcoupon", map, "GET");
        } catch (Exception e) {
            Log.e("seek", "get coupon  error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "get coupon result  is " + result);

        return result;

    }

    /**
     * 获取优惠券列表
     * @param mid
     * @return
     */
    public static String getCouponList(String mid) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mid", mid);
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "mycouponlist", map, "GET");
        } catch (Exception e) {
            Log.e("seek", "get coupons  error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "get coupons result  is " + result);

        return result;

    }

    /**
     * 获取是否有可用优惠券
     * @param mid
     * @return
     */
    public static String getCouponCanUse(String mid) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mid", mid);
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "getcanusecoupon", map, "GET");
        } catch (Exception e) {
            Log.e("seek", "get coupons can use  error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "get coupons count result  is " + result);

        return result;

    }

    /**
     * 议价功能，修改订单价格
     * @param pkid
     * @param flagkey
     * @param price
     * @return
     */
    public static String changeOrderPrice(String pkid, String flagkey, String price) {
        String result;
        HashMap map = new HashMap();
        map.put("pkid", pkid);
        map.put("flagkey", flagkey);
        map.put("price", price);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "priceupdate", map, "GET");
            Log.e("seek", "change order price result " + result);
        } catch (Exception e) {
            Log.e("seek", "change order  price error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 支付之前完成订单操作(状态=4 待支付)
     * @param pkid
     * @param flagkey
     * @return
     */
    public static String setBeforePayOk(String pkid, String flagkey) {
        HashMap map = new HashMap();
        map.put("pkid", pkid);
        map.put("flagkey", flagkey);
        String result;
        try {
            result = net(HttpConstants.HTTP_REQUEST, "beforepay", map, "GET");
            JSONObject resJson = new JSONObject(result);
            Log.e("seek", "before pay ok result is " + result);
            return result;
        } catch (Exception e) {
            Log.e("seek", "set before  pay ok  error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        Log.e("seek", "set before  pay ok  is " + result);

        return result;
    }

    /**
     * 上传身份认证信息
     * @param mid
     * @param handcard
     * @param idcard1
     * @param idcard2
     * @return
     */
    public static String sendIDCardInfo(String mid, String handcard, String idcard1, String idcard2, String realname, String idnumber) {
        String result;
        HashMap map = new HashMap();
        map.put("mid", mid);
        map.put("handcard", handcard);
        map.put("idcard1", idcard1);
        map.put("idcard2", idcard2);
		map.put("realname", realname);
		map.put("idnumber", idnumber);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "sendidcard", map, "GET");
            Log.e("seek", "send id card info result " + result);
        } catch (Exception e) {
            Log.e("seek", "send id card info error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取常用地址列表
     * @param pkid
     * @return
     */
    public static String getMemberAddress(String pkid) {
        String result;
        HashMap map = new HashMap();
        map.put("pkid", pkid);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "getmemberaddress", map, "GET");
            Log.e("seek", "getmemberaddress result " + result);
        } catch (Exception e) {
            Log.e("seek", "getmemberaddress error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除常用地址
     * @param mid
     * @param id
     * @return
     */
    public static String deleteMemberAddress(String mid,String id) {
        String result;
        HashMap map = new HashMap();
        map.put("pkid", id);
        map.put("mid", mid);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "delserviceaddress", map, "GET");
            Log.e("seek", "delserviceaddress result " + result);
        } catch (Exception e) {
            Log.e("seek", "delserviceaddress error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 添加用户常用地址
     * @param pkid
     * @param uid
     * @param detail_address
     * @return
     */
    public static String setMemberAddress(String pkid, String uid, String detail_address, String latitude, String longitude) {
        String result;
        HashMap map = new HashMap();
        map.put("pkid", pkid);
        map.put("uid", uid);
        map.put("detail_address", detail_address);
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "setmemberaddress", map, "GET");
            Log.e("seek", "getmemberaddress result " + result);
        } catch (Exception e) {
            Log.e("seek", "getmemberaddress error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置默认地址
     * @param pkid
     * @param aid
     * @return
     */
    public static String setDefaultAddress(String pkid, String aid) {
        String result;
        HashMap map = new HashMap();
        map.put("pkid", pkid);
        map.put("aid", aid);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "updatemaddresstype", map, "GET");
            Log.e("seek", "set default address result " + result);
        } catch (Exception e) {
            Log.e("seek", "set default address error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 修改地址
     * @param aid
     * @param detail_address
     * @return
     */
    public static String updateAddress(String aid, String detail_address, String uid, String lat, String lng) {
        String result;
        HashMap map = new HashMap();
        map.put("aid", aid);
        map.put("detail_address", detail_address);
        map.put("uid", uid);
        map.put("latitude", lat);
        map.put("longitude", lng);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "updatemaddress", map, "GET");
            Log.e("seek", "update address result " + result);
        } catch (Exception e) {
            Log.e("seek", "update address error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更改服务区域
     * @param mid
     * @param uid
     * @return
     */
    public static String changeServiceField(String mid, String uid) {
        String result;
        HashMap map = new HashMap();
        map.put("uid", uid);
        map.put("mid", mid);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "addserviceaddress", map, "GET");
            Log.e("seek", "change service field result " + result);
        } catch (Exception e) {
            Log.e("seek", "change service field error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return result;
    }

	/**
	 * 获取榜单列表
	 * @return
	 */
	public static String getTranking() {
		String result;
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getranking", null, "GET");
			Log.e("seek", "getranking result " + result);
		} catch (Exception e) {
			Log.e("seek", "getranking error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取今天完成数
	 * @param mid
	 * @return
	 */
	public static String getMyFinish(String mid) {
		String result;
		HashMap map = new HashMap();
		map.put("mid", mid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "myfinish", map, "GET");
			Log.e("seek", "myfinish result " + result);
		} catch (Exception e) {
			Log.e("seek", "myfinish error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * 获取收益
	 * @param mid
	 * @return
	 */
	public static String getMyIncome(String mid) {
		String result;
		HashMap map = new HashMap();
		map.put("mid", mid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "myincome", map, "GET");
			Log.e("seek", "myincome result " + result);
		} catch (Exception e) {
			Log.e("seek", "myincome error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;

	}


	/**
	 * 获取余额
	 * @param mid
	 * @return
	 */
	public static String getAccount(String mid) {
		String result;
		HashMap map = new HashMap();
		map.put("mid", mid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getaccount", map, "GET");
			Log.e("seek", "getaccount result " + result);
		} catch (Exception e) {
			Log.e("seek", "getaccount error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;

	}


	/**
	 * 获取余额明细
	 * @param mid
	 * @return
	 */
	public static String getAccountDetail(String mid) {
		String result;
		HashMap map = new HashMap();
		map.put("mid", mid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getyuedetail", map, "GET");
			Log.e("seek", "getyuedetail result " + result);
		} catch (Exception e) {
			Log.e("seek", "getyuedetail error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;

	}


	/**
	 * 获取消息
	 * @param mid
	 * @return
	 */
	public static String getMembrMessage(String mid) {
		String result;
		HashMap map = new HashMap();
		map.put("mid", mid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getmembermessage", map, "GET");
			Log.e("seek", "getmembermessage result " + result);
		} catch (Exception e) {
			Log.e("seek", "getmembermessage error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * 获取消息
	 * @param iid
	 * @return
	 */
	public static String getMessageDetail(String iid) {
		String result;
		HashMap map = new HashMap();
		map.put("iid", iid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getmessagedetail", map, "GET");
			Log.e("seek", "getmessagedetail result " + result);
		} catch (Exception e) {
			Log.e("seek", "getmessagedetail error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;

	}


	/**
	 * 获取是否有未读信息
	 * @param mid
	 * @return
	 */
	public static String getUnreadMessage(String mid) {
		String result;
		HashMap map = new HashMap();
		map.put("mid", mid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getunread", map, "GET");
			Log.e("seek", "getunread result " + result);
		} catch (Exception e) {
			Log.e("seek", "getunread error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * 搜索好友
	 * @param username
	 * @return
	 */
	public static String searchMember(String username, String mid) {
		String result;
		HashMap map = new HashMap();
		map.put("username", username);
		map.put("mid", mid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "searchmember", map, "GET");
			Log.e("seek", "searchmember result " + result);
		} catch (Exception e) {
			Log.e("seek", "searchmember error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 接单top
	 * @return
	 */
	public static String getMemberTop(String mid) {
		String result;
		HashMap map = new HashMap();
		map.put("mid", mid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "getmembertop", map, "GET");
			Log.e("seek", "getmembertop result " + result);
		} catch (Exception e) {
			Log.e("seek", "getmembertop error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 添加好友
	 * @param mid
	 * @param friendid
	 * @return
	 */
	public static String addFriend(String mid, String friendid) {

		String result;
		HashMap map = new HashMap();
		map.put("mid", mid);
		map.put("friendid", friendid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "addfriend", map, "GET");
			Log.e("seek", "addfriend result " + result);
		} catch (Exception e) {
			Log.e("seek", "addfriend error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * 验证好友
	 * @param id
	 * @param mid
	 * @param friendid
	 * @return
	 */
	public static String updFriendFlag(String id, String mid, String friendid) {

		String result;
		HashMap map = new HashMap();
		map.put("id", id);
		map.put("mid", mid);
		map.put("friendid", friendid);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "updfriendflag", map, "GET");
			Log.e("seek", "updfriendflag result " + result);
		} catch (Exception e) {
			Log.e("seek", "updfriendflag error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * 删除好友
	 * @param id
	 * @return
	 */
	public static String delFriend(String id) {

		String result;
		HashMap map = new HashMap();
		map.put("id", id);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "delfriend", map, "GET");
			Log.e("seek", "delfriend result " + result);
		} catch (Exception e) {
			Log.e("seek", "delfriend error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * 获取好友列表
	 * @param user_id
	 * @return
	 */
	public static String getFriendList(String user_id) {

		String result;
		HashMap map = new HashMap();
		map.put("user_id", user_id);
		try {
			result = net(HttpConstants.HTTP_REQUEST, "/Api/user/friend", map, "POST");
			Log.e("seek", "getallfriend result " + result);
		} catch (Exception e) {
			Log.e("seek", "getallfriend error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * 更新deviceToken
	 * @param mid
	 * @param device_token
	 * @param flagkey
	 * @return
	 */
    public static String updateDeviceToken(String mid, String device_token, String flagkey) {
        String result;
        HashMap map = new HashMap();
        map.put("devicetokens", device_token);
        map.put("mid", mid);
        map.put("flagkey", flagkey);
        try {
            result = net(HttpConstants.HTTP_REQUEST, "updatedevicetokens", map, "GET");
            Log.e("seek", "update device token result " + result);
        } catch (Exception e) {
            Log.e("seek", "update device token result error");
            result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
            e.printStackTrace();
        }
        return result;
    }


	public static String qqlogin(String openid) {
		HashMap map = new HashMap();
		map.put("qopenid", openid);
		String result;
		try {
			result = net(HttpConstants.HTTP_LOGIN, map, "GET");
		} catch (Exception e) {
			Log.e("seek", "qqlogin error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}

		return result;
	}



	public static String updateLuckyMoney(String pkid, String money, String pwd) {
		String result;
		HashMap map = new HashMap();
		map.put("pkid", pkid);
		map.put("money", money);
		map.put("password", pwd);
		try {
			result = net(HttpConstants.HTTP_UPDATE_LUCKY_MONEY, map, "GET");
			Log.e("seek", "update lucky money result is " + result);
		} catch (Exception e) {
			Log.e("seek", "update lucky money  error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return result;
	}

	public static List<String> getLocations() {
		List<String > list = new ArrayList<>();
		String result;
		try {
			result = net(HttpConstants.HTTP_GET_LOCATIONS, null, "GET");
		} catch (Exception e) {
			Log.e("seek", "get locations error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
		}
		return list;
	}

	public static List<Map<String,Object>> getOrders() {
//		map = new HashMap<String, Object>();
//		map.put("order_time", "11：00");
//		map.put("order_fee", "10");
//		map.put("order_info", "代上课");
//		map.put("type", 3);

		String result;
		try {
			result = net(HttpConstants.HTTP_GET_ORDERS, null, "GET");

			/*
			调用订单超时接口
			 */
			net(HttpConstants.HTTP_TIMEOUT, null, "GET");
		} catch (Exception e) {
			Log.e("seek", "get orders error");
			result = "{\"code\":\"-11\",\"msg\":\"失败\"}";
			e.printStackTrace();
			return null;
		}
		Log.e("seek", "the  orders is " + result);

		List<Map<String,Object>> data = new ArrayList<>();
//		{stime=16:09:03, count=0, location=null, state=1,
//				rmid=null, type=1, content=郭股海护航vv陈风风光光, time=1481534741971,
//				title=null, price=2.0, rtime=null, pkid=297e99b658f23f050158f25a1ddd0006,
//       longitude=null, mid=297e99b658f1a2080158f1ad96e70000, latitude=null, reson=null
		try {
			JSONObject resultJsons = new JSONObject(result);
			JSONArray resultJson = resultJsons.getJSONArray("normallist");
			for(int i = 0;i<resultJson.length();i++) {
				HashMap<String, Object> map = new HashMap<>();
				JSONObject order = resultJson.getJSONObject(i);
//				Log.e("seek", "the order is " + order);
				map.put("order_time", order.getString("stime"));
				map.put("order_fee", order.getString("price"));
				map.put("order_info", order.getString("type"));
				map.put("type", order.getString("type"));
				map.put("pkid", order.getString("pkid"));
				map.put("time", order.getString("time"));
				map.put("mid", order.getString("mid"));
				map.put("state", Integer.parseInt(order.getString("state")));

				data.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}


		return data;
	}




	//2.发送短信
	public static int getRequest2(String phoneNumber, String value, String type) {
		String result = null;
		String url = "http://v.juhe.cn/sms/send";//请求接口地址
		Map params = new HashMap();//请求参数
		params.put("mobile", phoneNumber);//接收短信的手机号码
		params.put("dtype", type);//返回数据的格式,xml或json，默认json

		try {
			result = net(url, params, "GET");
			JSONObject object = new JSONObject(result);
			return object.getInt("error_code");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return - 1;
	}

	/**
	 *
	 * @param strUrl
	 * @param func
	 * @param params
	 * @param method
	 * @return
	 * @throws Exception
	 */

	public static String net(String strUrl, String func, Map params,String method) throws Exception {
        Log.e("seek", "func is " + func);
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			strUrl += func;
			if((method==null || method.equals("GET"))&& params != null){
				strUrl = strUrl + "&" + urlencode(params);
			}
			Log.e("seek", "the url is " + strUrl);
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			if(method==null || method.equals("GET")){
				conn.setRequestMethod("GET");
			}else{
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			}
			conn.setRequestProperty("User-agent", userAgent);
			conn.setUseCaches(false);
			conn.setConnectTimeout(DEF_CONN_TIMEOUT);
			conn.setReadTimeout(DEF_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.connect();
			if (params!= null && method.equals("POST")) {
				Log.e("seek", "post params, " + params);
				try {
					DataOutputStream out = new DataOutputStream(conn.getOutputStream());
					out.writeBytes(urlencode(params));
				} catch (Exception e) {
					Log.e("seek", "exception login");
					// TODO: handle exception
				}
			}
			InputStream is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			rs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}

		Log.e("seek", func + " net result is " + rs);
		return rs;
	}

	/**
	 *
	 * @param strUrl 请求地址
	 * @param params 请求参数
	 * @param method 请求方法
	 * @return  网络请求字符串
	 * @throws Exception
	 */
	public static String net(String strUrl, Map params,String method) throws Exception {
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			if((method==null || method.equals("GET"))&& params != null){
				strUrl = strUrl+"?"+urlencode(params);
			}
			Log.e("seek", "the url is " + strUrl);
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			if(method==null || method.equals("GET")){
				conn.setRequestMethod("GET");
			}else{
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			}
			conn.setRequestProperty("User-agent", userAgent);
			conn.setUseCaches(false);
			conn.setConnectTimeout(DEF_CONN_TIMEOUT);
			conn.setReadTimeout(DEF_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.connect();
			if (params!= null && method.equals("POST")) {
                Log.e("seek", "post params, " + params);
				try {
					DataOutputStream out = new DataOutputStream(conn.getOutputStream());
					out.writeBytes(urlencode(params));
				} catch (Exception e) {
                    Log.e("seek", "exception login");
					// TODO: handle exception
				}
			}
			InputStream is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			rs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rs;
	}

	//将map型转为请求参数型
	public static String urlencode(Map<String,Object>data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		String ret = sb.toString();
		Log.e("seek", "post map data is " + ret.substring(0,ret.length()-1));
		return ret.substring(0,ret.length()-1);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是json形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		// Log.e("test", "url is " + url);
		// Log.e("test", "param is " + param);
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

}
