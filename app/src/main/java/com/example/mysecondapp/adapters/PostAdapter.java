package com.example.mysecondapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapp.models.Constants;
import com.example.mysecondapp.models.EntryPost;
import com.example.mysecondapp.LoginUtils;
import com.example.mysecondapp.R;
import com.example.mysecondapp.activities.PostDisplayActivity;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private List<EntryPost> postList = new ArrayList<>();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EntryPost entry = postList.get(position);
        // TODO: 用URL请求头像
        // holder.ivUsrPortrait.setImageResource(entry.getUsrPortrait());
        holder.tvUsrID.setText(Integer.valueOf(entry.getPostID()).toString());
        holder.tvTitle.setText(entry.getTitle());
        holder.tvContent.setText(entry.getContent());
        holder.tvTag.setText(entry.getTag()); // 去掉xml里我写的“鹊桥”

        // 给浏览帖子绑定监听
        // holder.itemView.setOnClickListener(view -> Toast.makeText(context, "这是帖子" + holder.postToken + "的浏览", Toast.LENGTH_SHORT).show());
        holder.itemView.setOnClickListener(view -> {
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
        ImageView ivUsrPortrait;
        TextView tvUsrID;
        TextView tvTitle;
        TextView tvContent;
        TextView tvTag; // 去掉点赞按钮以后新加的

        public ViewHolder(@NonNull View v) {
            super(v);
            ivUsrPortrait = v.findViewById(R.id.ivUsrPortrait);
            tvUsrID = v.findViewById(R.id.tvUsrID);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvContent = v.findViewById(R.id.tvContent);
            tvTag = v.findViewById(R.id.tvTag);
        }
    }
}
