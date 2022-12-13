package com.example.mysecondapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapp.R;
import com.example.mysecondapp.adapters.PostListAdapter;
import com.example.mysecondapp.models.EntryPost;
import com.example.mysecondapp.utils.BackendUtils;
import com.example.mysecondapp.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment {
    private RecyclerView rv;
    private List<EntryPost> searchListData;
    private PostListAdapter searchListAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hits_favorite_group, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv_list);
        searchListAdapter = new PostListAdapter(getActivity());
        rv.setAdapter(searchListAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle bundle = getArguments();
        assert bundle != null;
        String query = bundle.getString(Constants.SEARCH_KEYS);

        JSONArray keys = new JSONArray();
        for (String key: query.split("\\s+"))
            keys.put(key);
        JSONObject json = new JSONObject();
        try {
            json.put("keywords", keys);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        searchListData = new ArrayList<>();
        searchListAdapter.setPostList(searchListData);
        fetchSearchList(json);

        return view;
    }

    public void fetchSearchList(JSONObject json) {
        BackendUtils.post(getActivity(), "search", json, this::fetchSearchListCallback);
    }

    private void fetchSearchListCallback(JSONObject json) {
        searchListData.clear();
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                JSONArray arr = json.getJSONArray("entry");
                int length = arr.length();
                for (int i = 0; i < length; i++) {
                    JSONObject entry = arr.getJSONObject(i);
                    String title = entry.getString("title");
                    String content = entry.getString("content");
                    String group = entry.getString("group_name");
                    int hotIndex = length - i;
                    int postID = entry.getInt("post_id");
                    searchListData.add(new EntryPost(postID, title, hotIndex, group, content));
                }
            } else {
                Toast.makeText(getActivity(), "搜索失败! 请刷新再试.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(searchListData);
        searchListAdapter.setPostList(searchListData);
    }
}
