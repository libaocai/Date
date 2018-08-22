package cn.com.lamatech.date.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.com.lamatech.date.R;
import cn.com.lamatech.date.utils.DateTimePickDialogUtil;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    String mobile;
    String pwd;
    String code;
    String nickname = "";
    String sex = "2";
    String birthday_day = "";
    String country = "000000";
    String province = "";
    String city_name = "";
    String province_code = "";
    String city_code = "";
    String qq_number = "";
    String headpic="";

    LinearLayout gender_male_layout;
    ImageView gender_male_pic;
    TextView gender_male;

    LinearLayout gender_female_layout;
    ImageView gender_female_pic;
    TextView gender_female;

    LinearLayout birthday_layout;
    TextView birthday;

    LinearLayout city_layout;
    TextView city;

    EditText qq;
    EditText name;
    Button done;

    boolean is_third = false;
    String account_id = "";
    String type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mobile = getIntent().getStringExtra("mobile");
        pwd = getIntent().getStringExtra("pwd");
        code = getIntent().getStringExtra("code");
        is_third = getIntent().getBooleanExtra("is_third", false);
        if(is_third) {
            nickname = getIntent().getStringExtra("nickname");
            sex = getIntent().getStringExtra("sex");
            headpic = getIntent().getStringExtra("headpic");
            account_id = getIntent().getStringExtra("account_id");
            type = getIntent().getStringExtra("type");
        }

        Button done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity.this, HeadPicActivity.class);
                intent.putExtra("first", true);
                startActivity(intent);
                finish();
            }
        });

        initViews();
    }

    void initViews() {
        gender_male_layout = findViewById(R.id.gender_male_layout);
        gender_male_pic = findViewById(R.id.gender_male_pic);
        gender_male = findViewById(R.id.gender_male);

        gender_female_layout = findViewById(R.id.gender_female_layout);
        gender_female_pic = findViewById(R.id.gender_female_pic);
        gender_female = findViewById(R.id.gender_female);

        name = findViewById(R.id.name);
        name.addTextChangedListener(this);
        city_layout = findViewById(R.id.city_layout);
        city = findViewById(R.id.city);

        birthday_layout = findViewById(R.id.birthday_layout);
        birthday = findViewById(R.id.birthday);


        qq = findViewById(R.id.qq);
        qq.addTextChangedListener(this);

        gender_male_layout.setOnClickListener(this);
        gender_female_layout.setOnClickListener(this);
        city_layout.setOnClickListener(this);
        birthday_layout.setOnClickListener(this);

        done = findViewById(R.id.done);
        done.setOnClickListener(this);

        if(is_third) {
            name.setText(nickname);
            if(sex.equals("1")) {
                gender_male_pic.setImageResource(R.mipmap.mine_male);
                gender_male.setTextColor(getResources().getColor(R.color.colorGenderMale));
                gender_female_pic.setImageResource(R.mipmap.female_noselect);
                gender_female.setTextColor(getResources().getColor(R.color.colorGenderNoselect));
            } else {
                gender_male_pic.setImageResource(R.mipmap.male_noselect);
                gender_male.setTextColor(getResources().getColor(R.color.colorGenderNoselect));
                gender_female_pic.setImageResource(R.mipmap.mine_female);
                gender_female.setTextColor(getResources().getColor(R.color.colorGenderFeale));
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gender_male_layout:
                gender_male_pic.setImageResource(R.mipmap.mine_male);
                gender_male.setTextColor(getResources().getColor(R.color.colorGenderMale));
                gender_female_pic.setImageResource(R.mipmap.female_noselect);
                gender_female.setTextColor(getResources().getColor(R.color.colorGenderNoselect));
                sex = "1";
                break;
            case R.id.gender_female_layout:
                gender_male_pic.setImageResource(R.mipmap.male_noselect);
                gender_male.setTextColor(getResources().getColor(R.color.colorGenderNoselect));
                gender_female_pic.setImageResource(R.mipmap.mine_female);
                gender_female.setTextColor(getResources().getColor(R.color.colorGenderFeale));
                sex = "2";
                break;
            case R.id.birthday_layout:
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        UserInfoActivity.this, getTime());
                dateTimePicKDialog.dateTimePicKDialog(birthday);
                break;
            case R.id.city_layout:
                startActivityForResult(new Intent(UserInfoActivity.this, SelectCityActivity.class), 1000);
                break;
            case R.id.done:
                if (!birthday.getText().toString().equals("请选择出生日期")) {
                    birthday_day = birthday.getText().toString();
                }
                qq_number = qq.getText().toString();
                nickname = name.getText().toString();
                if ("".equals(nickname) || "".equals(qq_number) || "".equals(province) || "".equals(birthday_day)) {
                    Toast.makeText(UserInfoActivity.this, "未输入完全", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread() {
                    public void run() {
                        HashMap<String, String> map = new HashMap<>();
                        if(is_third) {
                            map.put("account_id", account_id);
                            map.put("type", type);
                            map.put("head_pic", headpic);
                        } else {
                            map.put("mobile", mobile);
                            map.put("password", pwd);
                            map.put("code", code);
                        }
                        map.put("nickname", nickname);
                        map.put("sex", sex);
                        map.put("birthday", birthday_day);
                        map.put("country", country);
                        map.put("province", province_code);
                        map.put("city", city_code);
                        map.put("qq", qq_number);
                        map.put("longitude", cn.com.lamatech.date.Date.lng+"");
                        map.put("latitude", cn.com.lamatech.date.Date.lat+"");
                        String result;
                        if(is_third) {
                            result = ServerProxy.thirdRegister(map);
                        } else {
                            result = ServerProxy.register(map);
                        }
                        if(result == null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UserInfoActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        try {
                            JSONObject res = new JSONObject(result);
                            if(res.getInt("code") == 200) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UserInfoActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                JSONObject data = res.getJSONObject("data");
                                SharedPreferences pref = getSharedPreferences(cn.com.lamatech.date.Date.DATE, 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putLong(cn.com.lamatech.date.Date.UserInfo.LAST_LOGIN, System.currentTimeMillis());
                                editor.putString(cn.com.lamatech.date.Date.UserInfo.USER_ID, mobile);
                                editor.putString(cn.com.lamatech.date.Date.UserInfo.PWD, pwd);
//                                editor.putString(Date.UserInfo.PKID, pkid);
//                                editor.putString(Date.UserInfo.FLAG_KEY, res.getString("flagkey"));
                                editor.commit();

                                HashMap <String, Object> newmap = new HashMap<>();
                                newmap.put("user_id", data.getString("user_id"));
                                newmap.put("sex", data.getString("sex"));
                                newmap.put("phone", data.getString("account_mobile"));
                                newmap.put("name", data.getString("nickname"));
                                newmap.put("head_pic", data.getString("head_pic"));
                                newmap.put("is_vip", "0");
                                cn.com.lamatech.date.Date.mUserInfo = new cn.com.lamatech.date.Date.UserInfo(newmap);
                                if(is_third) {
                                    if("2".equals(data.getString("sex"))) {
                                        Intent intent = new Intent(UserInfoActivity.this, VideoCatchActivity.class);
                                        intent.putExtra("first", true);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                                        intent.putExtra("first", true);
                                        startActivity(intent);
                                    }
                                } else {
                                    Intent intent = new Intent(UserInfoActivity.this, HeadPicActivity.class);
                                    intent.putExtra("first", true);
                                    startActivity(intent);
                                }
                                finish();
                            } else {
                                final String msg = res.getString("msg");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();


                break;
        }
    }

    String getTime() {
//        long time = 649526434 * 1000;//long now = android.os.SystemClock.uptimeMillis();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
//        Date d1 = new Date(time);
//        String t1 = format.format(d1);
//        Log.e("seek", t1);
        return "1986年9月24日 09:00";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1000) {
            province = data.getStringExtra("province");
            city_name = data.getStringExtra("city");
            province_code = data.getStringExtra("province_code");
            city_code = data.getStringExtra("city_code");
            city.setText(province + city_name);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (qq.length() == 0 || name.length() == 0) {
            done.setBackgroundResource(R.drawable.login_background);
        } else {
            done.setBackgroundResource(R.drawable.login_ok_background);
        }

    }
}
