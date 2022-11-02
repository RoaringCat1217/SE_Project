package com.example.mysecondapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HotListRVAdapter extends RecyclerView.Adapter<HotListRVAdapter.ViewHolder>{

    private List<HotListEntry> hotList = new ArrayList<>();
    Context context;
    OnItemClickLitener onItemClickLitener;

    public HotListRVAdapter(Context context) {
        this.context = context;
    }

    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener){
        this.onItemClickLitener = onItemClickLitener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dome, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HotListEntry entry = hotList.get(position);
        //绑定数据
        // holder.ivUsrPortrait.setImageResource(entry.getUsrPortrait());
        holder.tvUsrID.setText(entry.getUsrId());
        holder.tvTitle.setText(entry.getTitle());
        holder.tvContent.setText(entry.getContent());
        holder.tvLikes.setText(String.format(Locale.getDefault(), "%d", entry.getLikes()));


        //给点赞按钮添加监听
        /*
        if (onItemClickLitener != null) {
            holder.ivLikeIcon.setOnClickListener(view -> onItemClickLitener.onItemClick(view, position));
        }
        */
    }

    @Override
    public int getItemCount() {
        return hotList.size();
    }

    public void setHotList(List<HotListEntry> hotList) {
        this.hotList = hotList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUsrPortrait;
        TextView tvUsrID;
        TextView tvTitle;
        TextView tvContent;
        ImageView ivLikeIcon;
        TextView tvLikes;

        public ViewHolder(@NonNull View v) {
            super(v);
            ivUsrPortrait = v.findViewById(R.id.ivUsrPortrait);
            tvUsrID = v.findViewById(R.id.tvUsrID);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvContent = v.findViewById(R.id.tvContent);
            ivLikeIcon = v.findViewById(R.id.ivLikeIcon);
            tvLikes = v.findViewById(R.id.tvLikes);
        }
    }
}
