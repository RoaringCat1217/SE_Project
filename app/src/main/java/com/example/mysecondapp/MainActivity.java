package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import org.json.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvHotList;
    private List<HotListEntry> hotListData;
    private HotListRVAdapter hotListAdapter;

    private void fetchHotList() {
        BackendUtils.get(this, "hot", null, this::fetchHotListCallback);
    }

    private void fetchHotListCallback(JSONObject json) {
        hotListData.clear();
        try {
            JSONArray arr = json.getJSONArray("entry");
            int length = arr.length();
            for (int i = 0; i < length; i++) {
                JSONObject entry = arr.getJSONObject(i);
                String group = entry.getString("group_info");
                String title = entry.getString("title");
                int hotIndex = entry.getInt("hot_index");
                int rank = entry.getInt("rank");
                hotListData.add(new HotListEntry(group, title, hotIndex, rank));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(hotListData);
        hotListAdapter.setHotList(hotListData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvHotList = findViewById(R.id.rvHotList);
        hotListData = new ArrayList<>();
        hotListAdapter = new HotListRVAdapter();
        hotListAdapter.setHotList(hotListData);
        rvHotList.setAdapter(hotListAdapter);
        rvHotList.setLayoutManager(new LinearLayoutManager(this));
        fetchHotList();
    }



    public void go_back(View view) {
    }

    public void settings(View view) {
    }
}