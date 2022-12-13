package com.example.mysecondapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapp.models.EntryPost;
import com.example.mysecondapp.adapters.PostListAdapter;
import com.example.mysecondapp.R;
import com.example.mysecondapp.utils.BackendUtils;
import com.example.mysecondapp.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GroupListFragment extends Fragment {
    private String group;
    private RecyclerView rv;
    private List<EntryPost> groupListData;
    private PostListAdapter groupListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hits_favorite_group, container, false);

        rv = (RecyclerView) view.findViewById(R.id.rv_list);
        groupListAdapter = new PostListAdapter(getActivity());
        rv.setAdapter(groupListAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle bundle = getArguments();
        assert bundle != null;
        group = bundle.getString(Constants.GROUP_NAME);

        groupListData = new ArrayList<>();
        groupListAdapter.setPostList(groupListData);
        fetchGroupList();

        return view;
    }

    public void fetchGroupList() {
        Map<String, String> query = new HashMap<>();
        query.put("group_name", group);
        BackendUtils.get(getActivity(), "group", query, this::fetchGroupListCallback);
    }

    private void fetchGroupListCallback(JSONObject json) {
        groupListData.clear();
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                JSONArray arr = json.getJSONArray("entry");
                int length = arr.length();
                for (int i = 0; i < length; i++) {
                    JSONObject entry = arr.getJSONObject(i);
                    String title = entry.getString("title");
                    String content = entry.getString("content");
                    int hotIndex = length - i;
                    int postID = entry.getInt("post_id");
                    groupListData.add(new EntryPost(postID, title, hotIndex, group, content));
                }
            } else {
                Toast.makeText(getActivity(), "获取版面失败! 请刷新再试.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(groupListData);
        groupListAdapter.setPostList(groupListData);
    }

}
