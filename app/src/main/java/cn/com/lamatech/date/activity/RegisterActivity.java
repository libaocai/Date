package cn.com.lamatech.date.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.lamatech.date.R;
import cn.com.lamatech.date.utils.CommonUtils;

public class RegisterActivity extends AppCompatActivity implements TextWatcher {

    EditText phone;
    EditText pwd;
    Button done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        LinearLayout back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        phone = findViewById(R.id.phone);
        pwd = findViewById(R.id.pwd);
        done = findViewById(R.id.done);

        phone.addTextChangedListener(this);
        pwd.addTextChangedListener(this);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.length() <= 0 || pwd.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "输入不全", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!CommonUtils.checkPhone(phone.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "输入手机号不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.length() < 6 || pwd.length() > 16) {
                    Toast.makeText(RegisterActivity.this, "密码输入不正确", Toast.LENGTH_SHORT).show();
                    return;
                }



                new Thread() {
                    public void run() {
                        String result = ServerProxy.checkRegister(phone.getText().toString());
                        if(result == null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "验证手机号失败", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, RegisterNextActivity.class);
                                    intent.putExtra("phone", phone.getText().toString());
                                    intent.putExtra("pwd", pwd.getText().toString());
                                    startActivityForResult(intent, 100);
                                }
                            });
                            return;
                        }
                        try {
                            JSONObject res = new JSONObject(result);
                            if (res.getInt("code") == 400) {
                                final String msg = res.getString("msg");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setMessage(msg);
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                        builder.setNegativeButton("取消", null);
                                        builder.show();
                                    }
                                });


                            } else {
                                Intent intent = new Intent(RegisterActivity.this, RegisterNextActivity.class);
                                intent.putExtra("phone", phone.getText().toString());
                                intent.putExtra("pwd", pwd.getText().toString());
                                startActivityForResult(intent, 100);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (phone.length() > 1 && pwd.length() >= 6) {
            done.setBackgroundResource(R.drawable.login_ok_background);
        } else {
            done.setBackgroundResource(R.drawable.login_background);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            finish();
        }
    }
}
