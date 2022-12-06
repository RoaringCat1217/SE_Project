package com.example.mysecondapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mysecondapp.utils.BackendUtils;
import com.example.mysecondapp.utils.LoginUtils;
import com.example.mysecondapp.R;
import com.example.mysecondapp.utils.UserInfo;

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
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this::register);
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this::login);
    }

    public void login(View view) {
        String id = etUserID.getText().toString().trim();
        String pwd = etUserPwd.getText().toString().trim();
        etUserID.setAutofillHints(id);
        etUserPwd.setAutofillHints(pwd);
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

    public void register(View view) {
        String id = etUserID.getText().toString().trim();
        String pwd = etUserPwd.getText().toString().trim();
        Map<String, String> query = new HashMap<>();
        query.put("username", id);
        query.put("password", pwd);
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
        UserInfo.userID = etUserID.getText().toString().trim();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LoginUtils.callback != null)
            LoginUtils.callback.run();
    }
}