package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

    public void go_back(View view) {
    }

    public void settings(View view) {
    }
}