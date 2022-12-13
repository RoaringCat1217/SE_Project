package com.example.mysecondapp.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Base64;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

class ContentLengthIntercepter implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request.newBuilder().addHeader("Connection", "Close").build());
        return response.newBuilder().removeHeader("Content-Length").addHeader("Content-Length", "-1").build();
    }
}

public class BackendUtils {

    public interface BackendCallback {
        void run(JSONObject json);
    }

    public interface AvatarCallback {
        void run(Bitmap img);
    }

    private static final String baseURL = "http://47.93.251.137:3000/";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void get(Activity activity, String path, Map<String, String> query, BackendCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ContentLengthIntercepter())
                .retryOnConnectionFailure(true)
                .build();
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

    public static void post(Activity activity, String path, JSONObject json, BackendCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ContentLengthIntercepter())
                .retryOnConnectionFailure(true)
                .build();
        RequestBody body = RequestBody.create(JSON, json.toString());
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL + path).newBuilder();
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
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

    public static void getAvatar(Activity activity, String userID, AvatarCallback callback) {
        new Thread(() -> {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL + "getavatar").newBuilder();
            urlBuilder.addQueryParameter("username", userID);
            String urlStr = urlBuilder.build().toString();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Connection", "Closed");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(60000);
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null)
                        buffer.append(line);
                    String jsonStr = buffer.toString();
                    in.close();
                    JSONObject json = new JSONObject(jsonStr);
                    long retCode = json.getLong("code");
                    if (retCode == 1) {
                        String imgStr = json.getString("image");
                        if (imgStr != null && imgStr.length() > 2) {
                            byte[] bitmapArray = Base64.decode(imgStr.split(",")[1], Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                            activity.runOnUiThread(()->{callback.run(bitmap);});
                        }
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
