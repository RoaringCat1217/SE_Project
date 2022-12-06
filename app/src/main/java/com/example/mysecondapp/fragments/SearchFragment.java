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

        searchListData = new ArrayList<>();
        searchListAdapter.setPostList(searchListData);
        fetchSearchList();

        return view;
    }

    public void fetchSearchList() {
        // BackendUtils.get(getActivity(), "hot", null, this::fetchHotListCallback);
    }

    private void fetchSearchListCallback(JSONObject json) {
        searchListData.clear();
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {

            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(searchListData);
        searchListAdapter.setPostList(searchListData);
    }
}
