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
        postAdapter.setPostList(postData);
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
                // TODO:
<<<<<<< HEAD:app/src/main/java/com/example/mysecondapp/FavoriteFragment.java
                //  likes not available
                // int likes = entry.getInt("likes");
                int likes = 0;
                postData.add(new EntryPost(group, title, hotIndex, rank, likes));
=======
                //  backend needs to return favorite list
                int id = entry.getInt("id");
                postData.add(new EntryPost(rank, id, title, hotIndex, group, "测试内容"));
>>>>>>> 8fdb83e9187c25a308cf539ed555ade53248151b:app/src/main/java/com/example/mysecondapp/fragments/FavoriteFragment.java
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(postData);
        postAdapter.setPostList(postData);
    }
}
