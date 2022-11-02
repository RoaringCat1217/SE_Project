package com.example.mysecondapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HotListRVAdapter extends RecyclerView.Adapter<HotListRVAdapter.ViewHolder>{

    private List<HotListEntry> hotList = new ArrayList<>();

    public HotListRVAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dome, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(hotList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return hotList.size();
    }

    public void setHotList(List<HotListEntry> hotList) {
        this.hotList = hotList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
