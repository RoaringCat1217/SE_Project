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
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapp.utils.Constants;
import com.example.mysecondapp.models.EntryPost;
import com.example.mysecondapp.utils.LoginUtils;
import com.example.mysecondapp.R;
import com.example.mysecondapp.activities.PostDisplayActivity;

import java.util.ArrayList;
import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder>{

    private List<EntryPost> postList = new ArrayList<>();
    Context context;

    public PostListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EntryPost entry = postList.get(position);
        holder.tvTitle.setText(entry.getTitle());
        holder.tvContent.setText(entry.getContent());
        holder.tvTag.setText(entry.getTag());

        holder.itemView.setOnClickListener(view -> {
            // 浏览帖子页面前要登录
            LoginUtils.checkLogin(context, ()->{
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.POST_ID, entry.getPostID());
                try {
                    Intent intent = new Intent(view.getContext(), PostDisplayActivity.class);
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setPostList(List<EntryPost> hotList) {
        this.postList = hotList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        TextView tvTag;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvContent = v.findViewById(R.id.tvContent);
            tvTag = v.findViewById(R.id.tvTag);
        }
    }
}
