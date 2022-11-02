package com.example.mysecondapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
        holder.usr_portrait.setImageResource(entry.getUsrPortrait());
        holder.usr_id.setText(entry.getUsrId());
        holder.post_title.setText(entry.getPostTitle());
        holder.post_content.setText(entry.getPostContent());
        holder.liking_number.setText(entry.getLikingNumber());

        //给点赞按钮添加监听
        if (onItemClickLitener != null) {
            holder.liking_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickLitener.onItemClick(view, position);
                }
            });
        }
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
        ImageView usr_portrait;
        TextView usr_id;
        TextView post_title;
        TextView post_content;
        ImageView liking_icon;
        TextView liking_number;

        public ViewHolder(@NonNull View v) {
            super(v);
            usr_portrait = v.findViewById(R.id.usr_portrait);
            usr_id = v.findViewById(R.id.usr_id);
            post_title = v.findViewById(R.id.post_title);
            post_content = v.findViewById(R.id.post_content);
            liking_icon = v.findViewById(R.id.liking_icon);
            liking_number = v.findViewById(R.id.liking_number);
        }
    }
}
