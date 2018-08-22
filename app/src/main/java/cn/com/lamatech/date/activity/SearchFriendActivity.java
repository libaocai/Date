package cn.com.lamatech.date.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;
import cn.com.lamatech.date.activity.rong.db.Friend;
import cn.com.lamatech.date.activity.rong.server.widget.LoadDialog;
import cn.com.lamatech.date.activity.rong.server.widget.SelectableRoundedImageView;

public class SearchFriendActivity extends AppCompatActivity {


    private EditText mEtSearch;
    private LinearLayout searchItem;
    private TextView searchName;
    private SelectableRoundedImageView searchImage;
    private String mPhone;
    private String addFriendMessage;
    private String mFriendId;

    private Friend mFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        setTitle((R.string.search_friend));

        mEtSearch = (EditText) findViewById(R.id.search_edit);
        searchItem = (LinearLayout) findViewById(R.id.search_result);
        searchName = (TextView) findViewById(R.id.search_name);
        searchImage = (SelectableRoundedImageView) findViewById(R.id.search_header);
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 8) {
                    new Thread() {
                        public void run() {

                            String result = ServerProxy.searchFriend(Date.mUserInfo.userid, mEtSearch.getText().toString());
                            if(result == null) {
                                return;
                            }
                            /*
                            {
    "code": 200,
    "data": {
        "user_id": 1,
        "head_pic": "/public/upload/head_pic/20180810/da4fffd308f3b3b0ee5ca05005780d8f.jpg",
        "nickname": "李雷雷",
        "sex": 1,
        "age": 0,
        "signature": "你爱我，我就爱你",
        "auth_video_status": 2,
        "attention": 0
    },
    "msg": ""
}
                             */

                            try {
                                JSONObject res = new JSONObject(result);
                                if(res.getInt("code") == 200) {
                                    final String nickname = res.getJSONObject("data").getString("nickname");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            searchItem.setVisibility(View.VISIBLE);
                                            searchName.setText(nickname);
                                        }
                                    });
                                } else {
                                    final String msg = res.getString("msg");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SearchFriendActivity.this,msg,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } else {
                    searchItem.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
