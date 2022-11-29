package com.example.mysecondapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysecondapp.BackendUtils;
import com.example.mysecondapp.beans.CommentBean;
import com.example.mysecondapp.beans.CommentDetailBean;
import com.example.mysecondapp.adapters.CommentExpandAdapter;
import com.example.mysecondapp.models.Post;
import com.example.mysecondapp.R;
import com.example.mysecondapp.UserInfo;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PostDisplayActivity extends AppCompatActivity {
    public static final String POST_ID = "POST_ID";
    int postID;
    Post post;
    ImageView likeIcon;
    ImageView starIcon;
    TextView tvPosterID;
    TextView tvTitle;
    TextView tvContent;
    TextView tvLikes;

    private static final String TAG = "MainActivity";
    private TextView bt_comment;
    private ExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList;
    private BottomSheetDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 后端返回是否收藏的信息
        postID = getIntent().getIntExtra(POST_ID, -1);
        fetchPost(postID);

        setContentView(R.layout.post);

        tvPosterID = findViewById(R.id.usr_id);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvLikes = findViewById(R.id.like_number);
        likeIcon = findViewById(R.id.like_icon);
        starIcon = findViewById(R.id.star_icon);

        // 输出用户和帖子
        // ...
        likeIcon.setImageResource(R.drawable.thumbs_up_unselected); // 如果这个用户没点过赞
        // TODO: 点赞的返回数量没有, 加一个likes域
        likeIcon.setOnClickListener(v -> {
            like();
        });

        // TODO: 收藏逻辑
        starIcon.setImageResource(R.drawable.star_unselected); // 如果这个用户没收藏
        // 如果这个用户收藏了：starIcon.setImageResource(R.drawable.star_selected);
        // 输出收藏数
        // ...
        starIcon.setOnClickListener(new View.OnClickListener() {
            private boolean star = false; // 这个用户收藏还是没收藏
            @Override
            public void onClick(View v) {
                // 先告诉后端，成功了再继续
                if (!star) {
                    starIcon.setImageResource(R.drawable.star_selected);
                    star = true;
                    // 更改收藏数
                } else {
                    starIcon.setImageResource(R.drawable.star_unselected);
                    star = false;
                    // 更改收藏数
                }
            }
        });

        // 初始化评论界面
        initView();
    }

    private void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentDialog();
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // 评论列表
        commentsList = generateTestData();
        initExpandableListView(commentsList);
    }

    // 初始化评论和回复列表
    private void initExpandableListView(final List<CommentDetailBean> commentList) {
        expandableListView.setGroupIndicator(null);
        //默认展开所有的
        adapter = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        for (int i = 0; i < commentList.size(); i++) {
            expandableListView.expandGroup(i);
        }
        // 先认为评论没有回复，则不能点开
//        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
//                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
//                Log.e(TAG, "onGroupClick: 当前的评论id>>>" + commentList.get(groupPosition).getId());
////                if(isExpanded){
////                    expandableListView.collapseGroup(groupPosition);
////                }else {
////                    expandableListView.expandGroup(groupPosition, true);
////                }
//                showReplyDialog(groupPosition);
//                return true;
//            }
//        });
//
//        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
//                //Toast.makeText(, "点击了回复", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
    }

    private List<CommentDetailBean> generateTestData(){
//        Gson gson = new Gson();
//        commentBean = gson.fromJson(testJson, CommentBean.class);
        List<CommentDetailBean> commentList = commentBean.getData().getList();
        return commentList;
    }

    // 不知道这个是什么
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 弹出评论框：comment_dialog_layout
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        // 用户评论：点击了评论的发送按钮
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    CommentDetailBean detailBean = new CommentDetailBean("小明", commentContent,"刚刚");
                    adapter.addTheCommentData(detailBean);
                    // Toast.makeText(MainActivity.this,"评论成功",Toast.LENGTH_SHORT).show();

                }else {
                    // Toast.makeText(MainActivity.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 不知道这是啥
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    // 弹出回复框：comment_dialog_layout
    // 先无回复
//    private void showReplyDialog(final int position){
//        dialog = new BottomSheetDialog(this);
//        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
//        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
//        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
//        commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");
//        dialog.setContentView(commentView);
//        bt_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String replyContent = commentText.getText().toString().trim();
//                if(!TextUtils.isEmpty(replyContent)){
//
//                    dialog.dismiss();
//                    ReplyDetailBean detailBean = new ReplyDetailBean("小红",replyContent);
//                    adapter.addTheReplyData(detailBean, position);
//                    expandableListView.expandGroup(position);
//                    Toast.makeText(MainActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(MainActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        commentText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
//                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
//                }else {
//                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        dialog.show();
//    }


    private void like() {
        Map<String, String> query = new HashMap<>();
        query.put("user_name", UserInfo.userID);
        query.put("post_id", Integer.valueOf(postID).toString());
        if (post.isLiked())
            query.put("post_id", Integer.valueOf(-1).toString());
        else
            query.put("post_id", Integer.valueOf(1).toString());
        BackendUtils.get(this, "like", query, this::likeCallback);
    }

    private void likeCallback(JSONObject json) {
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                post.setLikes(json.getInt("likes"));
                post.toggleLiked();
                if (post.isLiked())
                    likeIcon.setImageResource(R.drawable.thumbs_up_selected);
                else
                    likeIcon.setImageResource(R.drawable.thumbs_up_unselected);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchPost(int postID) {
        Map<String, String> query = new HashMap<>();
        query.put("user_name", UserInfo.userID);
        // TODO: JSON数据类型问题？ 后端最好也返回post_id.
        query.put("post_id", Integer.valueOf(postID).toString());
        BackendUtils.get(this, "getpost", query, this::fetchPostCallback);
    }

    private void fetchPostCallback(JSONObject json) {
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                // TODO: 后端要返回发帖人ID和是否收藏
                String posterID = json.getString("poster");
                String title = json.getString("title");
                String content = json.getString("content");
                int likes = json.getInt("likes");
                boolean isLiked = json.getInt("liked") == 1;
                boolean isStared = false;
                post = new Post(postID, posterID, title, content, likes, isLiked, isStared);
                tvPosterID.setText(posterID);
                tvTitle.setText(title);
                tvContent.setText(content);
                tvLikes.setText(Integer.valueOf(likes).toString());

            }
            else
                Toast.makeText(this, "出错!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
