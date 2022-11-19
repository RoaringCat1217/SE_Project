package com.example.mysecondapp;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment {
    private RecyclerView recyclerView;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hits_favorite_group,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        List<EntryGroup> groupList = new ArrayList<>();
        groupList.add(new EntryGroup("学五"));
        groupList.add(new EntryGroup("信科"));
        groupList.add(new EntryGroup("软工"));
        groupList.add(new EntryGroup("夜奔"));
        groupList.add(new EntryGroup("三角地"));
        groupList.add(new EntryGroup("脱单"));

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        // 这里和热榜不一样，不另起一个文件专门写Adapter是因为板块逻辑简单一些
        GroupAdapter groupAdapter = new GroupAdapter(groupList);
        recyclerView.setAdapter(groupAdapter);

        // 设置监听事件：跳转到板块对应的帖子列表，在GroupActivity里面

//        groupAdapter.setOnItemClickListener(new GroupAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                // 跳到对应板块的帖子列表：GroupActivity
//                Toast.makeText(getContext(), "这是条目" + recyclerView.getBaseline(), Toast.LENGTH_LONG).show();
//            }
//        });

        groupAdapter.setOnItemClickListener(new GroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                try {
                    Intent intent = new Intent(view.getContext(), GroupListFragment.class);
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    static class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>{
        private List<EntryGroup> groupList;
        Context context;
        OnItemClickListener onItemClickListener;

        public interface OnItemClickListener{
            void onItemClick(View view, int position);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener){
            this.onItemClickListener = onItemClickListener;
        }

        public GroupAdapter(List<EntryGroup> groupList){
            this.groupList = groupList;
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

            //添加监听
            if (onItemClickListener != null) {
                holder.cv.setOnClickListener(view -> onItemClickListener.onItemClick(view, position));
            }
        }

        @Override
        public int getItemCount() {
            return groupList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvName;
            private CardView cv;
            public ViewHolder(View v) {
                super(v);
                tvName = (TextView) v.findViewById(R.id.tv_name);
                cv = (CardView) v.findViewById(R.id.cv_all);
            }
        }
    }
}
