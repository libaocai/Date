package cn.com.lamatech.date.activity.rong.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lamatech.seekserverproxy.HttpConstants;
import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;
import cn.com.lamatech.date.activity.rong.FriendListAdapter;
import cn.com.lamatech.date.activity.rong.SealConst;
import cn.com.lamatech.date.activity.rong.db.Friend;
import cn.com.lamatech.date.activity.rong.server.broadcast.BroadcastManager;
import cn.com.lamatech.date.activity.rong.server.pinyin.CharacterParser;
import cn.com.lamatech.date.activity.rong.server.pinyin.PinyinComparator;
import cn.com.lamatech.date.activity.rong.server.pinyin.SideBar;
import cn.com.lamatech.date.activity.rong.server.widget.SelectableRoundedImageView;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * tab 2 通讯录的 Fragment
 * Created by Bob on 2015/1/25.
 */
public class ContactsFragment extends Fragment implements View.OnClickListener {

    private SelectableRoundedImageView mSelectableRoundedImageView;
    private TextView mNameTextView;
    private TextView mNoFriends;
    private TextView mUnreadTextView;
    private View mHeadView;
    private EditText mSearchEditText;
    private ListView mListView;
    private PinyinComparator mPinyinComparator;
    private SideBar mSidBar;

    ArrayList<Friend> friendsList = new ArrayList();
    /**
     * 中部展示的字母提示
     */
    private TextView mDialogTextView;

    private List<Friend> mFriendList;
    private List<Friend> mFilteredFriendList;
    /**
     * 好友列表的 mFriendListAdapter
     */
    private FriendListAdapter mFriendListAdapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser mCharacterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */

    private String mId;
    private String mCacheName;

