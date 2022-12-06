package com.example.mysecondapp.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

public class BackendUtils {
    public interface BackendCallback {
        void run(JSONObject json);
    }
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .build();
    private static final String baseURL = "http://47.93.251.137:3000/";

    public static void get(Activity activity, String path, Map<String, String> query, BackendCallback callback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL + path).newBuilder();
        if (query != null) {
            for (Map.Entry<String, String> entry: query.entrySet())
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Connection", "close")
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(activity, "网络连接出错!", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Looper.prepare();
                    Toast.makeText(activity, "服务器端出错!", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    String jsonStr = response.body().string();
                    try {
                        JSONObject json = new JSONObject(jsonStr);
                        activity.runOnUiThread(()->{callback.run(json);});
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
