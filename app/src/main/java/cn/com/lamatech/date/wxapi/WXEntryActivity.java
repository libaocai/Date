package cn.com.lamatech.date.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.lamatech.seekserverproxy.HttpConstants;
import com.lamatech.seekserverproxy.ServerProxy;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.activity.LoginActivity;
import cn.com.lamatech.date.activity.MainActivity;
import cn.com.lamatech.date.activity.UserInfoActivity;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //配置您申请的KEY
    public static final String APPKEY = "dae4feb86c8185e0cb7f5555dd76bab9";
    public static final String MOULD_ID = "20184";
    private IWXAPI api;
    TextView code_textview;
    TextView access_token_textview;
    String code;
    String note1;
    String accessToken;
    String openID;
    String refreshToken;
    long expires_in;

    String APPID =  Date.WX_APP_ID;
    String SECRET = Date.WX_APP_SECRET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.wechat_login_back);
//        code_textview = (TextView)findViewById(R.id.code);
//        access_token_textview = (TextView) findViewById(R.id.access_token);
        api = WXAPIFactory.createWXAPI(this, "wx05c03dffed3fb13d");
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("date", "onResp ......");

        Intent intent = new Intent("com.lamatech.date.CLOSEDIALOG");

        int errorCode = resp.errCode;

        if(resp.getType() == 2) {
            finish();
            if(errorCode == BaseResp.ErrCode.ERR_OK) {
                // 分享成功，通知服务器需要得到优惠券
                new Thread() {
                    public void run() {
                        String result = ServerProxy.getCoupon(Date.mUserInfo.pkid, "");
                        Log.e("date", "get coupon result is " + result);
                        try {
                            JSONObject json = new JSONObject(result);
                            if(json.getInt("code") == 0) {
                                mHandler.sendEmptyMessage(3);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } else {
                finish();
            }
            return;
        }
        Log.e("date", "onResp ......, errorCode is " + errorCode);
        Log.e("date", "onResp ...., type is " + resp.getType());
        switch (errorCode) {
            case BaseResp.ErrCode.ERR_OK:
                sendBroadcast(intent);
                //用户同意
                code = ((SendAuth.Resp) resp).code;
                Log.e("date", "onResp ......, code is " + code);
                //code_textview.setText(code);
                new Thread() {
                    public void run() {
                        getAccessToken(code);
                    }
                }.start();

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                intent.putExtra("finish", false);
                sendBroadcast(intent);
                finish();
                //用户拒绝
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                intent.putExtra("finish", false);
                sendBroadcast(intent);
                finish();
                //用户取消
                break;
            default:
                intent.putExtra("finish", false);
                sendBroadcast(intent);
                finish();
                break;
        }
        Log.e("wechat", "onResp ......, resp.errStr is " + resp.errStr);

        // Toast.makeText(WXEntryActivity.this, resp.errStr, Toast.LENGTH_LONG).show();

    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(WXEntryActivity.this, "登录出错，请重新登录", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0){
                sendBroadcast(new Intent("com.lamatech.date.BIND_OK"));
                Toast.makeText(WXEntryActivity.this, "微信支付绑定成功", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3){
                Toast.makeText(WXEntryActivity.this, "3元优惠券已存入我的钱包", Toast.LENGTH_LONG).show();
                sendBroadcast(new Intent("com.lamatech.date.SHARED_SUCCESS"));
            }else{
                Toast.makeText(WXEntryActivity.this, "微信支付绑定失败", Toast.LENGTH_SHORT).show();
            }
            //access_token_textview.setText(accessToken);
            finish();
            return true;
        }
    });

    void setWOpenid(final String openid) {
        new Thread() {
            public void run() {
                String result = ServerProxy.setWOpenid(Date.mUserInfo.pkid, openid);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getInt("code") == 0) {
                        Date.mUserInfo.wopenid = openid;
                        mHandler.sendEmptyMessage(0);
                    } else {
                        mHandler.sendEmptyMessage(2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //
    public String getAccessToken(String code) {
        String result = null;
        SharedPreferences pref = getSharedPreferences(Date.DATE, 0);
        try {
            result = net("GET", code);
            JSONObject object = new JSONObject(result);
            Log.e("date", "wx back object is " + object.toString());
            accessToken = object.getString("access_token");
            openID = object.getString("openid");
            refreshToken = object.getString("refresh_token");
            expires_in = object.getLong("expires_in");
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("wopenid", openID);
            editor.commit();


                HashMap<String, Object> wxmap = new HashMap<>();
                wxmap.put("access_token", accessToken);
                wxmap.put("openid", openID);
                wxmap.put("lang", "zh_CN");
                String wxresult = ServerProxy.net("https://api.weixin.qq.com/sns/userinfo", wxmap, "GET");
                Log.e("date", wxresult);
                JSONObject wxjson = new JSONObject(wxresult);
                final String nickname = wxjson.getString("nickname");
                String headPicUrl = wxjson.getString("headimgurl");
                String sex = wxjson.getString("sex");
                String city = wxjson.getString("city");
                String province = wxjson.getString("province");

                HashMap map = new HashMap();
                map.put("account_id", openID);
//                map.put("nickname", nickname);
//                map.put("head_pic", headPicUrl);
//                map.put("sex", sex);
                map.put("type", "weixin");
                String result_wxcheck = ServerProxy.thirdIsRegister(map);
                if(result_wxcheck == null) {
                    return null;
                }
                Log.e("date", "result_wxcheck is "  + result_wxcheck);

                JSONObject res = new JSONObject(result_wxcheck);
                JSONObject data = res.getJSONObject("data");
            if (res.getInt("code") == 200) {//

                if(data.getInt("is_register") == 1) {
                    HashMap loginmap = new HashMap();
                    loginmap.put("account_id", openID);
                    loginmap.put("nickname", nickname);
                    loginmap.put("head_pic", headPicUrl);
                    loginmap.put("sex", sex);
                    loginmap.put("type", "weixin");
                    String result_wxlogin = ServerProxy.thirdLogin(map);
                    if(result_wxlogin == null) {
                        return null;
                    }
                    Log.e("date", "result_wxlogin is "  + result_wxlogin);

                    JSONObject reslogin = new JSONObject(result_wxlogin);
                    JSONObject datalogin = reslogin.getJSONObject("data");
                    HashMap<String, Object> userinfMap = new HashMap<>();
                    userinfMap.put("user_id", datalogin.getString("user_id"));
                    userinfMap.put("uuid", datalogin.getString("uuid"));
                    userinfMap.put("sex", datalogin.getString("sex"));
                    userinfMap.put("phone", datalogin.getString("account_mobile"));
                    userinfMap.put("name", datalogin.getString("nickname"));
                    userinfMap.put("head_pic", datalogin.getString("head_pic").replace("\\", ""));
                    userinfMap.put("level", datalogin.getString("level"));
                    userinfMap.put("auth_video_status", datalogin.getString("auth_video_status"));
//                try {
//                    userinfMap.put("token", data.getString("token"));
//                } catch (Exception e) {
//                    userinfMap.put("token", "");
//                }
                    Log.e("date", "head_pic is " + datalogin.getString("head_pic").replace("\\", ""));

                    Date.mUserInfo = new Date.UserInfo(userinfMap);

                    new Thread() {
                        public void run() {
                            HashMap map = new HashMap();
                            map.put("user_id", Date.mUserInfo.userid);
                            map.put("province", "");
                            map.put("city", "");
                            map.put("longitude", Date.lng + "");
                            map.put("latitude", Date.lat + "");
                            String result = ServerProxy.updateLocation(map);

                            Log.e("date", "update locations the reuslt is " + result);
                            if (result == null) return;
                        }
                    }.start();

                    new Thread() {
                        public void run() {
                            String result = ServerProxy.getToken(Date.mUserInfo.userid);
                            Log.e("date", "token result is " + result);
                            if (result == null) return;
                            try {
                                JSONObject res = new JSONObject(result);
                                Date.mUserInfo.token = res.getJSONObject("data").getString("token").replace("//", "");
                                Log.e("date", "token is " + Date.mUserInfo.token);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                } else {
                    Intent intent = new Intent(WXEntryActivity.this, UserInfoActivity.class);
                    intent.putExtra("nickname", nickname);
                    intent.putExtra("sex", sex);
                    intent.putExtra("headpic", headPicUrl);
                    intent.putExtra("is_third", true);
                    intent.putExtra("account_id", openID);
                    intent.putExtra("type", "weixin");
                    startActivity(intent);
                    finish();
                }

                Bitmap tmpBitmap = null;
                try {
                    InputStream is = new java.net.URL(Date.mUserInfo.head_pic).openStream();
                    tmpBitmap = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Date.mUserInfo.setHeadPicBm(tmpBitmap);
                startActivity(new Intent(WXEntryActivity.this, MainActivity.class));
                finish();
                return accessToken;
            }
//                String result_userinfo =  ServerProxy.getUserInfo(pkid);
//                if(result_userinfo == null) {
//                    return null;
//                }
//                Date.mUserInfo = new Date.UserInfo(userinfomap);
//                Date.mUserInfo.pkid = pkid;

                editor.putLong(Date.UserInfo.LAST_LOGIN, System.currentTimeMillis());
                editor.putString(Date.UserInfo.USER_ID, Date.mUserInfo.userid);
                editor.commit();
                if (Date.mUserInfo.username == null || "".equals(Date.mUserInfo.username)) {
                    Date.mUserInfo.username = nickname;
                    new Thread() {
                        public void run() {
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("name", nickname);
                            map.put("iphone", Date.mUserInfo.phone);
                            String result = ServerProxy.updateUserInfo(Date.mUserInfo.pkid,map);
                            Log.e("date", "reuslt is " + result);
                            try {
                                JSONObject json = new JSONObject(result);
                                if(json.getInt("code") == 0) {
                                    sendBroadcast(new Intent(
                                            "com.lamatech.date.action.UPDATE_USER_INFO"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                Bitmap tmpBitmap = null;
                if (Date.mUserInfo.head_pic == null || "".equals((Date.mUserInfo.head_pic))) {
                    try {
                        InputStream is = new URL(headPicUrl).openStream();
                        tmpBitmap = BitmapFactory.decodeStream(is);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    try {
                        InputStream is = new URL(HttpConstants.HTTP_HEAD_PIC + Date.mUserInfo.head_pic).openStream();
                        tmpBitmap = BitmapFactory.decodeStream(is);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//                Date.mUserInfo.setHeadPicBm(tmpBitmap);
//                if(res.getInt("flag") == 0) {
//                    Intent intent = new Intent(WXEntryActivity.this, BindPhoneActivity.class);
//                    intent.putExtra("wopenid", openID);
//                    startActivity(intent);
//                } else {
//                    startActivity(new Intent(WXEntryActivity.this, NewMainActivity.class));
//                }

            finish();
            return accessToken;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    public String net(String method, String code) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            Map params = new HashMap();//请求参数
            params.put("appid", APPID);
            params.put("secret", SECRET);
            params.put("code", code);
            params.put("grant_type", "authorization_code");
            String strUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
            if (method == null || method.equals("GET")) {
                strUrl = strUrl + "?" + urlencode(params);
            }
            Log.e("wechat", "the url is " + strUrl);
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();

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
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String ret = sb.toString();
        return ret.substring(0, ret.length() - 1);
    }


}
