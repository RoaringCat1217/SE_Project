package com.example.mysecondapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GroupListFragment extends Fragment {
    private RecyclerView rv;
    private List<EntryPost> groupListData;
    private PostAdapter groupListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getContext(), "这是板块", Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.fragment_hits_favorite_group, container, false);

        rv = (RecyclerView) view.findViewById(R.id.rv_list);
        groupListAdapter = new PostAdapter(getActivity());
        rv.setAdapter(groupListAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        groupListData = new ArrayList<>();
        groupListAdapter.setPostList(groupListData);
        fetchGroupList();

        return view;
    }

    private void fetchGroupList() {
    }

}
