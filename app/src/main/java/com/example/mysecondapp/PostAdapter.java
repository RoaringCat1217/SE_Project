package com.example.mysecondapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private List<EntryPost> hotList = new ArrayList<>();
    Context context;

    public PostAdapter(Context context) {
        this.context = context;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dome_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EntryPost entry = hotList.get(position);
        // 绑定数据
        // holder.ivUsrPortrait.setImageResource(entry.getUsrPortrait());
        holder.tvUsrID.setText(entry.getUsrId());
        holder.tvTitle.setText(entry.getTitle());
        holder.tvContent.setText(entry.getContent());
        holder.tvLikes.setText(String.format(Locale.getDefault(), "%d", entry.getLikes()));
        holder.setPostToken(position);
        // 给浏览帖子绑定监听
        holder.itemView.setOnClickListener(view -> Toast.makeText(context, "这是帖子" + holder.postToken + "的浏览", Toast.LENGTH_SHORT).show());
        // 给点赞按钮添加监听
        holder.ivLikeIcon.setOnClickListener(view -> LoginUtils.checkLogin(context, ()->Toast.makeText(context, "这是帖子" + holder.postToken + "的浏览", Toast.LENGTH_SHORT).show()));
    }

    @Override
    public int getItemCount() {
        return hotList.size();
    }

    public void setHotList(List<EntryPost> hotList) {
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
        private int postToken;

        public ViewHolder(@NonNull View v) {
            super(v);
            ivUsrPortrait = v.findViewById(R.id.ivUsrPortrait);
            tvUsrID = v.findViewById(R.id.tvUsrID);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvContent = v.findViewById(R.id.tvContent);
            ivLikeIcon = v.findViewById(R.id.ivLikeIcon);
            tvLikes = v.findViewById(R.id.tvLikes);
        }

        public void setPostToken(int postToken) {
            this.postToken = postToken;
        }
    }
}
