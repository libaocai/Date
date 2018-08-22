package cn.com.lamatech.date.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lamatech.seekserverproxy.HttpConstants;
import com.lamatech.seekserverproxy.ServerProxy;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView wechat;
    ImageView webo;
    ImageView qq;
    TextView email_phone;
    WXLoginBroadcastReceiver wxLoginBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lamatech.date.CLOSEDIALOG");
        wxLoginBroadcastReceiver = new WXLoginBroadcastReceiver();
        registerReceiver(wxLoginBroadcastReceiver,intentFilter);

//        new Thread() {
//            public void run() {
//                if(autoLogin()) {
//                    finish();
//                }
//            }
//        }.start();

    }
    void initViews() {
        wechat = findViewById(R.id.wechat);
        webo = findViewById(R.id.webo);
        qq = findViewById(R.id.qq);
        email_phone = findViewById(R.id.email_phone);


        wechat.setOnClickListener(this);
        webo.setOnClickListener(this);
        qq.setOnClickListener(this);
        email_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wechat:
                SendAuth.Req req = new SendAuth.Req();
                final IWXAPI api = WXAPIFactory.createWXAPI(LoginActivity.this, Date.WX_APP_ID);
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo_test";
                api.sendReq(req);
                break;
            case R.id.qq:
                break;
            case R.id.webo:
                break;
            case R.id.email_phone:
                startActivity(new Intent(LoginActivity.this, PhoneLoginActivity.class));
                finish();
                break;

        }
//        startActivity(new Intent(LoginActivity.this, PhoneLoginActivity.class));
//        finish();

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
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                return true;
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    void connect() {
        RongIM.connect(Date.mUserInfo.token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
            }
            @Override
            public void onSuccess(String userid) {
                //userid，是我们在申请token时填入的userid
                Log.e("date" , "sucess , userid is " +userid);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "链接服务器成功！", Toast.LENGTH_SHORT).show();

                    }
                });
            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("date" , "sucess , error is ");
                        Toast.makeText(LoginActivity.this, "链接服务器失败！", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wxLoginBroadcastReceiver);
    }

    class WXLoginBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra("finish", true)) {
                    finish();
                }
        }
    }

}
