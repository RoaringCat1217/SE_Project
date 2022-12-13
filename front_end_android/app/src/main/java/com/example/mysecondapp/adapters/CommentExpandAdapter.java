package com.example.mysecondapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysecondapp.R;
import com.example.mysecondapp.models.CommentItem;
import com.example.mysecondapp.utils.BackendUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentExpandAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "CommentExpandAdapter";
    private List<CommentItem> commentItemList;
    private Context context;

    public CommentExpandAdapter(Context context, List<CommentItem> commentItemList)               {
        this.context = context;
        this.commentItemList = commentItemList;
    }

    @Override
    public int getGroupCount() {
        return commentItemList.size();
    }

    @Override
    public Object getGroup(int i) {
        return commentItemList.get(i);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    // 展示所有评论
    @Override
    public View getGroupView(final int groupPosition, boolean isExpand, View convertView, ViewGroup viewGroup) {
//        final GroupHolder groupHolder;
//
//        if(convertView == null){
//            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item, viewGroup, false);
//            groupHolder = new GroupHolder(convertView);
//            convertView.setTag(groupHolder);
//        }else {
//            groupHolder = (GroupHolder) convertView.getTag();
//        }
        convertView = LayoutInflater.from(context).inflate(R.layout.comment_item, viewGroup, false);
        GroupHolder groupHolder = new GroupHolder(convertView);
        convertView.setTag(groupHolder);
        CommentItem item = commentItemList.get(groupPosition);
        // 显示除了头像外的东西
        groupHolder.replyName.setText(item.getReplyName());
        groupHolder.replyTime.setText(item.getReplyTime());
        groupHolder.replyContent.setText(item.getReplyContent());
        groupHolder.repliedName.setText(item.getRepliedName());
        groupHolder.repliedContent.setText(item.getRepliedContent());

        // 显示头像
        BackendUtils.getAvatar((AppCompatActivity) context, item.getReplyName(), img -> groupHolder.replyPortrait.setImageBitmap(img));


        return convertView;
    }

    private class GroupHolder{
        private CircleImageView replyPortrait;
        private TextView replyName, replyTime, repliedName, repliedContent, replyContent;
        public GroupHolder(View view) {
            replyPortrait =  view.findViewById(R.id.reply_portrait);
            replyName = view.findViewById(R.id.reply_name);
            replyTime = view.findViewById(R.id.reply_time);
            repliedName = view.findViewById(R.id.replied_name);
            repliedContent = view.findViewById(R.id.replied_content);
            replyContent = view.findViewById(R.id.reply_content);
        }
    }

    // 评论成功后插入评论
    public void addCommentData(CommentItem commentItem){
        if(commentItem!=null){
            commentItemList.add(commentItem);
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("评论数据为空!");
        }
    }

    // 以下是嵌套回复相关，都先不管

    public void addReplyData(CommentItem replyDetailBean, int groupPosition){
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        return convertView;
    }

    @Override
    public int getChildrenCount(int i) {
        return 0;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class ChildHolder{
        private TextView tv_name, tv_content;
        public ChildHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.reply_item_user);
            tv_content = (TextView) view.findViewById(R.id.reply_item_content);
        }
    }

}
