package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginUtils extends AppCompatActivity {
    public static boolean isLogin = false;
    public static final String KEY_NEXT_FORWARD = "next_forward";
    private int nextForwardCode = -1;
    private EditText etUserID, etUserPwd;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nextForwardCode = getIntent().getIntExtra(KEY_NEXT_FORWARD, -1);
        setContentView(R.layout.login_screen);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        etUserID = findViewById(R.id.etUserID);
        etUserPwd = findViewById(R.id.etUserPwd);
    }

    public void login(View view) {
        String id = etUserID.getText().toString().trim();
        String pwd = etUserPwd.getText().toString().trim();
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse("http://47.93.251.137:3000/" + "login").newBuilder();
        urlBuilder.addQueryParameter("username", id);
        urlBuilder.addQueryParameter("password", pwd);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(LoginUtils.this, "网络连接出错!", Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonStr = response.body().string();
                    try {
                        JSONObject json = new JSONObject(jsonStr);
                        long retCode = json.getLong("code");
                        Looper.prepare();
                        if (retCode == 1) {
                            Toast.makeText(LoginUtils.this, "登录成功!", Toast.LENGTH_LONG)
                                    .show();
                            loginSuccess();
                        }
                        else {
                            Toast.makeText(LoginUtils.this, "登录失败! 账号或密码错误.", Toast.LENGTH_LONG)
                                    .show();
                        }
                        Looper.loop();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void signup(View view) {
        String id = etUserID.getText().toString().trim();
        String pwd = etUserPwd.getText().toString().trim();
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse("http://47.93.251.137:3000/" + "signup").newBuilder();
        urlBuilder.addQueryParameter("username", id);
        urlBuilder.addQueryParameter("password", pwd);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(LoginUtils.this, "网络连接出错!", Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonStr = response.body().string();
                    try {
                        JSONObject json = new JSONObject(jsonStr);
                        long retCode = json.getLong("code");
                        Looper.prepare();
                        if (retCode == 1) {
                            Toast.makeText(LoginUtils.this, "注册成功!", Toast.LENGTH_SHORT)
                                    .show();
                            loginSuccess();
                        }
                        else {
                            Toast.makeText(LoginUtils.this, "注册失败! 用户名已存在.", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        Looper.loop();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void loginSuccess() {
        isLogin = true;
        Intent intent = new Intent(LoginUtils.this, MainActivity.class);
        LoginUtils.this.startActivity(intent);
    }
}