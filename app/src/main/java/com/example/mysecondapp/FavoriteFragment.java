package com.example.mysecondapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 这个和HitsFragment一模一样
public class FavoriteFragment extends Fragment {
    private RecyclerView rv;
    private List<EntryPost> postData;
    private PostAdapter postAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hits_favorite_group, container, false);

        rv = (RecyclerView) view.findViewById(R.id.rv_list);
        postAdapter = new PostAdapter(getActivity());
        rv.setAdapter(postAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        postData = new ArrayList<>();
        postAdapter.setHotList(postData);
        fetchFavoriteList();
        return view;
    }

    private void fetchFavoriteList() {
        BackendUtils.get(getActivity(), "hot", null, this::fetchFavoriteListCallback);
    }

    private void fetchFavoriteListCallback(JSONObject json) {
        postData.clear();
        try {
            JSONArray arr = json.getJSONArray("entry");
            int length = arr.length();
            for (int i = 0; i < length; i++) {
                JSONObject entry = arr.getJSONObject(i);
                String group = entry.getString("group_info");
                String title = entry.getString("title");
                int hotIndex = entry.getInt("hot_index");
                int rank = entry.getInt("rank");
                postData.add(new EntryPost(group, title, hotIndex, rank));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(postData);
        postAdapter.setHotList(postData);
    }
}
