package com.example.mysecondapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.mysecondapp.R;
import com.example.mysecondapp.models.CommentItem;
import com.example.mysecondapp.beans.ReplyDetailBean;

import java.util.List;

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
        final GroupHolder groupHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item, viewGroup, false);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        }else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        // 显示头像
        groupHolder.replyName.setText(commentItemList.get(groupPosition).getReplyName());
        groupHolder.replyTime.setText(commentItemList.get(groupPosition).getReplyTime());
        groupHolder.replyContent.setText(commentItemList.get(groupPosition).getReplyContent());

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
//        if(replyDetailBean!=null){
//            Log.e(TAG, "addTheReplyData: >>>>该刷新回复列表了:"+replyDetailBean.toString() );
//            if(commentBeanList.get(groupPosition).getReplyList() != null ){
//                commentBeanList.get(groupPosition).getReplyList().add(replyDetailBean);
//            }else {
//                List<ReplyDetailBean> replyList = new ArrayList<>();
//                replyList.add(replyDetailBean);
//                commentBeanList.get(groupPosition).setReplyList(replyList);
//            }
//            notifyDataSetChanged();
//        }else {
//            throw new IllegalArgumentException("回复数据为空!");
//        }
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
//        final ChildHolder childHolder;
//        if(convertView == null){
//            convertView = LayoutInflater.from(context).inflate(R.layout.comment_reply_item_layout,viewGroup, false);
//            childHolder = new ChildHolder(convertView);
//            convertView.setTag(childHolder);
//        }
//        else {
//            childHolder = (ChildHolder) convertView.getTag();
//        }
//
//        String replyUser = commentBeanList.get(groupPosition).getReplyList().get(childPosition).getNickName();
//        if(!TextUtils.isEmpty(replyUser)){
//            childHolder.tv_name.setText(replyUser + ":");
//        }
//
//        childHolder.tv_content.setText(commentBeanList.get(groupPosition).getReplyList().get(childPosition).getContent());

        return convertView;
    }

    @Override
    public int getChildrenCount(int i) {
        if(commentItemList.get(i).getReplyList() == null){
            return 0;
        }else {
            return commentItemList.get(i).getReplyList().size()>0 ? commentItemList.get(i).getReplyList().size():0;
        }

    }

    @Override
    public Object getChild(int i, int i1) {
        return commentItemList.get(i).getReplyList().get(i1);
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

    private void addReplyList(List<ReplyDetailBean> replyBeanList, int groupPosition){
        if(commentItemList.get(groupPosition).getReplyList() != null ){
            commentItemList.get(groupPosition).getReplyList().clear();
            commentItemList.get(groupPosition).getReplyList().addAll(replyBeanList);
        }else {
            commentItemList.get(groupPosition).setReplyList(replyBeanList);
        }

        notifyDataSetChanged();
    }

}
