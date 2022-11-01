package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        if (LoginUtils.checkForLogin(this, () -> onClick(view)))
            Toast.makeText(this, "已登录", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        if (data == null || resCode != Activity.RESULT_OK)
            return;
        String loginData = data.getStringExtra("key_login");
        if (!loginData.equals("from_login"))
            return;
        LoginUtils.loginSuccess(reqCode);
    }
}