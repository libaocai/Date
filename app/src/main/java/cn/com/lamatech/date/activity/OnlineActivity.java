package cn.com.lamatech.date.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lamatech.seekserverproxy.HttpConstants;
import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;

public class OnlineActivity extends AppCompatActivity {

    GridView gridView;
    private PullToRefreshGridView mPullRefreshGridView;
    ArrayList <HashMap<String, String>> mData = new ArrayList<>();
    TextView plane_count;
    TextView city;
    ImageView cancel;
    LinearLayout plane_layout;
    boolean isRefreshing =  false;
    int rocket_count = 0;
    void clearFilter() {
        SharedPreferences pref = getSharedPreferences(cn.com.lamatech.date.Date.DATE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Date.SORT, 0);
        editor.putInt(Date.GENDER, 0);
        editor.putString(Date.FILED, null);
        editor.putString(Date.PROVINCE_CODE, null);
        editor.putString(Date.CITY_CODE, null);
        editor.putInt(Date.VIDEO, 0);
        editor.putString(Date.AGE_L, null);
        editor.putString(Date.AGE_R, null);
        editor.putString(Date.TALL_L, null);
        editor.putString(Date.TALL_R, null);
        editor.putString(Date.EARN, null);
        editor.putString(Date.INCOME, null);

        editor.commit();

    }

