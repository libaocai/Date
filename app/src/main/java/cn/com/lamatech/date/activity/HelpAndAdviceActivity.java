package cn.com.lamatech.date.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.lamatech.date.R;

public class HelpAndAdviceActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> mData = new ArrayList<>();
    ListView questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_advice);
        LinearLayout back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        questions = findViewById(R.id.questions);
        getData();
        questions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HelpAndAdviceActivity.this, AnswerActivity.class);
                intent.putExtra("question", mData.get(i).get("title"));
                intent.putExtra("answer", mData.get(i).get("content"));
                intent.putExtra("link", mData.get(i).get("link"));
                startActivity(intent);
            }
        });

        RelativeLayout advice = findViewById(R.id.advice);
        advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpAndAdviceActivity.this, AdviceActivity.class));
            }
        });
    }


    void getData() {
        mData.clear();
        new Thread() {
            public void run() {
                String result = ServerProxy.getHelp();
                Log.e("date", "help result is " + result);
                /*
                 {"code":200,"data":[{"article_id":1,
                 "title":"怎样修改自己的资料？","description":"",
                 "content":"<p>在【我的】页面，点击自己的头像进入主页，向下滑动到资料表单，点击即可修改对应的内容。<\/p>",
                 "link":"\/mobile\/Article\/questionDetail?article_id=1"}]
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
                            map.put("article_id", data_res.getString("article_id"));
                            map.put("title", data_res.getString("title"));
                            map.put("content", data_res.getString("content"));
                            map.put("link", data_res.getString("link").replace("\\", ""));
                            mData.add(map);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                questions.setAdapter(new MyAdapter(HelpAndAdviceActivity.this, mData));
                                questions.deferNotifyDataSetChanged();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
            View v = layoutInflater.inflate(R.layout.help_item_layout,null);
            TextView title =  v.findViewById(R.id.title);
            HashMap map =  data.get(position);
            title.setText(map.get("title")+"");

            return v;
        }
    }
}
