package cn.com.lamatech.date.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;
import cn.com.lamatech.date.view.WheelView;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener{


    LinearLayout nose_layout;
    TextView nose;
    LinearLayout shoulder_layout;
    TextView shoulder;
    LinearLayout rich_man_layout;
    TextView rich_man;
    LinearLayout hand_layout;
    TextView hand;
    LinearLayout voice_layout;
    TextView voice;
    LinearLayout leg_layout;
    TextView leg;
    LinearLayout eye_layout;
    TextView eye;
    LinearLayout pecs_layout;
    TextView pecs;
    LinearLayout lip_layout;
    TextView lip;
    LinearLayout skin_layout;
    TextView skin;
    LinearLayout nose_bridge_layout;
    TextView nose_bridge;
    LinearLayout collarbone_layout;
    TextView collarbone;
    LinearLayout abdomen_layout;
    TextView abdomen;
    LinearLayout chest_layout;
    TextView chest;
    LinearLayout whiskers_layout;
    TextView whiskers;
    LinearLayout giggle_layout;
    TextView giggle;
    LinearLayout dimple_layout;
    TextView dimple;
    LinearLayout smile_layout;
    TextView smile;
    LinearLayout waist_layout;
    TextView waist;
    LinearLayout waist_muscle_layout;
    TextView waist_muscle;
    LinearLayout ass_layout;
    TextView ass;




    RelativeLayout age_layout;
    RelativeLayout high_layout;
    RelativeLayout earn_layout;
    TextView new_action;
    TextView near;
    TextView gender_all;
    TextView gender_male;
    TextView gender_female;
    RelativeLayout field;
    RelativeLayout country;
    ImageView country_line;
    ImageView field_line;

    TextView city;
    Switch s_v;
    TextView search;
    TextView earn_text;
    TextView age;
    TextView tall;

    int age_l_index = 0;
    int age_r_index = 1;
    int tall_l_index = 0;
    int tall_r_index = 1;

    int sort = 0;
    int gender = 0;
    String province = null;
    String province_code = null;
    String city_name;
    String city_code = null;
    String earn = null;
    String income = null;
    String age_l = null;
    String age_r = null;

    String tall_l = null;
    String tall_r = null;

    String satisfactory = null;

    private static final String[] PLANETS = new String[]{"不限"," 3000元以上/月", "5000元以上/月", "10000以上/月", "20000以上/月", "50万以上/年收入",
    "100万以上/年", "保密"};
    private static final String[] AGES = new String[]{"不限","18", "19", "20", "21", "22","23", "24", "25", "26", "27", "28", "29", "30"
    , "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
            "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",};
    private static final String[] TALLS = new String[]{"不限", "150cm", "151cm", "152cm", "153cm", "154cm", "155cm","156cm", "157cm", "158cm", "159cm",
            "160cm", "161cm", "162cm", "163cm", "164cm", "165cm","166cm", "167cm", "168cm", "169cm",
            "170cm", "171cm", "172cm", "173cm", "174cm", "175cm","166cm", "177cm", "178cm", "179cm",
            "180cm", "181cm", "182cm", "183cm", "184cm", "185cm","186cm", "187cm", "188cm", "189cm",
            "190cm", "191cm", "192cm", "193cm", "194cm", "195cm","196cm", "197cm", "198cm", "199cm",
            "200cm"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        LinearLayout back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initViews();
    }

    void initViews() {
        nose_layout = findViewById(R.id.nose_layout);
        nose = findViewById(R.id.nose);

        shoulder_layout = findViewById(R.id.shoulder_layout);
        shoulder = findViewById(R.id.shoulder);
        rich_man_layout = findViewById(R.id.rich_man_layout);
        rich_man = findViewById(R.id.rich_man);
        hand_layout = findViewById(R.id.hand_layout);
        hand = findViewById(R.id.hand);
        voice_layout = findViewById(R.id.voice_layout);
        voice = findViewById(R.id.voice);
        leg_layout = findViewById(R.id.leg_layout);
        leg = findViewById(R.id.leg);
        eye_layout = findViewById(R.id.eye_layout);
        eye = findViewById(R.id.eye);
        pecs_layout = findViewById(R.id.pecs_layout);
        pecs = findViewById(R.id.pecs);
        lip_layout = findViewById(R.id.lip_layout);
        lip = findViewById(R.id.lip);
        skin_layout = findViewById(R.id.skin_layout);
        skin = findViewById(R.id.skin);
        nose_bridge_layout = findViewById(R.id.nose_bridge_layout);
        nose_bridge = findViewById(R.id.nose_bridge);
        collarbone_layout = findViewById(R.id.collarbone_layout);
        collarbone = findViewById(R.id.collarbone);
        abdomen_layout = findViewById(R.id.abdomen_layout);
        abdomen = findViewById(R.id.abdomen);
        chest_layout = findViewById(R.id.chest_layout);
        chest = findViewById(R.id.chest);
        whiskers_layout = findViewById(R.id.whiskers_layout);
        whiskers = findViewById(R.id.whiskers);
        giggle_layout = findViewById(R.id.giggle_layout);
        giggle = findViewById(R.id.giggle);
        dimple_layout = findViewById(R.id.dimple_layout);
        dimple = findViewById(R.id.dimple);
        smile_layout = findViewById(R.id.smile_layout);
        smile = findViewById(R.id.smile);
        waist_layout = findViewById(R.id.waist_layout);
        waist = findViewById(R.id.waist);
        waist_muscle_layout = findViewById(R.id.waist_muscle_layout);
        waist_muscle = findViewById(R.id.waist_muscle);
        ass_layout = findViewById(R.id.ass_layout);
        ass = findViewById(R.id.ass);
        nose.setOnClickListener(this);
        nose_layout.setOnClickListener(this);
        shoulder_layout.setOnClickListener(this);
        rich_man_layout.setOnClickListener(this);
        hand_layout.setOnClickListener(this);
        voice_layout.setOnClickListener(this);
        leg_layout.setOnClickListener(this);
        eye_layout.setOnClickListener(this);
        pecs_layout.setOnClickListener(this);
        lip_layout.setOnClickListener(this);
        skin_layout.setOnClickListener(this);
        nose_bridge_layout.setOnClickListener(this);
        collarbone_layout.setOnClickListener(this);
        abdomen_layout.setOnClickListener(this);
        chest_layout.setOnClickListener(this);
        whiskers_layout.setOnClickListener(this);
        giggle_layout.setOnClickListener(this);
        dimple_layout.setOnClickListener(this);
        smile_layout.setOnClickListener(this);
        waist_layout.setOnClickListener(this);
        waist_muscle_layout.setOnClickListener(this);
        ass_layout.setOnClickListener(this);


        age_layout = findViewById(R.id.age_layout);
        high_layout = findViewById(R.id.high_layout);
        earn_layout = findViewById(R.id.earn_layout);
        age_layout.setOnClickListener(this);
        high_layout.setOnClickListener(this);
        earn_layout.setOnClickListener(this);

        new_action = findViewById(R.id.new_action);
        near = findViewById(R.id.near);

        new_action.setOnClickListener(this);
        near.setOnClickListener(this);

        gender_all = findViewById(R.id.gender_all);
        gender_male = findViewById(R.id.gender_male);
        gender_female = findViewById(R.id.gender_female);

        gender_all.setOnClickListener(this);
        gender_male.setOnClickListener(this);
        gender_female.setOnClickListener(this);

        field = findViewById(R.id.field);
        country = findViewById(R.id.country_layout);
        field_line = findViewById(R.id.field_line);
        country_line = findViewById(R.id.country_line);
        city = findViewById(R.id.city);
        field.setOnClickListener(this);

        s_v = findViewById(R.id.s_v);

        search = findViewById(R.id.search);
        search.setOnClickListener(this);

        earn_text = findViewById(R.id.earn_text);
        age = findViewById(R.id.age);
        tall = findViewById(R.id.tall);

        SharedPreferences pref = getSharedPreferences(Date.DATE, 0);
        sort = pref.getInt(Date.SORT, 0);
        if(sort == 2) {
            near.setTextColor(getResources().getColor(R.color.colorBlack));
            new_action.setTextColor(getResources().getColor(R.color.colorNotSelected));
            country.setVisibility(View.GONE);
            field.setVisibility(View.GONE);
            country_line.setVisibility(View.GONE);
            field_line.setVisibility(View.GONE);
        } else {
            new_action.setTextColor(getResources().getColor(R.color.colorBlack));
            near.setTextColor(getResources().getColor(R.color.colorNotSelected));
            country.setVisibility(View.VISIBLE);
            field.setVisibility(View.VISIBLE);
            country_line.setVisibility(View.VISIBLE);
            field_line.setVisibility(View.VISIBLE);
        }
        gender = pref.getInt(Date.GENDER, 0);
        if(gender == 1) {
            gender_male.setTextColor(getResources().getColor(R.color.colorBlack));
            gender_all.setTextColor(getResources().getColor(R.color.colorNotSelected));
            gender_female.setTextColor(getResources().getColor(R.color.colorNotSelected));
        } else if(gender == 2) {
            gender_female.setTextColor(getResources().getColor(R.color.colorBlack));
            gender_male.setTextColor(getResources().getColor(R.color.colorNotSelected));
            gender_all.setTextColor(getResources().getColor(R.color.colorNotSelected));
        }
        city_name = pref.getString(Date.FILED, null);
        if(city_name != null) {
            city.setText(city_name);
        }
        city_code = pref.getString(Date.CITY_CODE, null);
        province_code = pref.getString(Date.PROVINCE_CODE, null);

        if(pref.getInt(Date.VIDEO, 0) == 1) {
            s_v.setChecked(true);
        }

        age_l = pref.getString(Date.AGE_L, null);
        age_r = pref.getString(Date.AGE_R, null);
        if(age_l!=null && age_r !=null) {
            age.setText(age_l + "-" + age_r);
        }

        tall_l = pref.getString(Date.TALL_L, null);
        tall_r = pref.getString(Date.TALL_R, null);
        if(tall_l != null && tall_r != null) {
            tall.setText(tall_l + "-" + tall_r);
        }

        income = pref.getString(Date.INCOME, null);
        earn = pref.getString(Date.EARN, null);
        if(earn != null) {
            earn_text.setText(earn);
        }

        //satisfactory = pref.getString(Date.SATISFACTORY, null);

    }

    @Override
    public void onClick(View view) {
//        if(!Date.isVIP) {
//            switch (view.getId()) {
//                case R.id.age_layout:
//                case R.id.high_layout:
//                case R.id.earn_layout:
//                    showNoVIPDialog();
//                    return;
//            }
//        }
        switch (view.getId()) {
            case R.id.age_layout:
                age_l = null;
                age_r = null;
                age_l_index = 2;
                age_r_index = 3;
                View outerView1 = LayoutInflater.from(this).inflate(R.layout.double_wheel_view, null);
                final WheelView wv2 = (WheelView) outerView1.findViewById(R.id.wheel_view_wv2);
                wv2.setOffset(2);
                wv2.setItems(Arrays.asList(AGES));
                wv2.setSeletion(0);
                wv2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.d("date", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                        age_r_index = selectedIndex;
                        if(age_l == null || age_l.equals("不限")) {
                            age_l = null;
                            age_r = null;
                            age_l_index = 0;
                            wv2.setSeletion(0);
                            return;
                        }
                        age_r = item;
                        if(age_l_index >= age_r_index) {
                            wv2.setSeletion(age_l_index + 1 -2);
                            age_r_index = age_l_index + 1;
                            try {
                                age_r = AGES[age_r_index - 2];
                            }catch (Exception e) {
                                e.printStackTrace();
                                age_r = AGES[AGES.length - 1];
                            }
                        }

                    }
                });
                WheelView wv1 = (WheelView) outerView1.findViewById(R.id.wheel_view_wv);
                wv1.setOffset(2);
                wv1.setItems(Arrays.asList(AGES));
                wv1.setSeletion(0);
                wv1.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.d("date", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                        age_l_index = selectedIndex;
                        age_l = item;
                        if(age_r == null) {

                            age_r_index = selectedIndex + 1;
                            age_r = AGES[age_r_index - 2];
                            wv2.setSeletion(age_r_index-2);
                        }
                        if(age_l_index == 2) {
                            age_r = null;
                            wv2.setSeletion(0);
                        }
                        if(age_l_index >= age_r_index) {

                            age_r_index = age_l_index;
                            wv2.setSeletion(age_r_index-1);


                            if(age_r_index - 1 == AGES.length) {
                                age_r = AGES[AGES.length - 1];
                            } else {
                                age_r = AGES[age_r_index -1];
                            }

                        }

                    }
                });

                final android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(this);
                builder1.setView(outerView1);
                final android.support.v7.app.AlertDialog dilog1 = builder1.show();
                TextView ok1 = outerView1.findViewById(R.id.sure);
                ok1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(age_l == null || age_r == null) {
                            age.setText("不限");
                        } else {
                            age.setText(age_l + "-" + age_r);
                        }
                        dilog1.dismiss();
                    }
                });
                break;
            case R.id.high_layout:
                tall_l = null;
                tall_r = null;
                tall_l_index = 2;
                tall_r_index = 3;
                View outerView2 = LayoutInflater.from(this).inflate(R.layout.double_wheel_view, null);
                final WheelView wv4 = (WheelView) outerView2.findViewById(R.id.wheel_view_wv2);
                wv4.setOffset(2);
                wv4.setItems(Arrays.asList(TALLS));
                wv4.setSeletion(0);
                wv4.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.d("date", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                        tall_r_index = selectedIndex;
                        if(tall_l == null || tall_l.equals("不限")) {
                            tall_l = null;
                            tall_r = null;
                            tall_l_index = 0;
                            wv4.setSeletion(0);
                            return;
                        }
                        tall_r = item;

                        if(tall_l_index >= tall_r_index) {
                            wv4.setSeletion(tall_l_index + 1 -2);
                            tall_r_index = tall_l_index + 1;
                            try {
                                tall_r = TALLS[tall_r_index - 2];
                            }catch (Exception e) {
                                e.printStackTrace();
                                tall_r = TALLS[TALLS.length - 1];
                            }
                        }

                    }
                });
                WheelView wv3 = (WheelView) outerView2.findViewById(R.id.wheel_view_wv);
                wv3.setOffset(2);
                wv3.setItems(Arrays.asList(TALLS));
                wv3.setSeletion(0);
                wv3.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.d("date", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                        tall_l_index = selectedIndex;
                        tall_l = item;

                        if(tall_r == null) {

                            tall_r_index = selectedIndex + 1;
                            tall_r = TALLS[tall_r_index - 2];
                            wv4.setSeletion(tall_r_index-2);
                        }

                        if(tall_l_index == 2) {
                            tall_r = null;
                            wv4.setSeletion(0);
                        }
                        if(tall_l_index >= tall_r_index) {
                            tall_r_index = tall_l_index;
                            wv4.setSeletion(tall_r_index-1);

                            if(tall_r_index - 1 == TALLS.length) {
                                tall_r = TALLS[TALLS.length - 1];
                            } else {
                                tall_r = TALLS[tall_r_index -1];
                            }

                        }

                    }
                });

                final android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(this);
                builder2.setView(outerView2);
                final android.support.v7.app.AlertDialog dilog2 = builder2.show();
                TextView ok2 = outerView2.findViewById(R.id.sure);
                ok2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(tall_l == null || tall_r == null) {
                            tall.setText("不限");
                        } else {
                            tall.setText(tall_l + "-" + tall_r);
                        }
                        dilog2.dismiss();
                    }
                });
                break;
            case R.id.earn_layout:
                View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
                WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                wv.setOffset(2);
                wv.setItems(Arrays.asList(PLANETS));
                wv.setSeletion(0);
                wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.d("date", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                        earn = item;
                        income = (selectedIndex - 1)+"";
                    }
                });
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setView(outerView);
                final android.support.v7.app.AlertDialog dilog = builder.show();
                TextView ok = outerView.findViewById(R.id.sure);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        earn_text.setText(earn);
                        dilog.dismiss();
                    }
                });
                break;
            case R.id.new_action:
                new_action.setTextColor(getResources().getColor(R.color.colorBlack));
                near.setTextColor(getResources().getColor(R.color.colorNotSelected));
                country.setVisibility(View.VISIBLE);
                field.setVisibility(View.VISIBLE);
                country_line.setVisibility(View.VISIBLE);
                field_line.setVisibility(View.VISIBLE);
                sort = 1;
                break;
            case R.id.near:
                near.setTextColor(getResources().getColor(R.color.colorBlack));
                new_action.setTextColor(getResources().getColor(R.color.colorNotSelected));
                country.setVisibility(View.GONE);
                field.setVisibility(View.GONE);
                country_line.setVisibility(View.GONE);
                field_line.setVisibility(View.GONE);
                sort = 2;
                break;
            case R.id.gender_all:
                gender_all.setTextColor(getResources().getColor(R.color.colorBlack));
                gender_male.setTextColor(getResources().getColor(R.color.colorNotSelected));
                gender_female.setTextColor(getResources().getColor(R.color.colorNotSelected));
                gender = 0;
                break;
            case R.id.gender_male:
                gender_male.setTextColor(getResources().getColor(R.color.colorBlack));
                gender_all.setTextColor(getResources().getColor(R.color.colorNotSelected));
                gender_female.setTextColor(getResources().getColor(R.color.colorNotSelected));
                gender = 1;
                break;
            case R.id.gender_female:
                gender_female.setTextColor(getResources().getColor(R.color.colorBlack));
                gender_male.setTextColor(getResources().getColor(R.color.colorNotSelected));
                gender_all.setTextColor(getResources().getColor(R.color.colorNotSelected));
                gender = 2;
                break;
            case R.id.field:
                startActivityForResult(new Intent(FilterActivity.this, SelectCityActivity.class), 99);
                break;
            case R.id.search:
                HashMap <String, String> map = new HashMap<>();
                SharedPreferences pref = getSharedPreferences(cn.com.lamatech.date.Date.DATE, 0);
                SharedPreferences.Editor editor = pref.edit();

                if(sort != 0) {
                    map.put("order_type", sort+"");
                    editor.putInt(Date.SORT, sort);
                    if(sort == 2) {
                        map.put("longitude", Date.lng+"");
                        map.put("latitude", Date.lat+"");
                    }
                }
                map.put("sex", gender+"");
                editor.putInt(Date.GENDER, gender);
                if(province_code != null) {
                    map.put("province", province_code);
                    editor.putString(Date.FILED, province==null ? city_name : province+city_name);
                    editor.putString(Date.PROVINCE_CODE, province_code);
                }
                if(city_code != null) {
                    map.put("city", city_code);
                    editor.putString(Date.CITY_CODE, city_code);
                }
                if(s_v.isChecked()) {
                    map.put("auth_video_status", 1+"");
                    editor.putInt(Date.VIDEO, 1);
                } else {
                    editor.putInt(Date.VIDEO, 0);
                }

                if(age_l != null) {
                    map.put("age_l", age_l);
                    editor.putString(Date.AGE_L, age_l);
                } else {
                    editor.putString(Date.AGE_L, null);
                }
                if(age_r != null) {
                    map.put("age_r", age_r);
                    editor.putString(Date.AGE_R, age_r);
                } else {
                    editor.putString(Date.AGE_R, null);
                }
                if(tall_l != null) {
                    map.put("height_l", tall_l.replace("cm", ""));
                    editor.putString(Date.TALL_L, tall_l);
                } else {
                    editor.putString(Date.TALL_L, tall_l);
                }
                if(tall_r != null) {
                    map.put("height_r", tall_r.replace("cm", ""));
                    editor.putString(Date.TALL_R, tall_r);
                } else {
                    editor.putString(Date.TALL_R, tall_r);
                }
                if(earn != null) {
                    map.put("income", income);
                    editor.putString(Date.EARN, earn);
                    editor.putString(Date.INCOME, income);
                } else {
                    editor.putString(Date.EARN, earn);
                    editor.putString(Date.INCOME, income);
                }
                if(satisfactory != null) {
                    map.put("satisfactory_parts", satisfactory);
                    editor.putString(Date.SATISFACTORY, satisfactory);
                }

                editor.commit();


                Intent intent = new Intent();
                final SerializableMap myMap=new SerializableMap();
                myMap.setMap(map);//将map数据添加到封装的myMap中
                Bundle bundle=new Bundle();
                bundle.putSerializable("map", myMap);
                intent.putExtras(bundle);
                setResult(99, intent);
                finish();
                break;
            case R.id.nose_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = nose.getText().toString();
                break;
            case R.id.shoulder_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = shoulder.getText().toString();
                break;
            case R.id.rich_man_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = rich_man.getText().toString();
                break;
            case R.id.hand_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = hand.getText().toString();
                break;
            case R.id.voice_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = voice.getText().toString();
                break;
            case R.id.leg_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = leg.getText().toString();
                break;
            case R.id.eye_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = eye.getText().toString();
                break;
            case R.id.pecs_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = pecs.getText().toString();
                break;
            case R.id.lip_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = lip.getText().toString();
                break;
            case R.id.skin_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = skin.getText().toString();
                break;
            case R.id.nose_bridge_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = nose_bridge.getText().toString();
                break;
            case R.id.collarbone_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = collarbone.getText().toString();
                break;
            case R.id.abdomen_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = abdomen.getText().toString();
                break;
            case R.id.chest_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = chest.getText().toString();
                break;
            case R.id.whiskers_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = whiskers.getText().toString();
                break;
            case R.id.giggle_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = giggle.getText().toString();
                break;
            case R.id.dimple_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = dimple.getText().toString();
                break;
            case R.id.smile_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = smile.getText().toString();
                break;
            case R.id.waist_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = waist.getText().toString();
                break;
            case R.id.waist_muscle_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = waist_muscle.getText().toString();
                break;
            case R.id.ass_layout:
                nose_layout.setAlpha(1.0f);
                shoulder_layout.setAlpha(1.0f);
                rich_man_layout.setAlpha(1.0f);
                hand_layout.setAlpha(1.0f);
                voice_layout.setAlpha(1.0f);
                leg_layout.setAlpha(1.0f);
                eye_layout.setAlpha(1.0f);
                pecs_layout.setAlpha(1.0f);
                lip_layout.setAlpha(1.0f);
                skin_layout.setAlpha(1.0f);
                nose_bridge_layout.setAlpha(1.0f);
                collarbone_layout.setAlpha(1.0f);
                abdomen_layout.setAlpha(1.0f);
                chest_layout.setAlpha(1.0f);
                whiskers_layout.setAlpha(1.0f);
                giggle_layout.setAlpha(1.0f);
                dimple_layout.setAlpha(1.0f);
                smile_layout.setAlpha(1.0f);
                waist_layout.setAlpha(1.0f);
                waist_muscle_layout.setAlpha(1.0f);
                ass_layout.setAlpha(1.0f);
                view.setAlpha(0.4f);
                satisfactory = ass.getText().toString();
                break;
        }

    }

    void showNoVIPDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FilterActivity.this);
        builder.setMessage("仅VIP会员才能使用年龄，身高，收入搜索功能");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setView(R.layout.no_vip_dialog);
//        }
        builder.setPositiveButton("看看VIP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == 1000) {
            province = data.getStringExtra("province");
            city_name = data.getStringExtra("city");
            province_code = data.getStringExtra("province_code");
            city_code = data.getStringExtra("city_code");
            Log.e("date", "city is " + province + city_name);
            city.setText(province + city_name);
        }
    }
}
