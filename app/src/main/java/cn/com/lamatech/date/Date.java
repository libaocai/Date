package cn.com.lamatech.date;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Map;

import io.rong.imkit.RongIM;

public class Date extends Application {


    public static String city = "";
    public static String province="";
    public static String province_code = "";
    public static String city_code = "";
    public static Double lng = 0d;
    public static Double lat = 0d;
    public static boolean isVIP = false;
    public static Bitmap head_pic = null;
    public static UserInfo mUserInfo = null;

    public static final String DATE = "DATE";

    public static final String SORT = "SORT";
    public static final String GENDER = "GENDER";
    public static final String FILED = "FILED";
    public static final String PROVINCE_CODE = "PROVINCE_CODE";
    public static final String CITY_CODE = "CITY_CODE";
    public static final String VIDEO = "VIDEO";
    public static final String AGE_L = "AGE_L";
    public static final String AGE_R = "AGE_R";
    public static final String TALL_L = "TALL_L";
    public static final String TALL_R = "TALL_R";
    public static final String EARN = "EARN";
    public static final String INCOME = "INCOME";
    public static final String SATISFACTORY = "SATISFACTORY";

    public static final String WX_APP_ID = "wx14916e24be20671a";
    public static final String WX_APP_SECRET="5029de75a63b9dea815a8f64f4b593e6";

    public static final String WX_APP_KEY = "5194c04b43efe1c4973833c8f40a93b9";
    public static final String MCH_ID ="1505643651";

    @Override
    public void onCreate() {
        super.onCreate();


        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
        }

//        SpeechUtility.createUtility(Seek.this, "appid=5a123299");
//
//        instence = this;
//        SDKInitializer.initialize(this);
//        SDKInitializer.setCoordType(CoordType.BD09LL);
//
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.mipmap.mine_head_pic) //
                .showImageOnFail(R.mipmap.mine_head_pic) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
        ImageLoaderConfiguration config = new ImageLoaderConfiguration//
                .Builder(getApplicationContext())//
                .defaultDisplayImageOptions(defaultOptions)//
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)// 缓存一百张图片
                .writeDebugLogs()//
                .build();//
        ImageLoader.getInstance().init(config);
