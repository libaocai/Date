package cn.com.lamatech.date.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lamatech.seekserverproxy.HttpConstants;
import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;
import cn.com.lamatech.date.service.UpdateLocationService;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        //startActivity(new Intent(FirstActivity.this, LoginActivity.class));
        startService(new Intent(FirstActivity.this, UpdateLocationService.class));
        new Thread() {
            public void run() {
                if(autoLogin()) {
                    finish();
                } else {
                    startActivity(new Intent(FirstActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }.start();
    }
    boolean autoLogin() {
        SharedPreferences pref = getSharedPreferences(Date.DATE, 0);
        String nameID = pref.getString(Date.UserInfo.USER_ID, null);
        String pwd = pref.getString(Date.UserInfo.PWD, null);
        String device_token = pref.getString(Date.UserInfo.DEVICE_TOKEN, null);

        long lastLogin = pref.getLong(Date.UserInfo.LAST_LOGIN, 0);
        if(!((System.currentTimeMillis() - lastLogin) <= (7 * 24 * 60 * 60 * 1000))) { //7天未登录过
            return false;
        }
        if (nameID != null && pwd != null && lastLogin != 0) {
            String result = ServerProxy.login(nameID, pwd, device_token);

            Log.e("date", "auto log in , result is " + result);
            JSONObject ret = null;
            int code = -111;
            JSONObject data;
            String pkid = null;
            String flagkey = null;
            if (result == null) {
                return false;
            }
            try {
                ret = new JSONObject(result);
                code = ret.getInt("code");
                data = ret.getJSONObject("data");


                if (code == 200) {//
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putLong(Date.UserInfo.LAST_LOGIN, System.currentTimeMillis());
                    editor.putString(Date.UserInfo.FLAG_KEY, flagkey);
                    editor.commit();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("user_id", data.getString("user_id"));
                    map.put("uuid", data.getString("uuid"));
                    map.put("sex", data.getString("sex"));
                    map.put("phone", data.getString("account_mobile"));
                    map.put("name", data.getString("nickname"));
                    map.put("head_pic", data.getString("head_pic").replace("\\", ""));
                    map.put("auth_video_status", data.getString("auth_video_status"));
                    try {
                        map.put("level", data.getString("level"));
                    } catch (Exception e) {
                        map.put("level", "0");
                    }
//                try {
//                    map.put("token", data.getString("token"));
//                } catch (Exception e) {
//                    map.put("token", "");
//                }
                    Log.e("date","head_pic is " + data.getString("head_pic").replace("\\", ""));

                    Date.mUserInfo = new Date.UserInfo(map);

                    new Thread(){
                        public void run() {
                            HashMap map = new HashMap();
                            map.put("user_id", Date.mUserInfo.userid);
                            map.put("province", "");
                            map.put("city", "");
                            map.put("longitude", Date.lng+"");
                            map.put("latitude", Date.lat+"");
                            String result = ServerProxy.updateLocation(map);

                            Log.e("date", "update locations the reuslt is " + result);
                            if(result == null) return;
                        }
                    }.start();

                    new Thread() {
                        public void run() {
                            String result = ServerProxy.getToken(Date.mUserInfo.userid);
                            Log.e("date", "token result is " + result);
                            if(result == null) return;
                            try {
                                JSONObject res = new JSONObject(result);
                                Date.mUserInfo.token = res.getJSONObject("data").getString("token").replace("//", "");
                                Log.e("date", "token is " + Date.mUserInfo.token);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                    Bitmap tmpBitmap = null;
                    try {
                        InputStream is = new java.net.URL(HttpConstants.HTTP_HEAD_PIC + Date.mUserInfo.head_pic).openStream();
                        tmpBitmap = BitmapFactory.decodeStream(is);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Date.mUserInfo.setHeadPicBm(tmpBitmap);
                    startActivity(new Intent(FirstActivity.this, MainActivity.class));
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