    private void dialogShow2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.vip_dialog_layout, null);
        TextView title = v.findViewById(R.id.dialog_title);
        TextView content = (TextView) v.findViewById(R.id.dialog_content);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        title.setVisibility(View.GONE);
        content.setText(R.string.vip_tips);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnlineActivity.this, VIPCenterActivity.class));
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

    private void dialogShow3() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.vip_dialog_layout, null);
        TextView title = v.findViewById(R.id.dialog_title);
        TextView content = (TextView) v.findViewById(R.id.dialog_content);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        title.setVisibility(View.GONE);
        content.setText("上传视频认证成功后，方可访问");
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnlineActivity.this, VideoCatchActivity.class));
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


    private void dialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.vip_dialog_layout, null);
        TextView content = (TextView) v.findViewById(R.id.dialog_content);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        content.setText("火箭数量不够，是否购买");
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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


    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//            }
            initData();
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {


            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshGridView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        //gridView = findViewById(R.id.gview);
        city = findViewById(R.id.city);
        cancel = findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        city.setVisibility(View.GONE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(null);
                city.setText("全部");
                cancel.setVisibility(View.GONE);
                clearFilter();
            }
        });

        plane_layout = findViewById(R.id.plane_layout);
        plane_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rocket_count <= 0) {
                    dialogShow();
                    //launcherTheRocket();
                    return;
                }
                new Thread() {
                    public void run() {
                        String result = ServerProxy.setOnTop(Date.mUserInfo.userid);
                        try {
                            JSONObject res = new JSONObject(result);
                            if(res.getInt("code") == 200) {
                                rocket_count = rocket_count - 1;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(OnlineActivity.this, "置顶成功", Toast.LENGTH_SHORT).show();
                                        plane_count.setText(rocket_count+"");
                                        launcherTheRocket();
                                        initData();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });


        mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
        gridView = mPullRefreshGridView.getRefreshableView();

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                //Toast.makeText(OnlineActivity.this, "Pull Down!", Toast.LENGTH_SHORT).show();
                isRefreshing = true;
                initData();
                //new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                //Toast.makeText(OnlineActivity.this, "Pull Up!", Toast.LENGTH_SHORT).show();
                mPullRefreshGridView.onRefreshComplete();
                //new GetDataTask().execute();
            }

        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(OnlineActivity.this, UserPageActivity.class);
//                intent.putExtra("user_id", mData.get(i).get("user_id"));

                String toUserId = mData.get(i).get("user_id");

                Intent intent = new Intent(OnlineActivity.this, WebViewActivity.class);
                if(Date.mUserInfo.userid.equals(toUserId)) {
                    intent.putExtra("uri", HttpConstants.HTTP_REQUEST + "/mobile/user/myHomePage/user_id/" + Date.mUserInfo.userid);
                } else {
                    if(Date.mUserInfo.level.equals("0") && "1".equals(Date.mUserInfo.gender)) {
                        dialogShow2();
                        return;
                    }
                    if("0".equals(Date.mUserInfo.auth_video_status)&& "2".equals(Date.mUserInfo.gender)) {
                        dialogShow3();
                        return;
                    }
                    intent.putExtra("uri", HttpConstants.HTTP_REQUEST + "/mobile/user/homePage/user_id/" + Date.mUserInfo.userid
                            + "/toUserId/" + mData.get(i).get("user_id"));
                }
                startActivity(intent);
            }
        });




        LinearLayout filter = findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(OnlineActivity.this, FilterActivity.class), 99);
            }
        });

        TextView city = findViewById(R.id.city);
        city.setText(Date.city.equals("")?"北京":Date.city);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(OnlineActivity.this, FilterActivity.class), 99);
            }
        });

        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date.mUserInfo = null;
                SharedPreferences pref = getSharedPreferences(Date.DATE, 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putLong(Date.UserInfo.LAST_LOGIN, 0);
                editor.putString(Date.UserInfo.USER_ID, "");
                editor.putString(Date.UserInfo.PWD, "");
//                                editor.putString(Date.UserInfo.PKID, pkid);
//                                editor.putString(Date.UserInfo.FLAG_KEY, res.getString("flagkey"));
                editor.commit();
                startActivity(new Intent(OnlineActivity.this, LoginActivity.class));
                finish();
            }
        });
        plane_count = findViewById(R.id.plane_count);
        getPlaneCount();
        /* for test
        HashMap <String, String> map = new HashMap<>();
        map.put("sex", 1 + "");
        map.put("order_type", 1+"");
        map.put("longitude", Date.lng+"");
        map.put("latitude", Date.lat+"");
        map.put("province", "110000");
        map.put("city", "110100");
        //map.put("auth_video_status", 1+"");
        map.put("height_l", 150+"");
        map.put("height_r", 180+"");
        map.put("age_l", 19+"");
        map.put("age_r", 30+"");
        getData(map);
        */
       initData();

    }

    void initData() {
        HashMap <String, String> map = new HashMap<>();
        SharedPreferences pref = getSharedPreferences(cn.com.lamatech.date.Date.DATE, 0);

        int sort = pref.getInt(Date.SORT, 0);
        if(sort != 0) {
            map.put("order_type", sort+"");
        }
        int gender = pref.getInt(Date.GENDER, -1);
        if(gender != -1) {
            map.put("sex", gender + "");
        }
        String province_code = pref.getString(Date.PROVINCE_CODE, null);
        String field = pref.getString(Date.FILED, null);
        if(province_code != null) {
            map.put("province", province_code);
            city.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            if(gender == 1) {
                field += "·男";
            } else if (gender == 2) {
                field += "·女";
            }
            city.setText(field);
        }

        if(sort == 2) {
            city.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            if(gender == 1) {
                city.setText("附近" +"·男");
            } else if (gender == 2) {
                city.setText("附近" + "·女");
            } else {
                city.setText("附近");
            }
            map.put("longitude", Date.lng+"");
            map.put("latitude", Date.lat+"");
        }
        String city_code = pref.getString(Date.CITY_CODE, null);
        if(city_code != null) {
            map.put("city", city_code);
        }
        int video = pref.getInt(Date.VIDEO, 0);
        if(video == 1) {
            map.put("auth_video_status", 1+"");
        }
        String age_l = pref.getString(Date.AGE_L, null);
        if(age_l != null) {
            map.put("age_l", age_l);
        }
        String age_r = pref.getString(Date.AGE_R, null);
        if(age_r != null) {
            map.put("age_r", age_r);
        }
        String tall_l = pref.getString(Date.TALL_L, null);
        if(tall_l != null) {
            map.put("height_l", tall_l.replace("cm", ""));
        }
        String tall_r = pref.getString(Date.TALL_R, null);
        if(tall_r != null) {
            map.put("height_r", tall_r.replace("cm", ""));
        }
        String income = pref.getString(Date.INCOME, null);
        if(income != null) {
            map.put("income", income);
        }
//        String satisfactory = pref.getString(Date.SATISFACTORY, null);
//        if(satisfactory != null) {
//            map.put("satisfactory_parts", satisfactory);
//        }

        if(map.size() == 0) {
            getData(null);
        } else {
            getData(map);
        }
    }


    void getPlaneCount() {
        new Thread() {
            public void run() {
                String result = ServerProxy.getPlaneCount(Date.mUserInfo.userid);
                Log.e("date", "get plane count result is " + result);
                if(result == null) {
                    return;
                }
                try {
                    JSONObject res = new JSONObject(result);
                    if(res.getInt("code") == 200) {
                        JSONObject data = res.getJSONObject("data");
                        final String count = data.getString("num");
                        try {
                            rocket_count = Integer.parseInt(count);
                        } catch (Exception e) {
                            rocket_count = 0;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                plane_count.setText(count);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }


    void getData(final HashMap map) {
        mData.clear();
        new Thread() {
            public void run() {
//                for(int i=0; i< 200; i++) {
//                    HashMap map = new HashMap();
//                    map.put("photo", "https://b-ssl.duitang.com/uploads/item/201609/26/20160926124027_vxRkt.jpeg");
//                    map.put("name", "约玩" + i);
//                    map.put("time", "刚刚");
//                    map.put("gender", "1");
//                    map.put("video", "1");
//                    mData.add(map);
//                }
                String result = ServerProxy.getOnlineFriends(map);
                Log.e("date", "get online friends result is " + result);
                if(result == null) {
                    return;
                }
                try {
                    JSONObject res = new JSONObject(result);
                    if(res.getInt("code") == 200) {
                        JSONArray data = res.getJSONArray("data");
                        for(int i=0; i<data.length(); i++) {
                            HashMap datamap = new HashMap();
                            JSONObject dataObject = data.getJSONObject(i);
                            datamap.put("head_pic", HttpConstants.HTTP_HEAD_PIC + dataObject.getString("head_pic").replace("\\", ""));
                            datamap.put("name", dataObject.getString("nickname"));
                            datamap.put("active_time", dataObject.getString("active_time"));
                            datamap.put("user_id", dataObject.getString("user_id"));
                            try {
                                datamap.put("auth_video_status", dataObject.getString("auth_video_status"));
                            } catch (Exception e) {
                                datamap.put("auth_video_status","");
                            }
                            try {
                                datamap.put("longitude", dataObject.getString("longitude"));
                                datamap.put("latitude", dataObject.getString("latitude"));
                                datamap.put("distance", dataObject.getString("distance"));
                            } catch (Exception e) {

                            }
                            if(i%3 == 1) {
                                mData.add(i-1, datamap);
                            } else {
                                mData.add(datamap);
                            }
                        }

                        if(mData.size() == 1) {
                            mData.add(0, null);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isRefreshing) {
                            mPullRefreshGridView.onRefreshComplete();
                            isRefreshing = false;
                        }
                        gridView.setAdapter(new MyAdapter(OnlineActivity.this, mData));
                    }
                });
            }
        }.start();
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
            View v = layoutInflater.inflate(R.layout.item_grideview_layout,null);
            LinearLayout gridview = v.findViewById(R.id.gridview);
            final ImageView iv = v.findViewById(R.id.iv_gridView_item);
            ImageView is_fist = v.findViewById(R.id.is_first);
            TextView tv =  v.findViewById(R.id.tv_gridView_item);
            ImageView space1 = v.findViewById(R.id.space1);
            ImageView space2 = v.findViewById(R.id.space2);
            TextView time = v.findViewById(R.id.time);
            ImageView video = v.findViewById(R.id.video);
            HashMap map =  data.get(position);

            if(map == null) {
                gridview.setVisibility(View.INVISIBLE);
                return v;
            }
            if(position == 1) {
                is_fist.setVisibility(View.VISIBLE);
            }
            if(!"2".equals(map.get("auth_video_status"))) {
                video.setVisibility(View.GONE);
            }

            try {
                long active_time = Long.parseLong(map.get("active_time")+"");
                long current_time = System.currentTimeMillis()/1000;
                long pass = current_time - active_time;

                long days = pass/86400;
                long hours = (pass%86400)/3600;
                long mins = ((pass%86400)%3600)/60;
                long secs = (((pass%86400)%3600)%60);

                if(days > 0) {
                    time.setText(days + "天前");
                } else if(hours > 0) {
                    time.setText(hours + "小时前");
                } else if (mins > 0) {
                    time.setText(mins + "分钟前");
                } else {
                    time.setText("刚刚");
                }
            } catch (Exception e) {
                e.printStackTrace();
                time.setText("");
            }

            if(map.containsKey("distance")) {
                time.setText(map.get("distance")+"");
            }

            if(position%3 == 0 || position%3 == 2) {
                space2.setVisibility(View.GONE);
            } else {
                space1.setVisibility(View.GONE);
                space2.setVisibility(View.VISIBLE);
            }

            if(HttpConstants.HTTP_HEAD_PIC.equals(map.get("head_pic"))) {
                iv.setImageResource(R.mipmap.mine_head_pic);
            } else {
                Glide.with(context).load(map.get("head_pic")).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        Log.e("date", "glide resource is " + resource);
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
//            Glide.with(context).load(map.get("photo")).into(new SimpleTarget<GlideDrawable>() {
//                @Override
//                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                    iv.setImageDrawable(resource);
//                }
//            });


            tv.setText(map.get("name")+"");
            return v;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 99) {
            Bundle bundle = data.getExtras();
            SerializableMap serializableMap = (SerializableMap) bundle.get("map");
            Map<String ,String> map = serializableMap.getMap();

            SharedPreferences pref = getSharedPreferences(cn.com.lamatech.date.Date.DATE, 0);
            int gender = pref.getInt(Date.GENDER, -1);
            String province_code = pref.getString(Date.PROVINCE_CODE, null);
            String field = pref.getString(Date.FILED, null);
            int sort = pref.getInt(Date.SORT, 0);
            if(province_code != null) {
                city.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                if(gender == 1) {
                    field += "·男";
                } else if (gender == 2) {
                    field += "·女";
                }
                city.setText(field);
            }
            if(sort == 2) {
                city.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                if(gender == 1) {
                    city.setText("附近" +"·男");
                } else if (gender == 2) {
                    city.setText("附近" + "·女");
                } else {
                    city.setText("附近");
                }
            }

            getData((HashMap) map);
        }
    }

    private Handler mHandler = new Handler();

    private void launcherTheRocket() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                final View rocket = findViewById(R.id.rocket);
                rocket.setVisibility(View.VISIBLE);
                Animation rocketAnimation = AnimationUtils.loadAnimation(
                        getApplicationContext(), R.anim.rocket);
                rocketAnimation
                        .setAnimationListener(new VisibilityAnimationListener(
                                rocket));
                rocket.startAnimation(rocketAnimation);

//                final View cloud = findViewById(R.id.cloud);
//                Animation cloudAnimation = AnimationUtils.loadAnimation(
//                        getApplicationContext(), R.anim.cloud);
//                cloudAnimation
//                        .setAnimationListener(new VisibilityAnimationListener(
//                                cloud));
//                cloud.startAnimation(cloudAnimation);
//
//                final View launcher = findViewById(R.id.launcher);
//                Animation launcherAnimation = AnimationUtils.loadAnimation(
//                        getApplicationContext(), R.anim.launcher);
//                launcherAnimation
//                        .setAnimationListener(new VisibilityAnimationListener(
//                                launcher));
//                launcher.startAnimation(launcherAnimation);

            }
        }, 150);

    }

    public class VisibilityAnimationListener implements Animation.AnimationListener {

        private View mVisibilityView;

        public VisibilityAnimationListener(View view) {
            mVisibilityView = view;
        }

        public void setVisibilityView(View view) {
            mVisibilityView = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (mVisibilityView != null) {
                mVisibilityView.setVisibility(View.VISIBLE);
                // mVisibilityView.setVisibility(View.GONE);
            }


        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mVisibilityView != null) {
                mVisibilityView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
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
            Toast.makeText(OnlineActivity.this, "再按一次退出美丽约", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

}
