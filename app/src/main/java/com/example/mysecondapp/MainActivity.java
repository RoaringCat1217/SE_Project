package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.mysecondapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT > 9){
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        }
    }

    public void hello(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        String message = "connection failed";

        HttpURLConnection conn = null;
        String url = "http://47.93.251.137:3000/";
        try {
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();

            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);

            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
                InputStream is = conn.getInputStream();
                message = getStringFromInputStream(is);
            }
            else{
                message = "fuck";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null){
                conn.disconnect();
            }
        }

        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    private static String getStringFromInputStream(InputStream is) throws IOException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = is.read(buffer)) != -1){
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();
        os.close();
        System.out.println(state);
        return state;
    }
}