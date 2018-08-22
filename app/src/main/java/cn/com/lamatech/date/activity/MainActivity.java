package cn.com.lamatech.date.activity;

import android.Manifest;
import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.lamatech.seekserverproxy.HttpConstants;
import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;
import cn.com.lamatech.date.utils.CheckPermissionUtils;
import cn.com.lamatech.date.view.ShilangTabHost;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends ActivityGroup {

    LocationManager locationManager;
    String locationProvider;
    String TAG = "date";
    ShilangTabHost tabHost;
    GotoTabFirstReceiver gotoTabFirstReceiver;
    class GotoTabFirstReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("date", "go to first tab");
            tabHost.setCurrentTabByTag("online");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tabHost = (ShilangTabHost) findViewById(R.id.tabhost);

        tabHost.setVisibility(View.GONE);
        Window window = MainActivity.this.getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorActionBar));

        WebView hide_web_view = findViewById(R.id.hide_web_view);
        String uri = HttpConstants.HTTP_REQUEST + "/mobile/local/saveUserInfo/user_id/" + Date.mUserInfo.userid;
        Log.e("date", "hide view uri is " + uri);
        hide_web_view.loadUrl(uri);

        WebSettings settings = hide_web_view.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        /***打开本地缓存提供JS调用**/
        hide_web_view.getSettings().setDomStorageEnabled(true);
        // Set cache size to 8 mb by default. should be more than enough
        hide_web_view.getSettings().setAppCacheMaxSize(1024*1024*8);

        // This next one is crazy. It's the DEFAULT location for your app's cache
        // But it didn't work for me without this line.
        // UPDATE: no hardcoded path. Thanks to Kevin Hawkins
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        hide_web_view.getSettings().setAppCachePath(appCachePath);
        hide_web_view.getSettings().setAllowFileAccess(true);
        hide_web_view.getSettings().setAppCacheEnabled(true);

        hide_web_view.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                Log.e("date", "load hide web view finished");

                tabHost.setVisibility(View.VISIBLE);
               // mHandler.sendEmptyMessage(0);
                super.onPageFinished(view, url);
            }
        });
        hide_web_view.setWebChromeClient(new WebChromeClient());


        mHandler.sendEmptyMessage(0);
        initPermission();


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.com.lamatech.date.GOTO_TAB_FIRST");
        gotoTabFirstReceiver = new GotoTabFirstReceiver();
        registerReceiver(gotoTabFirstReceiver, intentFilter);
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            tabHost.setup(getLocalActivityManager());
            tabHost.addTab(tabHost.newTabSpec("online")
                    .setIndicator("在线",R.drawable.online)
                    .setContent(new Intent(MainActivity.this, OnlineActivity.class)));
            tabHost.addTab(tabHost.newTabSpec("interview")
                    .setIndicator("邀约",R.drawable.date)
                    .setContent(new Intent(MainActivity.this, InterviewActivity.class)));
            tabHost.addTab(tabHost.newTabSpec("movement")
                    .setIndicator("动态",R.drawable.movement)
                    .setContent(new Intent(MainActivity.this, MovementActivity.class)));
            tabHost.addTab(tabHost.newTabSpec("mine")
                    .setIndicator("我的",R.drawable.mine)
                    .setContent(new Intent(MainActivity.this, MineActivity.class)));
            tabHost.setCurrentTabByTag("online");
            return true;
        }
    });

    @Override
    protected void onResume() {
        super.onResume();
        //getLocation();
    }

    void getLocation() {

        locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        //下面注释的代码获取的location为null，所以采用Criteria的方式。
        /*List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
            Log.d(TAG, "onCreate: gps=" + locationProvider);
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
            Log.d(TAG, "onCreate: network=" + locationProvider);
        } else {
            Log.d(TAG, "onCreate: 没有可用的位置提供器");
            Toast.makeText(this,"没有可用的位置提供器",Toast.LENGTH_SHORT).show();
            return;
        }
        //获取Location，老是获取为空！所以用locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(locationProvider);
        */
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);//低精度，如果设置为高精度，依然获取不了location。
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗

        //从可用的位置提供器中，匹配以上标准的最佳提供器
        locationProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: 没有权限 ");
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        Log.d(TAG, "onCreate: " + (location == null) + "..");
        if (location != null) {
            Log.d(TAG, "onCreate: location");
            //不为空,显示地理位置经纬度
            showLocation(location);
        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }
    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider + ".." + Thread.currentThread().getName());
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider + ".." + Thread.currentThread().getName());
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: " + ".." + Thread.currentThread().getName());
            //如果位置发生变化,重新显示
            showLocation(location);
        }
    };

    private void showLocation(final Location location) {
        Log.d(TAG,"定位成功------->"+"location------>经度为：" + location.getLatitude() + "\n纬度为" + location.getLongitude());
        new Thread() {
            public void run() {
                getCityName(location);
            }
        }.start();

    }

    private void getCityName(Location location) {
        String path = "http://restapi.amap.com/v3/geocode/regeo?output=json&location=" +
                location.getLongitude() + "," + location.getLatitude() + "&key=d9e3a11ce15286228f52766416a8dbfd";

        Map map = new HashMap();
        map.put("output", "json");
        map.put("location", location.getLongitude() + "," + location.getLatitude());
        map.put("key", "d9e3a11ce15286228f52766416a8dbfd");
        try {
            String result = ServerProxy.net("http://restapi.amap.com/v3/geocode/regeo", map, "GET");
            Log.e("date", "regeo result is " + result);
            JSONObject res = new JSONObject(result);
            Date.city = res.getJSONObject("regeocode").getJSONObject("addressComponent").getString("city");
            Log.e("date",Date.city);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 初始化权限事件
     */
    private void initPermission() {
        //检查权限
        String[] permissions = CheckPermissionUtils.checkPermission(this);
        if (permissions.length == 0) {
            //权限都申请了
            //是否登录
        } else {
            //申请权限
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }
    /**
     * EsayPermissions接管权限处理逻辑
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("date", "result requestCode is " + requestCode);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        getLocation();
    }
//
//
//    @Override
//    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        Toast.makeText(this, "执行onPermissionsGranted()...", Toast.LENGTH_SHORT).show();
//        Log.e("date", "granted requestCode is " + requestCode);
//    }
//
//    @Override
//    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        Log.e("date", "denied requestCode is " + requestCode);
//        Toast.makeText(this, "执行onPermissionsDenied()...", Toast.LENGTH_SHORT).show();
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this, "当前App需要申请camera权限,需要打开设置页面么?")
//                    .setTitle("权限申请")
//                    .setPositiveButton("确认")
//                    .setNegativeButton("取消", null /* click listener */)
//                    .setRequestCode(88)
//                    .build()
//                    .show();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //locationManager.removeUpdates(locationListener);
        unregisterReceiver(gotoTabFirstReceiver);
    }

    @Override
    protected  void onPause() {
        super.onPause();
        //locationManager.removeUpdates(locationListener);

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
            Toast.makeText(MainActivity.this, "再按一次退出美丽约", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }



}
