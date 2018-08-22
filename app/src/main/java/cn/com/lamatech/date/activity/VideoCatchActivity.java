package cn.com.lamatech.date.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.lamatech.seekserverproxy.HttpConstants;
import com.lamatech.seekserverproxy.UploadFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;

public class VideoCatchActivity extends AppCompatActivity {

    VideoView video;
    int RECORD_SYSTEM_VIDEO = 99;
    String video_path;
    boolean isFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_catch);
        video = findViewById(R.id.video);
        isFirst = getIntent().getBooleanExtra("first", false);

        final TextView video_catch = findViewById(R.id.video_catch);
        video_catch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reconverIntent(video_catch);
            }
        });

    }

    /**
     * 启用系统相机录制
     *
     * @param view
     */
    public void reconverIntent(View view) {
        //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
        Uri fileUri = FileProvider.getUriForFile(this, "cn.com.lamatech.date.fileprovider", getOutputMediaFile());
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3); //限制的录制时长 以秒为单位
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,0); //设置拍摄的质量最小是0，最大是1（建议不要设置中间值，不同手机似乎效果不同。。。）
        //intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 1024);//限制视频文件大小 以字节为单位
        //MediaStore.EXTRA_MEDIA_RADIO_CHANNEL
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(intent, RECORD_SYSTEM_VIDEO);
    }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(data == null) {
            return;
        }
        video.setVideoURI(data.getData());
        video.start();
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                // TODO Auto-generated method stub
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });
//
        uploadVideo();
    }


    void uploadVideo() {
        new Thread() {
            public void run() {
                UploadFile uploadFile = new UploadFile();
                HashMap map = new HashMap();
                if(Date.mUserInfo == null) {
                    return;
                }
                map.put("user_id", Date.mUserInfo.userid);
                map.put("type", "auth_video");
                map.put("action", "add");
                String result = uploadFile.uploadFile(HttpConstants.HTTP_UPLOAD_PIC_FILE, video_path, map);
                Log.e("date", "upload head_pic result is " + result);
                if(result == null) {
                    return;
                }
                try {
                    JSONObject res = new JSONObject(result);
                    if(res.getInt("code") == 200) {
                        Date.mUserInfo.video = HttpConstants.HTTP_HEAD_PIC;
                        if(isFirst) {
                            startActivity(new Intent(VideoCatchActivity.this, MainActivity.class));
                        }
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
