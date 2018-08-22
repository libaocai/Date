package cn.com.lamatech.date.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;
import cn.com.lamatech.date.utils.CommonUtils;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        LinearLayout back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView version = findViewById(R.id.version);
        version.setText("V" + CommonUtils.getVerName(this));
        RelativeLayout logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShow2();
            }
        });

        RelativeLayout webviewtest = findViewById(R.id.webviewtest);
        webviewtest.setVisibility(View.GONE);
        webviewtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, WebViewTestActivity.class));
            }
        });

        LinearLayout notification_settings = findViewById(R.id.notification_settings);
        notification_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, NotificationSettingActivity.class));
            }
        });

        LinearLayout help_advice = findViewById(R.id.help_advice);
        help_advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, HelpAndAdviceActivity.class));
            }
        });


        RelativeLayout clear_catch = findViewById(R.id.clear_catch);
        clear_catch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShow();
            }
        });

    }

    private void dialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.vip_dialog_layout, null);
        TextView content = (TextView) v.findViewById(R.id.dialog_content);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        content.setText("确定清除缓存");
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView catch_size = findViewById(R.id.catch_size);
                catch_size.setText("0k");
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    private void dialogShow2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.vip_dialog_layout, null);
        TextView title = v.findViewById(R.id.dialog_title);
        title.setText("注销登录");
        TextView content = (TextView) v.findViewById(R.id.dialog_content);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        content.setText("确定注销登录么？");
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Date.mUserInfo = null;
                SharedPreferences pref = getSharedPreferences(Date.DATE, 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putLong(Date.UserInfo.LAST_LOGIN, 0);
                editor.putString(Date.UserInfo.USER_ID, "");
                editor.putString(Date.UserInfo.PWD, "");
//                                editor.putString(Date.UserInfo.PKID, pkid);
//                                editor.putString(Date.UserInfo.FLAG_KEY, res.getString("flagkey"));
                editor.putString("wopenid", "");
                editor.commit();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                setResult(199);
                finish();
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }
}
