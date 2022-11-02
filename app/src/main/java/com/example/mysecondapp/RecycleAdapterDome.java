package com.example.mysecondapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
① 创建一个继承RecyclerView.Adapter<VH>的Adapter类
② 创建一个继承RecyclerView.ViewHolder的静态内部类
③ 在Adapter中实现3个方法：
   onCreateViewHolder()
   onBindViewHolder()
   getItemCount()
*/
public class RecycleAdapterDome extends RecyclerView.Adapter<RecycleAdapterDome.MyViewHolder>{
        private Context context;
        private List<String> list;
        private View inflater;
        //构造方法，传入数据,即把展示的数据源传进来，并且复制给一个全局变量，以后的操作都在该数据源上进行
        public RecycleAdapterDome(Context context, List<String> list){
                this.context = context;
                this.list = list;
        }
        //由于RecycleAdapterDome继承自RecyclerView.Adapter,则必须重写onCreateViewHolder()，onBindViewHolder()，getItemCount()
        //onCreateViewHolder()方法用于创建ViewHolder实例，我们在这个方法将item_dome.xml布局加载进来
        //然后创建一个ViewHolder实例，并把加载出来的布局传入到构造函数，最后将实例返回
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                //创建ViewHolder，返回每一项的布局
                inflater = LayoutInflater.from(context).inflate(R.layout.item_dome,parent,false);
                return new MyViewHolder(inflater);
        }
        //onBindViewHolder()方法用于对RecyclerView子项数据进行赋值，会在每个子项被滚动到屏幕内的时候执行
        //这里我们通过position参数的得到当前项的实例，然后将数据设置到ViewHolder的TextView即可
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
                //将数据和控件绑定
                holder.textView.setText(list.get(position));
        }
        //getItemCount()告诉RecyclerView一共有多少个子项，直接返回数据源的长度。
        @Override
        public int getItemCount() {
                //返回Item总条数
                return list.size();
        }

        //内部类，绑定控件
        class MyViewHolder extends RecyclerView.ViewHolder{
                TextView textView;
                public MyViewHolder(View itemView) {//这个view参数就是recyclerview子项的最外层布局
                        super(itemView);
                        //可以通过findViewById方法获取布局中的TextView
                        textView = (TextView) itemView.findViewById(R.id.tvTitle);
                }
        }
}