    private static final int CLICK_CONTACT_FRAGMENT_FRIEND = 2;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_address, container, false);
        initView(view);
        initData();
        updateUI();
        refreshUIListener();

        getData();
        return view;
    }

    void getData() {
        new Thread() {
            public void run() {
                String result = ServerProxy.getFriendList(Date.mUserInfo.userid);
                Log.e("date", "message result is " + result);
                /*
                {
    "code": 200,
    "data": [
        {
            "user_id": 2,
            "head_pic": "/public/upload/head_pic/20180705/4e13efddacf6fee75bc5c13e8c9119ce.jpg",
            "nickname": "李四",
            "auth_video_status": 0
        }
    ],
    "msg": ""
}
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
                            Friend friend = new Friend(
                                    data_res.getString("user_id"),
                                    data_res.getString("nickname"),
                                    Uri.parse(HttpConstants.HTTP_HEAD_PIC + data_res.getString("head_pic").replace("\\", "")),
                                    data_res.getString("nickname"),
                                    null, null, null, null,
                                    CharacterParser.getInstance().getSpelling(data_res.getString("nickname")),
                                    CharacterParser.getInstance().getSpelling(data_res.getString("nickname")));
                            if (friend.getPortraitUri() == null || TextUtils.isEmpty(friend.getPortraitUri().toString())) {
//                                String portrait = getPortrait(friend);
//                                if (portrait != null) {
//                                    friend.setPortraitUri(Uri.parse(getPortrait(friend)));
//                                }
                            }


                            friendsList.add(friend);
                        }

                        // for test
//                        if(friendsList.size() == 0) {
//                            Friend friend = new Friend(
//                                    Date.mUserInfo.userid,
//                                    "作业",
//                                    Uri.parse(HttpConstants.HTTP_HEAD_PIC + Date.mUserInfo.head_pic),
//                                    "作业",
//                                    null, null, null, null,
//                                    CharacterParser.getInstance().getSpelling("作业"),
//                                    CharacterParser.getInstance().getSpelling("作业"));
//                            friendsList.add(friend);
//                        }
                        mHandler.sendEmptyMessage(0);


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


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            updateFriendsList(friendsList);
            return false;
        }
    });


    private void startFriendDetailsPage(Friend friend) {

        RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.PRIVATE, friend.getUserId(), friend.getDisplayName());

//        Intent intent = new Intent(getActivity(), UserDetailActivity.class);
//        intent.putExtra("type", CLICK_CONTACT_FRAGMENT_FRIEND);
//        intent.putExtra("friend", friend);
//        startActivity(intent);
    }

    private void initView(View view) {
        mSearchEditText = (EditText) view.findViewById(R.id.search);
        mListView = (ListView) view.findViewById(R.id.listview);
        mNoFriends = (TextView) view.findViewById(R.id.show_no_friend);
        mSidBar = (SideBar) view.findViewById(R.id.sidrbar);
        mDialogTextView = (TextView) view.findViewById(R.id.group_dialog);
        mSidBar.setTextView(mDialogTextView);
        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
        mHeadView = mLayoutInflater.inflate(R.layout.item_contact_list_header,
                                            null);
        mUnreadTextView = (TextView) mHeadView.findViewById(R.id.tv_unread);
        RelativeLayout newFriendsLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_newfriends);
        RelativeLayout groupLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_chatroom);
        RelativeLayout publicServiceLayout = (RelativeLayout) mHeadView.findViewById(R.id.publicservice);
        RelativeLayout selfLayout = (RelativeLayout) mHeadView.findViewById(R.id.contact_me_item);
        mSelectableRoundedImageView = (SelectableRoundedImageView) mHeadView.findViewById(R.id.contact_me_img);
        mNameTextView = (TextView) mHeadView.findViewById(R.id.contact_me_name);
        updatePersonalUI();
        mListView.addHeaderView(mHeadView);
        mNoFriends.setVisibility(View.VISIBLE);

        selfLayout.setOnClickListener(this);
        groupLayout.setOnClickListener(this);
        newFriendsLayout.setOnClickListener(this);
        publicServiceLayout.setOnClickListener(this);
        groupLayout.setVisibility(View.GONE);
        newFriendsLayout.setVisibility(View.GONE);
        publicServiceLayout.setVisibility(View.GONE);
        //设置右侧触摸监听
        mSidBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mFriendListAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });
    }

    private void initData() {
        mFriendList = new ArrayList<>();
        FriendListAdapter adapter = new FriendListAdapter(getActivity(), mFriendList);
        mListView.setAdapter(adapter);
        mFilteredFriendList = new ArrayList<>();
        //实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();
        mPinyinComparator = PinyinComparator.getInstance();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mDialogTextView != null) {
            mDialogTextView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr 需要过滤的 String
     */
    private void filterData(String filterStr) {
        List<Friend> filterDateList = new ArrayList<>();

        try {
            if (TextUtils.isEmpty(filterStr)) {
                filterDateList = mFriendList;
            } else {
                filterDateList.clear();
                for (Friend friendModel : mFriendList) {
                    String name = friendModel.getName();
                    String displayName = friendModel.getDisplayName();
                    if (!TextUtils.isEmpty(displayName)) {
                        if (name.contains(filterStr) || mCharacterParser.getSpelling(name).startsWith(filterStr) || displayName.contains(filterStr) || mCharacterParser.getSpelling(displayName).startsWith(filterStr)) {
                            filterDateList.add(friendModel);
                        }
                    } else {
                        if (name.contains(filterStr) || mCharacterParser.getSpelling(name).startsWith(filterStr)) {
                            filterDateList.add(friendModel);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, mPinyinComparator);
        mFilteredFriendList = filterDateList;
        mFriendListAdapter.updateListView(filterDateList);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.re_newfriends:
//                mUnreadTextView.setVisibility(View.GONE);
//                Intent intent = new Intent(getActivity(), NewFriendListActivity.class);
//                startActivityForResult(intent, 20);
//                break;
//            case R.id.re_chatroom:
//                startActivity(new Intent(getActivity(), GroupListActivity.class));
//                break;
//            case R.id.publicservice:
//                Intent intentPublic = new Intent(getActivity(), PublicServiceActivity.class);
//                startActivity(intentPublic);
//                break;
//            case R.id.contact_me_item:
//                RongIM.getInstance().startPrivateChat(getActivity(), mId, mCacheName);
//                break;
        }
    }
    public static final String UPDATE_FRIEND = "update_friend";
    public static final String UPDATE_RED_DOT = "update_red_dot";

    private void refreshUIListener() {
        BroadcastManager.getInstance(getActivity()).addAction(UPDATE_FRIEND, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getAction();
                if (!TextUtils.isEmpty(command)) {
                    updateUI();
                }
            }
        });

        BroadcastManager.getInstance(getActivity()).addAction(UPDATE_RED_DOT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getAction();
                if (!TextUtils.isEmpty(command)) {
                    mUnreadTextView.setVisibility(View.INVISIBLE);
                }
            }
        });
        BroadcastManager.getInstance(getActivity()).addAction(SealConst.CHANGEINFO, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updatePersonalUI();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            BroadcastManager.getInstance(getActivity()).destroy(UPDATE_FRIEND);
            BroadcastManager.getInstance(getActivity()).destroy(UPDATE_RED_DOT);
            BroadcastManager.getInstance(getActivity()).destroy(SealConst.CHANGEINFO);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {

    }

    private void updateFriendsList(List<Friend> friendsList) {
        //updateUI fragment初始化和好友信息更新时都会调用,isReloadList表示是否是好友更新时调用
        boolean isReloadList = false;
        if (mFriendList != null && mFriendList.size() > 0) {
            mFriendList.clear();
            isReloadList = true;
        }
        mFriendList = friendsList;
        if (mFriendList != null && mFriendList.size() > 0) {
            handleFriendDataForSort();
            mNoFriends.setVisibility(View.GONE);
        } else {
            mNoFriends.setVisibility(View.VISIBLE);
        }

        // 根据a-z进行排序源数据
        Collections.sort(mFriendList, mPinyinComparator);
        if (isReloadList) {
            mSidBar.setVisibility(View.VISIBLE);
            mFriendListAdapter.updateListView(mFriendList);
        } else {
            mSidBar.setVisibility(View.VISIBLE);
            mFriendListAdapter = new FriendListAdapter(getActivity(), mFriendList);

            mListView.setAdapter(mFriendListAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mListView.getHeaderViewsCount() > 0) {
                        startFriendDetailsPage(mFriendList.get(position - 1));
                    } else {
                        startFriendDetailsPage(mFilteredFriendList.get(position));
                    }
                }
            });


            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    Friend bean = mFriendList.get(position - 1);
                    startFriendDetailsPage(bean);
                    return true;
                }
            });
            mSearchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                    filterData(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (mListView.getHeaderViewsCount() > 0) {
                            mListView.removeHeaderView(mHeadView);
                        }
                    } else {
                        if (mListView.getHeaderViewsCount() == 0) {
                            mListView.addHeaderView(mHeadView);
                        }
                    }
                }
            });
        }
    }

    private void updatePersonalUI() {

    }

    private void handleFriendDataForSort() {
        for (Friend friend : mFriendList) {
            if (friend.isExitsDisplayName()) {
                String letters = replaceFirstCharacterWithUppercase(friend.getDisplayNameSpelling());
                friend.setLetters(letters);
            } else {
                String letters = replaceFirstCharacterWithUppercase(friend.getNameSpelling());
                friend.setLetters(letters);
            }
        }
    }

    private String replaceFirstCharacterWithUppercase(String spelling) {
        if (!TextUtils.isEmpty(spelling)) {
            char first = spelling.charAt(0);
            char newFirst = first;
            if (first >= 'a' && first <= 'z') {
                newFirst -= 32;
            }
            StringBuilder builder = new StringBuilder(String.valueOf(newFirst));
            if (spelling.length() > 1) {
                builder.append(spelling.substring(1, spelling.length()));
            }
            return builder.toString();
        } else {
            return "#";
        }
    }
}
