package com.example.mysecondapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapp.BackendUtils;
import com.example.mysecondapp.models.EntryPost;
import com.example.mysecondapp.adapters.PostAdapter;
import com.example.mysecondapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HitsFragment extends Fragment {
    private RecyclerView rv;
    private List<EntryPost> hotListData;
    private PostAdapter hotListAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hits_favorite_group, container, false);

        rv = (RecyclerView) view.findViewById(R.id.rv_list);
        hotListAdapter = new PostAdapter(getActivity());
        rv.setAdapter(hotListAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        hotListData = new ArrayList<>();
        hotListAdapter.setPostList(hotListData);
        fetchHotList();

        return view;
    }

    public void fetchHotList() {
        BackendUtils.get(getActivity(), "hot", null, this::fetchHotListCallback);
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
                int id = entry.getInt("id");
                hotListData.add(new EntryPost(rank, id, title, hotIndex, group, "测试内容"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(hotListData);
        hotListAdapter.setPostList(hotListData);
    }

}
