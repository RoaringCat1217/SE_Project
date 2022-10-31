package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.mysecondapp.MESSAGE";
    private EditText etUserID, etUserPwd;
    private final OkHttpClient client = new OkHttpClient();

    private RecyclerView recyclerView;//声明RecyclerView
    private RecycleAdapterDome adapterDome;//声明适配器
    private Context context;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //开始设置RecyclerView
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //添加数据
        list = new ArrayList<>();
        for (int i=0;i<20;i++){
            list.add("这是第"+i+"个测试");
        }
        //
        adapterDome = new RecycleAdapterDome(context,list);
        /*
        与ListView效果对应的可以通过LinearLayoutManager来设置
        与GridView效果对应的可以通过GridLayoutManager来设置
        与瀑布流对应的可以通过StaggeredGridLayoutManager来设置
        */
        //LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        //创建线性布局
        LinearLayoutManager manager = new LinearLayoutManager(context);
        //垂直方向
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerView.LayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        //GridLayoutManager manager1 = new GridLayoutManager(context,2);
        //manager1.setOrientation(GridLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager manager2 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //给RecyclerView设置布局管理器
        recyclerView.setLayoutManager(manager);
        //创建适配器，并且设置
        recyclerView.setAdapter(adapterDome);
    }

//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login_screen);
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
//        etUserID = findViewById(R.id.etUserID);
//        etUserPwd = findViewById(R.id.etUserPwd);
//    }

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
                Toast.makeText(MainActivity.this, "网络连接出错!", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonStr = response.body().string();
                    try {
                        JSONObject json = new JSONObject(jsonStr);
                        long retCode = json.getLong("code");
                        if (retCode == 1) {
                            Toast.makeText(MainActivity.this, "登录成功!", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "登录失败! 账号或密码错误.", Toast.LENGTH_SHORT)
                                    .show();
                        }
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
                Toast.makeText(MainActivity.this, "网络连接出错!", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonStr = response.body().string();
                    try {
                        JSONObject json = new JSONObject(jsonStr);
                        long retCode = json.getLong("code");
                        if (retCode == 1) {
                            Toast.makeText(MainActivity.this, "注册成功!", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "注册失败! 用户名已存在.", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void go_back(View view) {
    }

    public void settings(View view) {
    }
}