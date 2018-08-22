package cn.com.lamatech.date.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;

public class SelectCityActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ArrayList province = new ArrayList();
    ArrayList province_code = new ArrayList();

    HashMap<String, ArrayList> cityMap = new HashMap<>();
    HashMap<String, ArrayList> city_codeMap = new HashMap<>();

    HashMap<String, ArrayList> districtMap = new HashMap<>();

    ListView list_view;

    String province_select = "";
    String province_code_select = "";
    String city_select = "";
    String city_code_select = "";
    String district_select = "";
    LinearLayout gps_location;
    TextView location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        LinearLayout back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gps_location = findViewById(R.id.gps_location);
        location = findViewById(R.id.location);
        if(!"".equals(Date.province) || !"".equals(Date.city)) {
            location.setText(Date.province+Date.city);
        }
        gps_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"".equals(Date.province) || !"".equals(Date.city)) {
                    location.setText(Date.province+Date.city);
                }

                if(!"无法定位到当前的位置".equals(location.getText().toString())) {
                    Intent intent = new Intent();
                    intent.putExtra("province", Date.province);
                    intent.putExtra("city", Date.city);
                    intent.putExtra("province_code", Date.province_code);
                    intent.putExtra("city_code", Date.city_code);
                    setResult(1000,intent );
                    finish();
                }
            }
        });

        list_view = findViewById(R.id.list_view);
        list_view.setOnItemClickListener(this);
        new Thread() {
            public void run() {
                String result = ServerProxy.getLocationsJson();
                if(result == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SelectCityActivity.this, "获取列表失败，请返回重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    JSONObject res = new JSONObject(result);
                    JSONArray data = res.getJSONArray("data");
                    for(int i= 0; i<data.length(); i++) {
                        JSONObject provinceObject = data.getJSONObject(i);
                        province.add(provinceObject.getString("name"));
                        province_code.add(provinceObject.getString("code"));
                        ArrayList city = new ArrayList();
                        ArrayList city_code = new ArrayList();

                        try {
                            JSONArray cityArray = provinceObject.getJSONArray("sub");
                            for (int j = 0; j < cityArray.length(); j++) {
                                JSONObject cityObject = cityArray.getJSONObject(j);
                                city.add(cityObject.getString("name"));
                                city_code.add(cityObject.get("code"));
                                ArrayList district = new ArrayList();
                                try {
                                    JSONArray districtArray = cityObject.getJSONArray("sub");
                                    for (int h = 0; h < districtArray.length(); h++) {
                                        JSONObject districtfObject = districtArray.getJSONObject(h);
                                        district.add(districtfObject.getString("name"));
                                    }
                                    districtMap.put(provinceObject.getString("name") + cityObject.getString("name"), district);
                                } catch (JSONException e) {
                                    Log.e("date", "city is " + cityObject.getString("name"));
                                    districtMap.put(provinceObject.getString("name") + cityObject.getString("name"), null);
                                    e.printStackTrace();
                                }
                            }

                            cityMap.put(provinceObject.getString("name"), city);
                            city_codeMap.put(provinceObject.getString("code"), city_code);
                        } catch (JSONException e) {
                            Log.e("date", "province is " + provinceObject.getString("name"));
                            cityMap.put(provinceObject.getString("name"), null);
                            city_codeMap.put(provinceObject.getString("code"), null);
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendEmptyMessage(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            Log.e("date", "province size is " + province.size());
            list_view.setAdapter(new MyAdapter(SelectCityActivity.this, province));
            list_view.deferNotifyDataSetChanged();
            return false;
        }
    });

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if("".equals(province_select)) {
            province_select = (String) province.get(i);
            province_code_select = province_code.get(i)+"";
            Log.e("date", "city map is " + cityMap.get(province_select));
            if(cityMap.get(province_select) == null) {
                Intent intent = new Intent();
                intent.putExtra("province", province_select);
                intent.putExtra("city", "");
                intent.putExtra("province_code", province_code_select);
                intent.putExtra("city_code", "");
                setResult(1000,intent );
                finish();
                return;
            }
            list_view.setAdapter(new MyAdapter(SelectCityActivity.this, cityMap.get(province_select)));
            list_view.deferNotifyDataSetChanged();
        } else if("".equals(city_select)) {
            city_select = (String) cityMap.get(province_select).get(i);
            city_code_select = (String) city_codeMap.get(province_code_select).get(i);
            //if(districtMap.get(province_select+city_select) == null) {
                Intent intent = new Intent();
                intent.putExtra("province", province_select);
                intent.putExtra("city", city_select);
                intent.putExtra("province_code", province_code_select);
                intent.putExtra("city_code", city_code_select);
                setResult(1000,intent );
                finish();
//                return;
//            }
//            list_view.setAdapter(new MyAdapter(SelectCityActivity.this, districtMap.get(province_select+city_select)));
//            list_view.deferNotifyDataSetChanged();

        }
        /*else {
            district_select = (String) districtMap.get(province_select+city_select).get(i);
            Intent intent = new Intent();
            intent.putExtra("province", province_select);
            intent.putExtra("city", city_select+district_select);
            intent.putExtra("province_code", province_code_select);
            intent.putExtra("city_code", city_code_select);
            setResult(1000,intent );
            finish();
        }*/
    }

    //自定义适配器
    class MyAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;

        Context context;
        private ArrayList< String> data;
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
            View v = layoutInflater.inflate(R.layout.location_list_layout,null);
            TextView tv =  v.findViewById(R.id.text);

            tv.setText(data.get(position)+"");
            return v;
        }
    }
}
