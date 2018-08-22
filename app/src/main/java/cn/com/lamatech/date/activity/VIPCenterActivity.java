package cn.com.lamatech.date.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;

public class VIPCenterActivity extends AppCompatActivity implements View.OnClickListener{

    TextView vip1;
    TextView vip2;
    TextView vip3;
    TextView vip4;

    TextView is_vip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vipcenter);
        LinearLayout back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView name = findViewById(R.id.name);
        name.setText(Date.mUserInfo.username);
        ImageView head_pic = findViewById(R.id.head_pic);
        head_pic.setImageBitmap(Date.mUserInfo.head_pic_bm);

        is_vip = findViewById(R.id.is_vip);
        if("1".equals(Date.mUserInfo.level)) {
            is_vip.setText("白银会员，剩余。。天");
        }
        if("2".equals(Date.mUserInfo.level)) {
            is_vip.setText("黄金会员，剩余。。天");
        }
        if("3".equals(Date.mUserInfo.level)) {
            is_vip.setText("铂金会员，剩余。。天");
        }
        if("4".equals(Date.mUserInfo.level)) {
            is_vip.setText("钻石会员，剩余。。天");
        }

        vip1 = findViewById(R.id.vip1);
        vip2 = findViewById(R.id.vip2);
        vip3 = findViewById(R.id.vip3);
        vip4 = findViewById(R.id.vip4);

        vip1.setOnClickListener(this);
        vip2.setOnClickListener(this);
        vip3.setOnClickListener(this);
        vip4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vip1:
                dialogShow(1);
                break;
            case R.id.vip2:
                dialogShow(2);
                break;
            case R.id.vip3:
                dialogShow(3);
                break;
            case R.id.vip4:
                dialogShow(4);
                break;
        }
    }


    private void dialogShow(final int level) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.vip_dialog_layout, null);
        TextView title = v.findViewById(R.id.dialog_title);
        TextView content = (TextView) v.findViewById(R.id.dialog_content);
        String vip_level = "";
        switch (level) {
            case 1:
                vip_level = "白银会员";
                break;
            case 2:
                vip_level = "黄金会员";
                break;
            case 3:
                vip_level = "铂金会员";
                break;
            case 4:
                vip_level = "钻石会员";
                break;
        }
        content.setText("确认购买" + vip_level + "?");
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               buyLevel(level);
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

    void buyLevel(final int level) {
        new Thread() {
            public void run() {
                String result = ServerProxy.changeLevel(Date.mUserInfo.userid, level+"");
                Log.e("date", "buy level result is " + result);
                try {
                    JSONObject res = new JSONObject(result);
                    if(res.getInt("code") == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VIPCenterActivity.this, "购买成功", Toast.LENGTH_SHORT).show();
                                Date.mUserInfo.level = level+"";
                                if("1".equals(Date.mUserInfo.level)) {
                                    is_vip.setText("白银会员，剩余。。天");
                                }
                                if("2".equals(Date.mUserInfo.level)) {
                                    is_vip.setText("黄金会员，剩余。。天");
                                }
                                if("3".equals(Date.mUserInfo.level)) {
                                    is_vip.setText("铂金会员，剩余。。天");
                                }
                                if("4".equals(Date.mUserInfo.level)) {
                                    is_vip.setText("钻石会员，剩余。。天");
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VIPCenterActivity.this, "购买失败，请重试", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
