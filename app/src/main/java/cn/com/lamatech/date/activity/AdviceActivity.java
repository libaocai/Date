package cn.com.lamatech.date.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lamatech.seekserverproxy.HttpConstants;
import com.lamatech.seekserverproxy.UploadFile;
import com.lling.photopicker.PhotoPickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;
import cn.com.lamatech.date.utils.CommonUtils;

public class AdviceActivity extends AppCompatActivity {

    EditText advice;
    ImageView pic;
    TextView send;

    String pic_path;

    private static final int REQUEST_CAMMER = 1;// 摄像头的请求
    private static final int REQUEST_ALBUM1 = 2;// 相册的请求
    private static final int REQUEST_ALBUM2 = 3;// 相册的请求
    private static final int REQUEST_ALBUM3 = 4;// 相册的请求
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        LinearLayout back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        advice = findViewById(R.id.advice);
        pic = findViewById(R.id.pic);
        send = findViewById(R.id.send);

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdviceActivity.this, PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, false);
                intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
                intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 1);
                startActivityForResult(intent, REQUEST_ALBUM1);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(advice.length() == 0) {
                    Toast.makeText(AdviceActivity.this, "请填写内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                upload();
            }
        });
    }

    void upload() {
        new Thread() {
            public void run() {
                UploadFile uploadFile = new UploadFile();
                HashMap map = new HashMap();
                if(Date.mUserInfo == null) {
                    return;
                }
                map.put("user_id", Date.mUserInfo.userid);
                map.put("content", advice.getText().toString());

                String result = uploadFile.uploadFile(HttpConstants.HTTP_HEAD +HttpConstants.HTTP_IP + "/Api/User/feedback", pic_path, map);
                Log.e("date", "feed back result is " + result);
                if(result == null) {
                    return;
                }
                try {

                    JSONObject res = new JSONObject(result);
                    if(res.getInt("code") == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AdviceActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_ALBUM1 || requestCode == REQUEST_ALBUM2 || requestCode == REQUEST_ALBUM3) {
            if (data == null) {
                if (requestCode == REQUEST_ALBUM1) {
                    pic.setImageResource(R.mipmap.new072);
                    pic_path = "";
                }
                return;
            }

            ArrayList<String> tmpResult = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
            final String picturePath = tmpResult.get(0);
            Log.e("date", "picturePath:" + picturePath);
            if (requestCode == REQUEST_ALBUM1) {
                pic_path = picturePath;
                pic.setImageBitmap(CommonUtils.getimage(picturePath));
            }


/*
            new Thread() {
                public void run() {
                    UploadFile uploadFile = new UploadFile();
                    String result = uploadFile.uploadFile(HttpConstants.HTTP_UPLOAD_PIC_FILE, picturePath);

                    Log.e("seek", "upload file is " + result);
                    if (requestCode == REQUEST_ALBUM1) {
                        pic1_path = result;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pic1.setImageBitmap(CommonUtils.getimage(picturePath));
                            }
                        });

                    } else if (requestCode == REQUEST_ALBUM2) {
                        pic2_path = result;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pic2.setImageBitmap(CommonUtils.getimage(picturePath));
                            }
                        });
                    }
                }
            }.start();

*/
            return;
        }
    }
}
