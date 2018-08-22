package cn.com.lamatech.date.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lamatech.seekserverproxy.HttpConstants;
import com.lamatech.seekserverproxy.UploadFile;
import com.lling.photopicker.PhotoPickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;

public class InterviewActivity extends AppCompatActivity {

    WebView interview;
    ProgressBar pg1;

    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>
    private Uri imageUri;
    private void dialogShow() {
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
        dialog.setCanceledOnTouchOutside(false);
        DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        } ;
        dialog.setOnKeyListener(keylistener);
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(InterviewActivity.this, VIPCenterActivity.class));
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sendBroadcast(new Intent("cn.com.lamatech.date.GOTO_TAB_FIRST"));
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
        dialog.setCanceledOnTouchOutside(false);
        DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        } ;
        dialog.setOnKeyListener(keylistener);
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(InterviewActivity.this, VideoCatchActivity.class));
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sendBroadcast(new Intent("cn.com.lamatech.date.GOTO_TAB_FIRST"));
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Date.mUserInfo.level.equals("0") && "1".equals(Date.mUserInfo.gender)) {
            dialogShow();
        }

        if("0".equals(Date.mUserInfo.auth_video_status) && "2".equals(Date.mUserInfo.gender)) {
            dialogShow3();
            return;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);


        interview = findViewById(R.id.interview);

        pg1= findViewById(R.id.progressBar1);

        String uri = HttpConstants.HTTP_REQUEST + HttpConstants.INTERVIEW + "/user_id/" + Date.mUserInfo.userid;
        interview.loadUrl(uri);

        interview.getSettings().setJavaScriptEnabled(true);
        interview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        WebSettings settings = interview.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        interview.addJavascriptInterface(new JSInterface(),"Android");
        /***打开本地缓存提供JS调用**/
        interview.getSettings().setDomStorageEnabled(true);
        // Set cache size to 8 mb by default. should be more than enough
        interview.getSettings().setAppCacheMaxSize(1024*1024*8);

        // This next one is crazy. It's the DEFAULT location for your app's cache
        // But it didn't work for me without this line.
        // UPDATE: no hardcoded path. Thanks to Kevin Hawkins
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        interview.getSettings().setAppCachePath(appCachePath);
        interview.getSettings().setAllowFileAccess(true);
        interview.getSettings().setAppCacheEnabled(true);

        interview.setWebViewClient(new WebViewClient(){
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
                super.onPageFinished(view, url);
            }
        });
        interview.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根

                if(newProgress==100){
                    pg1.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg1.setProgress(newProgress);//设置进度值
                }

            }
            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL=filePathCallback;
                take();
                return true;
            }


            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage=uploadMsg;
                take();
            }
            public void openFileChooser(ValueCallback<Uri> uploadMsg,String acceptType) {
                mUploadMessage=uploadMsg;
                take();
            }
            public void openFileChooser(ValueCallback<Uri> uploadMsg,String acceptType, String capture) {
                mUploadMessage=uploadMsg;
                take();
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ALBUM1://这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
                if (data == null) {
                    if (requestCode == REQUEST_ALBUM1) {
                    }
                    return;
                }

                ArrayList<String> tmpResult = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                final String picturePath = tmpResult.get(0);
                uploadFile(picturePath);
                break;
            case 99:
                if(requestCode == 99) {
                    if(data == null) {
                        return;
                    }

                    Log.e("date", "data data is " + data.getData());
                    interview.loadUrl("javascript:getJavaFiles("+"'"+video_path+"'"+")");
                    uploadFile(video_path);
                    return;
                }

                break;
        }
        if(requestCode==FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            }
            else  if (mUploadMessage != null) {

                if (result != null) {
                    String path = getPath(getApplicationContext(),
                            result);
                    Uri uri = Uri.fromFile(new File(path));
                    mUploadMessage
                            .onReceiveValue(uri);
                } else {
                    mUploadMessage.onReceiveValue(imageUri);
                }
                mUploadMessage = null;



            }
        }
    }



    @SuppressWarnings("null")
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;

        if (resultCode == Activity.RESULT_OK) {

            if (data == null) {

                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if(results!=null){
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }else{
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }

        return;
    }



    private void take(){
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        // Create the storage directory if it does not exist
        if (! imageStorageDir.exists()){
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);

        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i,"Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        InterviewActivity.this.startActivityForResult(chooserIntent,  FILECHOOSER_RESULTCODE);
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }




    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    //设置返回键动作（防止按返回键直接退出程序)
    //退出时的时间
    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            if(interview.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                interview.goBack();
                return true;
            } else if(event.getRepeatCount() == 0){
                exit();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(InterviewActivity.this, "再按一次退出美丽约", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    private static final int REQUEST_ALBUM1 = 2;// 相册的请求
    class JSInterface {
//        @JavascriptInterface
//        public void goBack(String arg){
//            finish();
//        }

        @JavascriptInterface
        public void gotoJavaCamera(int type) {
            if(type == 3) {
                reconverIntent();
                return;
            }
            Log.e("date", "goto java camera");
            Intent intent = new Intent(InterviewActivity.this, PhotoPickerActivity.class);
            intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
            intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
            intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 1);
            startActivityForResult(intent, REQUEST_ALBUM1);
        }
    }



    /**
     * 启用系统相机录制
     *
     */
    public void reconverIntent() {
        //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
        Uri fileUri = FileProvider.getUriForFile(this, "cn.com.lamatech.date.fileprovider", getOutputMediaFile());
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10); //限制的录制时长 以秒为单位
        //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1); //设置拍摄的质量最小是0，最大是1（建议不要设置中间值，不同手机似乎效果不同。。。）
        //intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 1024);//限制视频文件大小 以字节为单位
        //MediaStore.EXTRA_MEDIA_RADIO_CHANNEL
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(intent, 99);
    }
String video_path;
    private File getOutputMediaFile() {
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Toast.makeText(this, "请检查SDCard！", Toast.LENGTH_SHORT).show();
            return null;
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DCIM), "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        // Create a media file name
        String timeStamp = System.currentTimeMillis()+"";
        video_path = mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4";
        File mediaFile = new File(video_path);
        return mediaFile;
    }

    void uploadFile(final String path) {
        new Thread() {
            public void run() {
                UploadFile uploadFile = new UploadFile();
                String result = uploadFile.uploadFile(HttpConstants.HTTP_UPLOAD_FILE, path);
                Log.e("date", "upload file result is " + result);
                if(result == null) {
                    return;
                }
                try {

                    JSONObject res = new JSONObject(result);
                    if(res.getInt("code") == 200) {

                        JSONObject data = res.getJSONObject("data");
                        final String file_path = data.getString("filepath");
                        Log.e("date", "filepath is " + file_path);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                interview.loadUrl("javascript:getJavaFiles("+"'"+file_path+"'"+")");
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }



}
