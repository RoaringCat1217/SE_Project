package com.example.mysecondapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapp.R;
import com.example.mysecondapp.activities.MainActivity;
import com.example.mysecondapp.fragments.GroupListFragment;
import com.example.mysecondapp.models.EntryGroup;
import com.example.mysecondapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>{
    private List<EntryGroup> groupList = new ArrayList<>();
    Context context;

    public GroupAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_group,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EntryGroup entry = groupList.get(position);
        //绑定数据
        holder.tvName.setText(entry.getName());

        holder.itemView.setOnClickListener(view -> {
            String group = groupList.get(position).getName();
            ((MainActivity)context).showGroupList(group);
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void setGroupList(List<EntryGroup> groupList) {
        this.groupList = groupList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CardView cv;
        public ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            cv = (CardView) v.findViewById(R.id.cv_all);
        }
    }
}