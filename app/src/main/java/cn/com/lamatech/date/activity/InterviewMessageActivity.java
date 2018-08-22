package cn.com.lamatech.date.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.lamatech.date.R;
import cn.com.lamatech.date.view.XListView;

public class InterviewMessageActivity extends AppCompatActivity {
    ArrayList<HashMap<String, Object>> mData = new ArrayList<>();
    XListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        LinearLayout back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        listView = findViewById(R.id.message_list);

        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {

            }
        });

        getData();
    }

    void getData() {
        mData.clear();

//        new Thread() {
//            public void run() {
//                String result = ServerProxy.getMessages(Date.mUserInfo.userid, 1+"");
//                Log.e("date", "message result is " + result);
//                /*
//                {"code":200,"data":
//                [{"message_id":1,"message":"hello，大家好",
//                "category":0,"data":"","send_time":"2018-07-31",
//                "status":""}],"msg":""}
//                 */
//                if(result == null) {
//                    return;
//                }
//                try {
//                    JSONObject res = new JSONObject(result);
//                    if(res.getInt("code") == 200) {
//                        JSONArray data = res.getJSONArray("data");
//                        for(int i=0; i<data.length(); i++) {
//                            JSONObject data_res = data.getJSONObject(i);
//                            HashMap<String, String> map = new HashMap();
//                            map.put("message_id", data_res.getString("message_id"));
//                            map.put("message", data_res.getString("message"));
//                            map.put("category", data_res.getString("category"));
//                            map.put("data", data_res.getString("data"));
//                            map.put("send_time", data_res.getString("send_time"));
//                            map.put("status", data_res.getString("status"));
//                            mData.add(map);
//                        }
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                listView.setAdapter(new MyAdapter(MessageActivity.this, mData));
//                                listView.deferNotifyDataSetChanged();
//                                listView.stopRefresh();
//                            }
//                        });
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }


    //自定义适配器
    class MyAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;

        Context context;
        private ArrayList<HashMap<String, String>> data;

        public MyAdapter(Context context, ArrayList data) {
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
            View v = layoutInflater.inflate(R.layout.message_item_layout, null);
            final ImageView iv = v.findViewById(R.id.message_pic);

            TextView tv = v.findViewById(R.id.message);
            TextView time = v.findViewById(R.id.time);
            time.setVisibility(View.GONE);
            TextView message_data = v.findViewById(R.id.data);
            message_data.setVisibility(View.GONE);
            HashMap map = data.get(position);
            tv.setText(map.get("message") + "");
            iv.setImageResource((Integer) map.get("pic"));
            return v;
        }
    }
}