//
//
//        //设置LOG开关，默认为false
//
//        UMConfigure.setLogEnabled(true);
//        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
//        UMConfigure.init(this, "5a137c06f43e48552c0001bc", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "0cc73761c9ce094528029e30b46fdad7");
//
//        PushAgent mPushAgent = PushAgent.getInstance(this);
//
//        //sdk开启通知声音
//        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
//        // sdk关闭通知声音
////		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
//        // 通知声音由服务端控制
////		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);
//
////		mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
////		mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
//
//
//        UmengMessageHandler messageHandler = new UmengMessageHandler() {
//            /**
//             * 自定义消息的回调方法
//             * */
//            @Override
//            public void dealWithCustomMessage(final Context context, final UMessage msg) {
//
//
//                new Handler().post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        Log.e("seek", "dealWithCustomMessage");
//                        // TODO Auto-generated method stub
//                        // 对自定义消息的处理方式，点击或者忽略
//                        boolean isClickOrDismissed = true;
//                        if (isClickOrDismissed) {
//                            //自定义消息的点击统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
//                        } else {
//                            //自定义消息的忽略统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
//                        }
//                        startActivity(new Intent(Seek.this, ReceiveOrderActivity.class));
//                        //Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            /**
//             * 自定义通知栏样式的回调方法
//             * */
//            @Override
//            public Notification getNotification(Context context, UMessage msg) {
//                Log.e("seek", "getNotification text is " + msg.text);
//                Log.e("seek", "getNotification title is " + msg.title);
//                Log.e("seek", "getNotification url is " + msg.url);
//                Log.e("seek", "getNotification display type is " + msg.display_type);
//                Log.e("seek", "getNotification custom is " + msg.custom);
//
//                /*
//                custom :
//                {"orderid":"2017101604698365","mid":"297e99b65f0fbe6b015f0fd304d10003",
//                "message_time2":"","message_type":5,"message_time1":"1508120209048",
//                "content":"白花花hh","use_address2":"","push_count":0,"use_address1":"1",
//                "surplusprice":7787,"send_time":"1508120222811","aid1":"辽宁中医药大学 和舒适度很好的广告",
//                "commission":7787,"id":"297e99b65f22ece2015f22f85c5f0001","aid2":"","status":1,"couponprice":0}
//
//                 */
//                Intent intent = new Intent("com.lamatech.seek.NEW_DATA");
//                intent.putExtra("title", msg.title);
//                intent.putExtra("text", msg.text);
//                intent.putExtra("custom", msg.custom);
//                sendBroadcast(intent);
//                switch (msg.builder_id) {
//                    case 1:
//                        Notification.Builder builder = new Notification.Builder(context);
////                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
////                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
////                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
////                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
////                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
////                        builder.setContent(myNotificationView)
////                                .setSmallIcon(getSmallIconId(context, msg))
////                                .setTicker(msg.ticker)
////                                .setAutoCancel(true);
//
//                        return builder.getNotification();
//                    default:
//                        //默认为0，若填写的builder_id并不存在，也使用默认。
//                        return super.getNotification(context, msg);
//                }
//            }
//        };
//        mPushAgent.setMessageHandler(messageHandler);
//
//
//        /**
//         * 自定义行为的回调处理
//         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
//         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
//         * */
//        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
//            @Override
//            public void dealWithCustomAction(Context context, UMessage msg) {
//                Log.e("seek", "notificationClickHandler dealWithCustomMessage");
//                Log.e("seek", "notificationClickHandler dealWithCustomMessage text is " + msg.text);
//                Log.e("seek", "notificationClickHandler dealWithCustomMessage title is " + msg.title);
//                if("您有新的订单了".equals(msg.text)) {
//                    Intent intent = new Intent(Seek.this, NewMainActivity.class);
//                    intent.putExtra("show_receive",true);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    return;
//                }
//
//                if(msg.text.contains("请求加您为好友")) {
//                    Intent intent = new Intent(Seek.this, MessageListActivity.class);
//                    intent.putExtra("new_friend",true);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    return;
//                }
//
//                try {
//                    JSONObject resObject = new JSONObject(msg.custom);
//                    String id = resObject.getString("id");
//                    Intent intent = new Intent(Seek.this, OrderDetailActivity.class);
//                    intent.putExtra("pkid", id);
//                    startActivity(intent);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//
//            }
//        };
//        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
//        //参考http://bbs.umeng.com/thread-11112-1-1.html
//        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
//        mPushAgent.setNotificationClickHandler(notificationClickHandler);
//
//
//        //注册推送服务 每次调用register都会回调该接口
//        mPushAgent.register(new IUmengRegisterCallback() {
//            @Override
//            public void onSuccess(String deviceToken) {
//                Log.e("seek", "on Success device token: " + deviceToken);
//                SharedPreferences pref = getSharedPreferences(Seek.SEEK, 0);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putString(UserInfo.DEVICE_TOKEN, deviceToken);
//                editor.commit();
//
//                if(Seek.mUserInfo != null) {
//                    ServerProxy.updateDeviceToken(Seek.mUserInfo.pkid, deviceToken, pref.getString(UserInfo.FLAG_KEY, ""));
//                }
////                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//                Log.e("seek", "register failed: " + s + " " +s1);
//                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
//            }
//        });
//
//        //此处是完全自定义处理设置
////        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
//

    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }


    public static class UserInfo {
        public String userid;
        public String username;
        public String pwd;
        public String head_pic;
        public String pkid;
        public String mood;
        public String phone;
        public String email;
        public int authentication;
        public String longitude;
        public String latitude;
        public String location;
        public Bitmap head_pic_bm;
        public String wopenid;
        public String age;
        public String gender;
        public String sharecode;
        public String video;
        public String uuid;
        public String token;
        public String level;
        public String auth_video_status;

        public static  final String USER_ID = "userid";
        public static final String PWD = "pwd";
        public static final String LAST_LOGIN = "last_login";
        public static final String DEVICE_TOKEN = "device_token";
        public static final String PKID = "pkid";
        public static final String FLAG_KEY = "flagkey";

        /*
        {"code":"0","msg":"成功","username":"13080700025",
        "photo":"/photo/1482196882714.jpg","password":"D8578EDF8458CE06FBC5BB76A58C5CA4",
        "name":"","iphone":"","sex":"","email":"","longitude":"","latitude":"","location":"",
        "account":"28.0","paymentmode":"","orderflag":"1","time":"1481779202538","mood":""}
         */
        public  UserInfo(Map<String, Object> map) {
            this.userid = (String) map.get("user_id");
            this.username = (String) map.get("name");
            this.pwd = (String) map.get("password");
            this.head_pic = (String) map.get("head_pic");
            this.pkid = (String) map.get("pkid");
            this.mood = (String) map.get("mood");
            this.phone = (String) map.get("phone");
            this.email = (String) map.get("email");
            this.longitude = (String) map.get("longitude");
            this.location = (String) map.get("location");
            this.latitude = (String) map.get("latitude");
            this.age = map.get("age")+"";
            this.gender = map.get("sex")+"";
            this.sharecode = map.get("sharecode")+"";
            this.uuid = map.get("uuid")+"";
            this.token = map.get("token")+"";
            this.level = map.get("level") + "";
            this.auth_video_status = map.get("auth_video_status")+"";

            try {
                this.authentication = Integer.parseInt((String) map.get("idVerification")); // 1：成功   2：失败   0：未认证  4:审核中
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(this.username == null) {
                this.username = "";
            }

            this.wopenid = (String) map.get("wopenid");
            if((this.userid != null &&!"".equals(this.userid)) && (this.phone == null || "".equals(this.phone))) {
                this.phone = this.userid;
            }
        }

        public void setHeadPicBm(Bitmap bm) {

            Log.e("date", "the bitmap is " + bm);
            this.head_pic_bm = bm;
        }

        public String toString() {
            String str = "user info is " + "userid=" + userid +
                    ";username =" + username +
                    ";head_pic=" + head_pic + ";pkid=" + pkid + ";mood=" + mood
                    + ";phone=" + phone + ";email="+email + ";location=" + location + ";wxopenid=" + wopenid
                    + "; uuid = " + uuid + ";level=" + level + ";auth_video_status=" + auth_video_status;
            return  str;
        }
    }
}
