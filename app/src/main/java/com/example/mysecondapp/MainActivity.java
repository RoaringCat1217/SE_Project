package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.mysecondapp.MESSAGE";
    private EditText userID, userPwd;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        userID = (EditText) findViewById(R.id.text_userid);
        userPwd = (EditText) findViewById(R.id.text_userpwd);

    }

    public void login(View view) {
        String id = userID.getText().toString().trim();
        String pwd = userPwd.getText().toString().trim();
        try {
            String jsonStr = HttpLoginSignup(id, pwd, "login");
            try {
                JSONObject json = new JSONObject(jsonStr);
                long code = json.getLong("code");
                if (code == 1)
                    System.out.println("成功");
                else
                    System.out.println("登录失败, 用户名或密码错误!");
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void signup(View view) {
        String id = userID.getText().toString().trim();
        String pwd = userPwd.getText().toString().trim();
        try {
            String jsonStr = HttpLoginSignup(id, pwd, "register");
            try {
                JSONObject json = new JSONObject(jsonStr);
                long code = json.getLong("code");
                if (code == 1)
                    System.out.println("成功!");
                else
                    System.out.println("注册失败, 用户名已被注册!");
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String HttpLoginSignup(String id, String pwd, String port) throws IOException{
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse("http://47.93.251.137:3000/" + port).newBuilder();
        urlBuilder.addQueryParameter("username", id);
        urlBuilder.addQueryParameter("password", pwd);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        if (response.code() != 200)
            throw new IOException();
        return response.body().string();
    }
}