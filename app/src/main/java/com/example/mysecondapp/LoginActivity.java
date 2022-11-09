package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText etUserID, etUserPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        etUserID = findViewById(R.id.etUserID);
        etUserPwd = findViewById(R.id.etUserPwd);
        /*if(etUserID.getAutofillHints()[0] != ""){
            etUserID.setText(etUserID.getAutofillHints()[0]);
            etUserPwd.setText(etUserPwd.getAutofillHints()[0]);
        }*/
    }

    public void login(View view) {
        String id = etUserID.getText().toString().trim();
        String pwd = etUserPwd.getText().toString().trim();
        etUserID.setAutofillHints(id);
        etUserPwd.setAutofillHints(pwd);
        /*else{
            etUserID.setAutofillHints("");
            etUserPwd.setAutofillHints("");
        }*/
        Map<String, String> query = new HashMap<>();
        query.put("username", id);
        query.put("password", pwd);
        BackendUtils.get(this, "login", query, this::loginCallback);
    }

    private void loginCallback(JSONObject json) {
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                loginSuccess();
            }
            else
                Toast.makeText(LoginActivity.this, "登录失败! 账号或密码错误.", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void signup(View view) {
        String id = etUserID.getText().toString().trim();
        String pwd = etUserPwd.getText().toString().trim();
        Map<String, String> query = new HashMap<>();
        query.put("id", id);
        query.put("pwd", pwd);
        BackendUtils.get(this, "register", query, this::signupCallback);
    }

    private void signupCallback(JSONObject json) {
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                Toast.makeText(LoginActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
                loginSuccess();
            }
            else
                Toast.makeText(LoginActivity.this, "注册失败! 账号已存在.", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loginSuccess() {
        LoginUtils.login = true;
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LoginUtils.callback != null)
            LoginUtils.callback.run();
    }
}