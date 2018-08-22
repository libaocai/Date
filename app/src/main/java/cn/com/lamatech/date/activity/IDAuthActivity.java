package cn.com.lamatech.date.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lamatech.seekserverproxy.HttpConstants;
import com.lamatech.seekserverproxy.UploadFile;
import com.lling.photopicker.PhotoPickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;
import cn.com.lamatech.date.utils.CommonUtils;

public class IDAuthActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView pic1;
    ImageView pic2;
    Button send;


    String pic1_path = "";
    String pic2_path = "";

    private static final int REQUEST_CAMMER = 1;// 摄像头的请求
    private static final int REQUEST_ALBUM1 = 2;// 相册的请求
    private static final int REQUEST_ALBUM2 = 3;// 相册的请求
    private static final int REQUEST_ALBUM3 = 4;// 相册的请求

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idauth);
        LinearLayout back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pic1 = findViewById(R.id.pic1);
        pic2 = findViewById(R.id.pic2);
        send = findViewById(R.id.send);

        pic1.setOnClickListener(this);
        pic2.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pic1:
                Intent intent = new Intent(IDAuthActivity.this, PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
                intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 1);
                startActivityForResult(intent, REQUEST_ALBUM1);
                break;
            case R.id.pic2:
                Intent intent1 = new Intent(IDAuthActivity.this, PhotoPickerActivity.class);
                intent1.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                intent1.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
                intent1.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 1);
                startActivityForResult(intent1, REQUEST_ALBUM2);
                break;
            case R.id.send:
                if("".equals(pic1_path) || "".equals(pic2_path)) {
                    Toast.makeText(this, "请选择两张图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadPic();
                break;
        }
    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_ALBUM1 || requestCode == REQUEST_ALBUM2 || requestCode == REQUEST_ALBUM3) {
            if (data == null) {
                if (requestCode == REQUEST_ALBUM1) {
                    pic1.setImageResource(R.mipmap.new072);
                    pic1_path = "";
                } else if (requestCode == REQUEST_ALBUM2) {
                    pic2.setImageResource(R.mipmap.new072);
                    pic2_path = "";
                }
                return;
            }

            ArrayList<String> tmpResult = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
            final String picturePath = tmpResult.get(0);
            Log.e("date", "picturePath:" + picturePath);
            if (requestCode == REQUEST_ALBUM1) {
                pic1_path = picturePath;
                pic1.setImageBitmap(CommonUtils.getimage(picturePath));
            }
            if (requestCode == REQUEST_ALBUM2) {
                pic2_path = picturePath;
                pic2.setImageBitmap(CommonUtils.getimage(picturePath));
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

    void uploadPic() {
        new Thread() {
            public void run() {
                UploadFile uploadFile = new UploadFile();
                HashMap map = new HashMap();
                if(Date.mUserInfo == null) {
                    return;
                }
                map.put("user_id", Date.mUserInfo.userid);

                String[] paths = {pic1_path, pic2_path};
                String result = uploadFile.uploadFile(HttpConstants.HTTP_HEAD +HttpConstants.HTTP_IP + "/Api/User/identityAuth", paths, map);
                Log.e("date", "id auth result is " + result);
                if(result == null) {
                    return;
                }
                try {

                    JSONObject res = new JSONObject(result);
                    if(res.getInt("code") == 200) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(IDAuthActivity.this, "上传成功，等待工作人员审核", Toast.LENGTH_SHORT).show();
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
}
