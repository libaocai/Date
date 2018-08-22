package cn.com.lamatech.date.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.com.lamatech.date.Date;

public class UpdateLocationService extends Service {
    public UpdateLocationService() {
    }

    LocationManager locationManager;
    String locationProvider;
    String TAG = "date";
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getLocation();
        return super.onStartCommand(intent, flags, startId);
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
        //Log.d(TAG,"定位成功------->"+"location------>经度为：" + location.getLatitude() + "\n纬度为" + location.getLongitude());
        double lng = location.getLongitude();
        double lat = location.getLatitude();
        if(Date.lng != lng || Date.lat != lat) {
            Date.lng = location.getLongitude();
            Date.lat = location.getLatitude();

            if(Date.mUserInfo == null) {
                return;
            }

            new Thread() {
                public void run() {
                    getCityName(location);
                }
            }.start();
        }



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
            //Log.e("date", "regeo result is " + result);
            JSONObject res = new JSONObject(result);
            Date.city = res.getJSONObject("regeocode").getJSONObject("addressComponent").getString("city");
            Date.province = res.getJSONObject("regeocode").getJSONObject("addressComponent").getString("province");
            String adcode = res.getJSONObject("regeocode").getJSONObject("addressComponent").getString("adcode");
            Date.province_code = adcode.substring(0, 3) + "000";
            Date.city_code = adcode.substring(0, 4) + "00";
            Log.e("date", "province code is " + Date.province_code);
            Log.e("date", "city code is " + Date.city_code);

                new Thread() {
                    public void run() {
                        HashMap map = new HashMap();
                        map.put("user_id", Date.mUserInfo.userid);
                        map.put("province", Date.province_code);
                        map.put("city", Date.city_code);
                        map.put("longitude", Date.lng + "");
                        map.put("latitude", Date.lat + "");
                        String result = ServerProxy.updateLocation(map);
                        Log.e("date", "update locations the reuslt is " + result);
                    }
                }.start();

            sendBroadcast(new Intent("com.lamatech.date.UPDATE_LOCATION"));
           // Log.e("date",Date.city);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }

}
