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
import android.widget.TextView;
import android.widget.Toast;

import com.lamatech.seekserverproxy.ServerProxy;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.lamatech.date.R;

public class RegisterNextActivity extends AppCompatActivity {

    TextView phone;
    Button done;
    EditText code;
    String pwd;
    String mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_next);
        LinearLayout back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterNextActivity.this);
                builder.setMessage("验证码短信可能略有延迟，确定返回吗？");
                builder.setNegativeButton("等待", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

                builder.show();
            }
        });
        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);
        done = findViewById(R.id.done);

        pwd = getIntent().getStringExtra("pwd");
        mobile = getIntent().getStringExtra("phone");

        phone.setText(getIntent().getStringExtra("phone"));

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(code.length() >= 6) {
                    done.setBackgroundResource(R.drawable.login_ok_background);
                }
            }
        });

        new Thread() {
            public void run() {
                String result = ServerProxy.getPhoneCode(phone.getText().toString());
                if(result == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterNextActivity.this, "获取验证码失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                Log.e("date", "get phone code is " + result);
                try {
                    JSONObject res = new JSONObject(result);
                    if(res.getInt("code") == 400) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterNextActivity.this, "该手机号已注册，请直接登录", Toast.LENGTH_SHORT).show();
//                                setResult(100);
//                                finish();
                            }
                        });
                    } else {
                        JSONObject data = res.getJSONObject("data");
                        final String code = data.getString("code");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterNextActivity.this, "验证码已发送", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(code.length() < 6) {
                    Toast.makeText(RegisterNextActivity.this, "输入验证码位数不对", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread() {
                    public void run() {
                        String result = ServerProxy.checkPhoneCode(phone.getText().toString(), code.getText().toString());
                        Log.e("date", "get phone code is " + result);
                        if(result == null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterNextActivity.this, "验证失败，请重新提交", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        try {
                            JSONObject res = new JSONObject(result);
                            if(res.getInt("code") ==  200) {
                                Intent intent = new Intent(RegisterNextActivity.this, UserInfoActivity.class);
                                intent.putExtra("mobile", mobile);
                                intent.putExtra("pwd", pwd);
                                intent.putExtra("code", code.getText().toString());
                                startActivity(intent);
                                setResult(100);
                                finish();
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterNextActivity.this);
                                        builder.setMessage("验证码不正确");
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                        builder.show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }
}
