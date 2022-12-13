package com.example.mysecondapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapp.adapters.GroupAdapter;
import com.example.mysecondapp.models.EntryGroup;
import com.example.mysecondapp.R;
import com.example.mysecondapp.models.EntryPost;
import com.example.mysecondapp.utils.BackendUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupFragment extends Fragment {
    List<EntryGroup> groupList = new ArrayList<>();
    GroupAdapter groupAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hits_favorite_group,container,false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        groupAdapter = new GroupAdapter(getActivity());
        recyclerView.setAdapter(groupAdapter);
        fetchGroupList();

        return view;
    }

    private void fetchGroupList() {
        BackendUtils.get(getActivity(), "catalogue", null, this::fetchGroupListCallback);
    }

    private void fetchGroupListCallback(JSONObject json) {
        groupList.clear();
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                JSONArray arr = json.getJSONArray("groups");
                int length = arr.length();
                for (int i = 0; i < length; i++) {
                    String group = arr.getString(i);
                    groupList.add(new EntryGroup(group));
                }
            } else {
                Toast.makeText(getActivity(), "获取小组失败! 请刷新再试.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        groupAdapter.setGroupList(groupList);
    }
}
