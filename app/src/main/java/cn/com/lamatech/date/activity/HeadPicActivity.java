package cn.com.lamatech.date.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import cn.com.lamatech.date.Date;
import cn.com.lamatech.date.R;

public class HeadPicActivity extends AppCompatActivity {

    final int IMAGE_REQUEST_CODE = 99;
    final int REQUEST_CAMERA = 999;
    Button photo_button;
    Button camera_button;
    ImageView head_pic;
    String path;

    File file;
    boolean  isFirst = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_pic);

        LinearLayout back_layout = findViewById(R.id.back_layout);
        isFirst = getIntent().getBooleanExtra("first", false);
        if(getIntent().getBooleanExtra("first", false)) {
            back_layout.setVisibility(View.GONE);
        }
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        photo_button = findViewById(R.id.photo);
        camera_button = findViewById(R.id.camera);
        head_pic = findViewById(R.id.head_pic);

        photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyWritePermission();
            }
        });

    }

    public void applyWritePermission() {

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= 23) {
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //调用相机
                useCamera();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {
            useCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            useCamera();
        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 使用相机
     */
    private void useCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/test/" + System.currentTimeMillis() + ".jpg");
        file.getParentFile().mkdirs();

        //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
        Uri uri = FileProvider.getUriForFile(this, "cn.com.lamatech.date.fileprovider", file);
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //在相册里面选择好相片之后调回到现在的这个activity中
        switch (requestCode) {
            case IMAGE_REQUEST_CODE://这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        path = cursor.getString(columnIndex);  //获取照片路径
                        Log.e("date", "path is " + path);
                        uploadPic();
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        head_pic.setImageBitmap(bitmap);
                        Date.head_pic = bitmap;
                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_CAMERA:
                head_pic.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                Date.head_pic = BitmapFactory.decodeFile(file.getAbsolutePath());

                path = file.getAbsolutePath();
                uploadPic();

                break;
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
                map.put("type", "head_pic");
                if(Date.mUserInfo.head_pic.length() == 0) {
                    map.put("action", "add");
                } else {
                    map.put("action", "edit");
                }

                String result = uploadFile.uploadFile(HttpConstants.HTTP_UPLOAD_PIC_FILE, path, map);
                Log.e("date", "upload head_pic result is " + result);
                if(result == null) {
                    return;
                }
                try {

                    JSONObject res = new JSONObject(result);
                    if(res.getInt("code") == 200) {

                        JSONObject data = res.getJSONObject("data");
                        Log.e("date","head_pic is " + data.getString("head_pic").replace("\\", ""));
                        if(Date.mUserInfo != null) {
                            Date.mUserInfo.head_pic = data.getString("head_pic");
                            Bitmap tmpBitmap = null;
                            try {
                                InputStream is = new java.net.URL(HttpConstants.HTTP_HEAD_PIC + Date.mUserInfo.head_pic).openStream();
                                tmpBitmap = BitmapFactory.decodeStream(is);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Date.mUserInfo.setHeadPicBm(tmpBitmap);
                        }
                        if(isFirst) {
                            if(Date.mUserInfo.gender.equals("2")) {
                                Intent intent = new Intent(HeadPicActivity.this, VideoCatchActivity.class);
                                intent.putExtra("first", true);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(HeadPicActivity.this, MainActivity.class);
                                intent.putExtra("first", true);
                                startActivity(intent);
                            }
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
