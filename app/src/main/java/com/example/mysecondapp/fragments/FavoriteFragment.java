package com.example.mysecondapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapp.utils.BackendUtils;
import com.example.mysecondapp.models.EntryPost;
import com.example.mysecondapp.adapters.PostListAdapter;
import com.example.mysecondapp.R;
import com.example.mysecondapp.utils.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteFragment extends Fragment {
    private RecyclerView rv;
    private List<EntryPost> postData;
    private PostListAdapter postListAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hits_favorite_group, container, false);

        rv = (RecyclerView) view.findViewById(R.id.rv_list);
        postListAdapter = new PostListAdapter(getActivity());
        rv.setAdapter(postListAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        postData = new ArrayList<>();
        postListAdapter.setPostList(postData);
        fetchFavoriteList();
        return view;
    }

    private void fetchFavoriteList() {
        Map<String, String> query = new HashMap<>();
        query.put("username", UserInfo.userID);
        BackendUtils.get(getActivity(), "getstars", query, this::fetchFavoriteListCallback);
    }

    private void fetchFavoriteListCallback(JSONObject json) {
        postData.clear();
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                JSONArray arr = json.getJSONArray("entry");
                int length = arr.length();
                for (int i = 0; i < length; i++) {
                    JSONObject entry = arr.getJSONObject(i);
                    String group = entry.getString("group_name");
                    String title = entry.getString("title");
                    String content = entry.getString("content");
                    int hotIndex = length - entry.getInt("rank");
                    int postID = entry.getInt("post_id");
                    postData.add(new EntryPost(postID, title, hotIndex, group, content));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(postData);
        postListAdapter.setPostList(postData);
    }
}
