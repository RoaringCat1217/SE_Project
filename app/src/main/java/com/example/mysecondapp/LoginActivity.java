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

public class LoginActivity extends AppCompatActivity {
    private EditText etUserID, etUserPwd;
    private final OkHttpClient client = new OkHttpClient();
    public static final String KEY_LOGIN = "key_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Toast.makeText(LoginActivity.this, "网络出错, 请检查网络连接!", Toast.LENGTH_SHORT)
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
                            Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT)
                                    .show();
                            runOnUiThread(() -> {loginSuccess();});
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "登录失败! 账号或密码错误.", Toast.LENGTH_SHORT)
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
                = HttpUrl.parse("http://47.93.251.137:3000/" + "register").newBuilder();
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
                Toast.makeText(LoginActivity.this, "网络连接出错!", Toast.LENGTH_SHORT)
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
                            Toast.makeText(LoginActivity.this, "注册成功!", Toast.LENGTH_SHORT)
                                    .show();
                            runOnUiThread(() -> {loginSuccess();});
                            loginSuccess();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "注册失败! 用户名已存在.", Toast.LENGTH_SHORT)
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
        Intent intent = new Intent();
        intent.putExtra(KEY_LOGIN, "from_login");
        setResult(RESULT_OK, intent);
        finish();
    }
}