package cn.com.lamatech.date.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lamatech.seekserverproxy.HttpConstants;
import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;

public class MineActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout video_auth;
    LinearLayout id_auth;
    ImageView head_pic;
    TextView nick;
    TextView number;
    RelativeLayout settings;
    RelativeLayout friends;
    RelativeLayout message_layout;
    RelativeLayout visitor_layout;
    RelativeLayout head_layout;
    RelativeLayout comment_layout;
    RelativeLayout my_count_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        head_pic = findViewById(R.id.head_pic);
        if(Date.mUserInfo != null && Date.mUserInfo.head_pic_bm != null){
            head_pic.setImageBitmap(Date.mUserInfo.head_pic_bm);
        }
        head_pic.setOnClickListener(this);
        head_layout = findViewById(R.id.head_layout);
        head_layout.setOnClickListener(this);
        video_auth = findViewById(R.id.video_auth);
        video_auth.setOnClickListener(this);
        id_auth = findViewById(R.id.id_auth);
        id_auth.setOnClickListener(this);

        nick = findViewById(R.id.nick);
        number = findViewById(R.id.number);
        if(Date.mUserInfo != null) {
            nick.setText(Date.mUserInfo.username);
            number.setText(Date.mUserInfo.uuid);
        } else {
            nick.setText("未登录");
            number.setVisibility(View.INVISIBLE);
        }

        settings = findViewById(R.id.settings_layout);
        settings.setOnClickListener(this);

        friends = findViewById(R.id.friends);
        friends.setOnClickListener(this);
        message_layout = findViewById(R.id.message_layout);
        message_layout.setOnClickListener(this);
        visitor_layout = findViewById(R.id.visitor_layout);
        visitor_layout.setOnClickListener(this);
        my_count_layout = findViewById(R.id.my_count_layout);
        my_count_layout.setOnClickListener(this);

        comment_layout = findViewById(R.id.comment_layout);
        comment_layout.setOnClickListener(this);
        new Thread() {
            public void run() {
                String result = ServerProxy.getVisitors(Date.mUserInfo.userid, "1", "1");
                Log.e("date", "get visitors is " + result);
            }
        }.start();

        new Thread() {
            public void run() {
                String result = ServerProxy.getUserInfo(Date.mUserInfo.userid);
                Log.e("date", "get userinfo is " + result);
                if(result == null) {
                    return;
                }
                /*{
    "code": 200,
    "data": {
        "user_id": 1,
        "uuid": 98502198,
        "account_mobile": "15201110059",
        "email": "",
        "sex": 1,
        "birthday": "",
        "age": 0,
        "height": "",
        "weight": "",
        "user_money": "0.00",
        "frozen_money": "0.00",
        "distribut_money": "0.00",
        "underling_number": 0,
        "points": 0,
        "reg_time": 1530696947,
        "last_login": 1530696947,
        "last_ip": "",
        "qq": "",
        "mobile_validated": 0,
        "oauth": "",
        "openid": "",
        "unionid": "",
        "head_pic": "/public/upload/head_pic/20180726/e82f48b2cc32fc10928df5ef336ba201.jpg",
        "province": 370000,
        "city": 370100,
        "district": 0,
        "email_validated": 0,
        "nickname": "李雷雷",
        "level": 1,
        "discount": "1.00",
        "total_amount": "0.00",
        "is_lock": 0,
        "token": "84bde3d9850efbdb6fcf2f8567b83ee2",
        "message_mask": 63,
        "push_id": "",
        "distribut_level": 0,
        "is_vip": 1,
        "satisfactory_parts": "",
        "longitude": "117.2234550",
        "latitude": "36.7085820",
        "auth_video_status": 2,
        "auth_identity_status": 0,
        "auth_profession_status": 1,
        "active_time": 1530969326,
        "is_line": 1,
        "income": 0,
        "rockets": 0,
        "flower_num": 97,
        "signature": "你爱我，我就爱你",
        "emotion_status": "",
        "permanent_province": "",
        "profession": "",
        "permanent_city": "",
        "phoneOwechat": "",
        "rongyun_token": "SBJEHEXC0qjIkGN5tevHJDoJzC1b/p237t784hHIriSpKf1+aM8he6w6MY6cgbuhxWQ3HnSPi1Cu3KAoX3U5K0t/aHPRJmGC",
        "sort": ""
    },
    "msg": ""
}

                 */

                try {
                    JSONObject res = new JSONObject(result);
                    if(res.getInt("code") == 200) {
                        JSONObject data = res.getJSONObject("data");
                        final String nickname = data.getString("nickname");
                        final String uuid = data.getString("uuid");
                        final int auth_video_status = data.getInt("auth_video_status");
                        final int auth_identity_status = data.getInt("auth_identity_status");
                        final int auth_profession_status = data.getInt("auth_profession_status");
                        Log.e("date", "auth_identity_status is " + auth_identity_status);
                        runOnUiThread(new Runnable() {
                            @SuppressLint("ResourceType")
                            @Override
                            public void run() {
                                nick.setText(nickname);
                                number.setText(uuid);
                                if(auth_video_status == 2) {
                                    TextView video_ok = findViewById(R.id.video_ok);
                                    ImageView video_pic = findViewById(R.id.video_pic);
                                    video_ok.setText("视频已认证");
                                    video_pic.setImageResource(R.mipmap.video_auth_ok);
                                }
                                if(auth_identity_status == 2) {
                                    TextView id_auth_ok = findViewById(R.id.id_auth_ok);
                                    ImageView id_pic = findViewById(R.id.id_auth_pic);
                                    id_auth_ok.setText("身份已认证");
                                    id_pic.setImageResource(R.mipmap.id_auth_ok);
                                }

                                if(auth_profession_status == 2) {
                                    TextView job_auth_ok = findViewById(R.id.job_auth_ok);
                                    ImageView job_pic = findViewById(R.id.job_auth_pic);
                                    job_auth_ok.setText("身份已认证");
                                    job_pic.setImageResource(R.id.job_auth_pic);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_pic:
            case R.id.head_layout:
                Intent intent = new Intent(MineActivity.this, WebViewActivity.class);
                intent.putExtra("uri", HttpConstants.HTTP_REQUEST + "/mobile/user/myHomePage/user_id/" + Date.mUserInfo.userid);
                startActivityForResult(intent, 200);
                break;
            case R.id.video_auth:
                Intent video_intent = new Intent(MineActivity.this, VideoCatchActivity.class);
                startActivityForResult(video_intent, 99);
                break;
            case R.id.id_auth:
                Intent id_intent = new Intent(MineActivity.this, IDAuthActivity.class);
                startActivityForResult(id_intent, 999);
                break;
            case R.id.settings_layout:
                Intent settings_intent = new Intent(MineActivity.this, SettingsActivity.class);
                startActivityForResult(settings_intent, 199);
                break;
            case R.id.friends:
                Intent friend = new Intent(MineActivity.this, FriendsListActivity.class);
                friend.putExtra("uri", HttpConstants.HTTP_REQUEST + "/mobile/user/friend/user_id/" + Date.mUserInfo.userid);
                startActivity(friend);
                break;
            case R.id.message_layout:
                Intent message = new Intent(MineActivity.this, MessageActivity.class);
                startActivity(message);
                break;
            case R.id.visitor_layout:
                Intent visitor = new Intent(MineActivity.this, WebViewActivity.class);
                visitor.putExtra("uri", HttpConstants.HTTP_REQUEST + "/mobile/user/visitor/user_id/" + Date.mUserInfo.userid + "/type/1/page/1");
                startActivity(visitor);
                break;
            case R.id.comment_layout:
                Intent comment = new Intent(MineActivity.this, WebViewActivity.class);
                comment.putExtra("uri", HttpConstants.HTTP_REQUEST + "/mobile/user/comment/user_id" + Date.mUserInfo.userid + "/type/1/page/1");
                startActivity(comment);
                break;
            case R.id.my_count_layout:
                startActivity(new Intent(MineActivity.this, VIPCenterActivity.class));
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200) {
            head_pic = findViewById(R.id.head_pic);
            if(Date.mUserInfo != null && Date.mUserInfo.head_pic_bm != null){
                head_pic.setImageBitmap(Date.mUserInfo.head_pic_bm);
            }
        }

        if(resultCode == 199) {
            finish();
        }
    }

    //退出时的时间
    private long mExitTime;
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MineActivity.this, "再按一次退出美丽约", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
