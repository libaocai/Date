package cn.com.lamatech.date.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lamatech.seekserverproxy.HttpConstants;
import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;
import cn.com.lamatech.date.activity.rong.ConversationListAdapterEx;
import cn.com.lamatech.date.activity.rong.fragment.ContactsFragment;
import cn.com.lamatech.date.activity.rong.server.widget.LoadDialog;
import cn.com.lamatech.date.view.XListView;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.ContactNotificationMessage;

public class FriendsListActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener
                                                                      ,IUnReadMessageObserver {

    ArrayList<HashMap<String, String>> mData = new ArrayList<>();
    ViewPager viewPager;
    LinearLayout friend_layout;
    TextView friend;
    ImageView friend_line;

    LinearLayout attention_layout;
    TextView attention;
    ImageView attention_line;

    LinearLayout fans_layout;
    TextView fans;
    ImageView fans_line;

    private List<Fragment> mFragment = new ArrayList<>();

    /**
     * 会话列表的fragment
     */
    private ConversationListFragment mConversationListFragment = null;
    private boolean isDebug;
    private Context mContext;
    private Conversation.ConversationType[] mConversationsTypes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        LinearLayout back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView add_new_friend = findViewById(R.id.add_new_friend);
        add_new_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendsListActivity.this, SearchFriendActivity.class));
            }
        });
        viewPager = findViewById(R.id.viewpager);
        friend_layout = findViewById(R.id.friend_layout);
        friend = findViewById(R.id.friend);
        friend_line = findViewById(R.id.friend_line);

        attention_layout = findViewById(R.id.attention_layout);
        attention = findViewById(R.id.attention);
        attention_line = findViewById(R.id.attention_line);

        fans_layout = findViewById(R.id.fans_layout);
        fans = findViewById(R.id.fans);
        fans_line = findViewById(R.id.fans_line);

        friend_layout.setOnClickListener(this);
        attention_layout.setOnClickListener(this);
        fans_layout.setOnClickListener(this);

        changeTextViewColor();
        changeSelectedTabState(0);
        initMainViewPager();

        connect();
    }

    void connect() {
        Log.e("date", "token is " + Date.mUserInfo.token);
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
                        //Toast.makeText(FriendsListActivity.this, "链接服务器成功！", Toast.LENGTH_SHORT).show();

                    }
                });
            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("date" , "sucess , error is ");
                       // Toast.makeText(FriendsListActivity.this, "链接服务器失败！", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    void changeTextViewColor() {
        friend.setTextColor(Color.parseColor("#abadbb"));
        attention.setTextColor(Color.parseColor("#abadbb"));
        fans.setTextColor(Color.parseColor("#abadbb"));

        friend_line.setAlpha(0.0f);
        attention_line.setAlpha(0.0f);
        fans_line.setAlpha(0.0f);
    }

    void changeSelectedTabState(int position) {
        switch (position) {
            case 0:
                friend.setTextColor(Color.parseColor("#000000"));
                friend_line.setAlpha(1.0f);
                break;
            case 1:
                attention.setTextColor(Color.parseColor("#000000"));
                attention_line.setAlpha(1.0f);
                break;
            case 2:
                fans.setTextColor(Color.parseColor("#000000"));
                fans_line.setAlpha(1.0f);
                break;
        }
    }

    void initMainViewPager() {
        mFragment.add(new ContactsFragment());
        WebViewFragment attentionFragment = new WebViewFragment();
        attentionFragment.setUri(HttpConstants.HTTP_REQUEST + "/mobile/user/attention/" + Date.mUserInfo.userid);
        mFragment.add(attentionFragment);
        WebViewFragment fansFragment = new WebViewFragment();
        fansFragment.setUri(HttpConstants.HTTP_REQUEST + "/mobile/user/fans/" + Date.mUserInfo.userid);
        mFragment.add(fansFragment);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(this);
        initData();
    }

    void initData() {

        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
        };

        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        getConversationPush();// 获取 push 的 id 和 target
        getPushMessage();
    }

    private void getConversationPush() {
        if (getIntent() != null && getIntent().hasExtra("PUSH_CONVERSATIONTYPE") && getIntent().hasExtra("PUSH_TARGETID")) {

            final String conversationType = getIntent().getStringExtra("PUSH_CONVERSATIONTYPE");
            final String targetId = getIntent().getStringExtra("PUSH_TARGETID");


            RongIM.getInstance().getConversation(Conversation.ConversationType.valueOf(conversationType), targetId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {

                    if (conversation != null) {

                        if (conversation.getLatestMessage() instanceof ContactNotificationMessage) { //好友消息的push
                            startActivity(new Intent(FriendsListActivity.this, FriendsListActivity.class));
                        } else {
                            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon().appendPath("conversation")
                                    .appendPath(conversationType).appendQueryParameter("targetId", targetId).build();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            });
        }
    }

    /**
     * 得到不落地 push 消息
     */
    private void getPushMessage() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
            String path = intent.getData().getPath();
            if (path.contains("push_message")) {
                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                String cacheToken = sharedPreferences.getString("loginToken", "");
                if (TextUtils.isEmpty(cacheToken)) {
                    startActivity(new Intent(FriendsListActivity.this, LoginActivity.class));
                } else {
                    if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                        LoadDialog.show(mContext);
                        RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                LoadDialog.dismiss(mContext);
                            }

                            @Override
                            public void onSuccess(String s) {
                                LoadDialog.dismiss(mContext);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {
                                LoadDialog.dismiss(mContext);
                            }
                        });
                    }
                }
            }
        }
    }



    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;
            if (isDebug) {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM,
                        Conversation.ConversationType.DISCUSSION
                };

            } else {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM
                };
            }
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    void getData() {
        mData.clear();
        new Thread() {
            public void run() {
                String result = ServerProxy.getFriendList(Date.mUserInfo.userid);
                Log.e("date", "message result is " + result);
                /*
                {"code":200,"data":
                [{"message_id":1,"message":"hello，大家好",
                "category":0,"data":"","send_time":"2018-07-31",
                "status":""}],"msg":""}
                 */
                if(result == null) {
                    return;
                }
                try {
                    JSONObject res = new JSONObject(result);
                    if(res.getInt("code") == 200) {
                        JSONArray data = res.getJSONArray("data");
                        for(int i=0; i<data.length(); i++) {
                            JSONObject data_res = data.getJSONObject(i);
                            HashMap<String, String> map = new HashMap();
                            map.put("message_id", data_res.getString("message_id"));
                            map.put("message", data_res.getString("message"));
                            map.put("category", data_res.getString("category"));
                            map.put("data", data_res.getString("data"));
                            map.put("send_time", data_res.getString("send_time"));
                            map.put("status", data_res.getString("status"));
                            mData.add(map);
                        }

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                listView.setAdapter(new MyAdapter(FriendsListActivity.this, mData));
//                                listView.deferNotifyDataSetChanged();
//                                listView.stopRefresh();
//                            }
//                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onCountChanged(int count) {
//        if (count == 0) {
//            mUnreadNumView.setVisibility(View.GONE);
//        } else if (count > 0 && count < 100) {
//            mUnreadNumView.setVisibility(View.VISIBLE);
//            mUnreadNumView.setText(String.valueOf(count));
//        } else {
//            mUnreadNumView.setVisibility(View.VISIBLE);
//            mUnreadNumView.setText(R.string.no_read_message);
//        }
    }


    //自定义适配器
    class MyAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;

        Context context;
        private ArrayList<HashMap<String, String>> data;
        public MyAdapter(Context context,ArrayList data){
            this.context = context;
            this.data = data;
            layoutInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int item) {
            return data.get(item);
        }

        public long getItemId(int id) {
            return id;
        }

        //创建View方法
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = layoutInflater.inflate(R.layout.message_item_layout,null);
            final ImageView iv = v.findViewById(R.id.message_pic);
            TextView tv =  v.findViewById(R.id.message);
            TextView time = v.findViewById(R.id.time);
            TextView message_data = v.findViewById(R.id.data);
            HashMap map =  data.get(position);
            tv.setText(map.get("message")+"");
            message_data.setText(map.get("data")+"");
            time.setText(map.get("send_time")+"");
            return v;
        }
    }

    @Override
    public void onClick(View v) {
        changeTextViewColor();
        switch (v.getId()) {
            case R.id.friend_layout:
                changeTextViewColor();
                changeSelectedTabState(0);
                viewPager.setCurrentItem(0);
                break;
            case R.id.attention_layout:
                changeTextViewColor();
                changeSelectedTabState(1);
                viewPager.setCurrentItem(1);
                break;
            case R.id.fans_layout:
                changeTextViewColor();
                changeSelectedTabState(2);
                viewPager.setCurrentItem(2);
                break;
        }
    }
}
