package cn.com.lamatech.date.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lamatech.seekserverproxy.HttpConstants;
import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;

public class PhoneLoginActivity extends AppCompatActivity implements TextWatcher{

    EditText phone;
    EditText pwd;
    Button done;
    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        LinearLayout back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        phone = findViewById(R.id.phone);
        pwd = findViewById(R.id.pwd);
        done = findViewById(R.id.done);

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PhoneLoginActivity.this, RegisterActivity.class));
            }
        });

        phone.addTextChangedListener(this);
        pwd.addTextChangedListener(this);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phone.length() <= 0 || pwd.length() < 6) {
                    Toast.makeText(PhoneLoginActivity.this, "输入不全", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread() {
                    public void run() {
                        String result = ServerProxy.login(phone.getText().toString(), pwd.getText().toString(), "");
                        if(result == null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PhoneLoginActivity.this, "登录失败，请重试", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        Log.e("date", "log in result is " + result);
                        try {
                            JSONObject res = new JSONObject(result);
                            if(res.getInt("code") == 400) {
                                final String msg = res.getString("msg");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(PhoneLoginActivity.this);
                                        builder.setMessage(msg);
//                                        builder.setPositiveButton("创建新账号", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                startActivity(new Intent(PhoneLoginActivity.this, RegisterActivity.class));
//                                            }
//                                        });
                                        builder.setNegativeButton("取消", null);
                                        builder.show();
                                    }
                                });
                            } else if(res.getInt("code") == 200) {
                                /*
                                "data":{"user_id":"16","sex":"2","account_mobile":"13041009481","nickname":"13041009481","head_pic":""
                                 */
                                SharedPreferences pref = getSharedPreferences(Date.DATE, 0);
                                JSONObject data = res.getJSONObject("data");
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putLong(Date.UserInfo.LAST_LOGIN, System.currentTimeMillis());
                                editor.putString(Date.UserInfo.USER_ID, phone.getText().toString());
                                editor.putString(Date.UserInfo.PWD, pwd.getText().toString());
//                                editor.putString(Date.UserInfo.PKID, pkid);
//                                editor.putString(Date.UserInfo.FLAG_KEY, res.getString("flagkey"));
                                editor.commit();

                                HashMap <String, Object> map = new HashMap<>();
                                map.put("user_id", data.getString("user_id"));
                                map.put("sex", data.getString("sex"));
                                map.put("phone", data.getString("account_mobile"));
                                map.put("name", data.getString("nickname"));
                                map.put("head_pic", data.getString("head_pic").replace("\\", ""));
                                map.put("level", data.getString("level"));
                                map.put("auth_video_status", data.getString("auth_video_status"));
//                                try {
//                                    map.put("token", data.getString("token"));
//                                } catch (Exception e) {
//                                    map.put("token", "");
//                                }
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

                                startActivity(new Intent(PhoneLoginActivity.this, MainActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(phone.length() > 1 && pwd.length() >= 6) {
            done.setBackgroundResource(R.drawable.login_ok_background);
        } else {
            done.setBackgroundResource(R.drawable.login_background);
        }
    }
}
