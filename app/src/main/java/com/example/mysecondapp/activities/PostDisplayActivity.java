package com.example.mysecondapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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

import com.example.mysecondapp.models.EntryPost;
import com.example.mysecondapp.utils.BackendUtils;
import com.example.mysecondapp.models.CommentItem;
import com.example.mysecondapp.adapters.CommentExpandAdapter;
import com.example.mysecondapp.utils.Constants;
import com.example.mysecondapp.models.Post;
import com.example.mysecondapp.R;
import com.example.mysecondapp.utils.UserInfo;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDisplayActivity extends AppCompatActivity {
    int postID;
    Post post;
    ImageView likeIcon;
    ImageView starIcon;
    TextView tvPosterID;
    TextView tvTitle;
    TextView tvContent;
    TextView tvTime;
    TextView tvLikes;
    CircleImageView userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postID = getIntent().getIntExtra(Constants.POST_ID, -1);

        setContentView(R.layout.post);
        tvPosterID = findViewById(R.id.usr_id);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvTime = findViewById(R.id.tv_time);
        tvLikes = findViewById(R.id.like_number);
        likeIcon = findViewById(R.id.like_icon);
        starIcon = findViewById(R.id.star_icon);
        userAvatar = findViewById(R.id.usr_portrait);

        likeIcon.setImageResource(R.drawable.thumbs_up_unselected); // 如果这个用户没点过赞
        likeIcon.setOnClickListener(v -> like());

        starIcon.setImageResource(R.drawable.star_unselected); // 如果这个用户没收藏
        starIcon.setOnClickListener(v -> star());

        fetchPost(postID);

        // 初始化评论界面
        initView();
    }

    private void fetchPost(int postID) {
        Map<String, String> query = new HashMap<>();
        query.put("username", UserInfo.userID);
        query.put("post_id", Integer.valueOf(postID).toString());
        BackendUtils.get(this, "getpost", query, this::fetchPostCallback);
    }

    private void fetchPostCallback(JSONObject json) {
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                String posterID = json.getString("poster_name");
                String title = json.getString("title");
                String content = json.getString("content");
                int likes = json.getInt("likes");
                boolean isLiked = json.getInt("liked") == 1;
                boolean isStared = json.getInt("stared") == 1;
                String time = json.getString("time");

                post = new Post(postID, posterID, title, content, likes, isLiked, isStared, time);
                tvPosterID.setText(posterID);
                tvTitle.setText(title);
                tvContent.setText(content);
                tvTime.setText(time);
                tvLikes.setText(Integer.valueOf(likes).toString());
                if (post.isLiked())
                    likeIcon.setImageResource(R.drawable.thumbs_up_selected);
                else
                    likeIcon.setImageResource(R.drawable.thumbs_up_unselected);
                if (post.isStared())
                    starIcon.setImageResource(R.drawable.star_selected);
                else
                    starIcon.setImageResource(R.drawable.star_unselected);
                downloadAvatar(posterID);
            }
            else
                Toast.makeText(this, "访问的帖子不存在!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private static final String TAG = "MainActivity";
    private TextView comment_button;
    private ExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private List<CommentItem> commentItemList;
    private BottomSheetDialog dialog;

    private void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.comment_list);
        comment_button = (TextView) findViewById(R.id.comment_button);
        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentDialog();
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // 显示评论列表
        fetchComment();
    }

    // 去后端拿评论数据
    private void fetchComment() {
        Map<String, String> query = new HashMap<>();
        query.put("post_id", Integer.valueOf(postID).toString());
        BackendUtils.get(this, "getreply", query, this::fetchCommentCallback);
    }

    private void fetchCommentCallback(JSONObject json) {
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                commentItemList = new ArrayList<>();
                JSONArray arr = json.getJSONArray("entry");
                int length = arr.length();
                for (int i = 0; i < length; i++) {
                    JSONObject entry = arr.getJSONObject(i);
                    int replyID = entry.getInt("reply_id");
                    String replyName = entry.getString("replyer_name");
                    String content = entry.getString("content");
                    String repliedContent = entry.getString("replied_content");
                    String repliedName = entry.getString("replied_name");
                    String time = entry.getString("reply_time");
                    commentItemList.add(new CommentItem(replyID, replyName, content, repliedContent, repliedName, time));
                }
                initExpandableListView();
            }
            else
                Toast.makeText(this, "获取评论出错!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 初始化评论列表
    // TODO: 还是只能显示demo用的一条评论
    private void initExpandableListView() {
        expandableListView.setGroupIndicator(null);
        adapter = new CommentExpandAdapter(this, commentItemList);
        expandableListView.setAdapter(adapter);
        for (int i = 0; i < commentItemList.size(); i++) {
            expandableListView.expandGroup(i);
        }
        // 点击某条评论：弹出输入框 发送后更新
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {

                Log.e(TAG, "onGroupClick: 当前的评论id>>>" + commentItemList.get(groupPosition).getId());

                showReplyDialog(groupPosition);
                return true;
            }
        });
    }

    // 弹出评论框, 评论整个帖子
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_box,null);
        EditText commentText = (EditText) commentView.findViewById(R.id.comment_text);
        Button commentSend = (Button) commentView.findViewById(R.id.comment_send);
        dialog.setContentView(commentView);

        commentSend.setOnClickListener(view -> {
            String commentContent = commentText.getText().toString().trim();
            if(!TextUtils.isEmpty(commentContent)){
                dialog.dismiss();
                Map<String, String> query = new HashMap<>();
                query.put("post_id", Integer.valueOf(postID).toString());
                query.put("username", UserInfo.userID);
                query.put("content", commentContent);
                BackendUtils.get(PostDisplayActivity.this, "newcomment", query, (BackendUtils.BackendCallback) json -> {
                    try {
                        long retCode = json.getLong("code");
                        if (retCode == 1) {
                            int replyID = json.getInt("post_id");
                            CommentItem commentItem = new CommentItem(replyID, "小明", commentContent, "小花", "帖子", "刚刚");
                            adapter.addCommentData(commentItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                Toast.makeText(PostDisplayActivity.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    // 弹出回复框, 评论某个评论
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_box,null);
        EditText commentText = (EditText) commentView.findViewById(R.id.comment_text);
        Button commentSend = (Button) commentView.findViewById(R.id.comment_send);
        commentText.setHint("回复 " + commentItemList.get(position).getReplyName() + " 的评论:");
        dialog.setContentView(commentView);

        commentSend.setOnClickListener(view -> {
            String commentContent = commentText.getText().toString().trim();
            if(!TextUtils.isEmpty(commentContent)){
                dialog.dismiss();
                Map<String, String> query = new HashMap<>();
                query.put("post_id", Integer.valueOf(commentItemList.get(position).getId()).toString());
                query.put("username", UserInfo.userID);
                query.put("content", commentContent);
                BackendUtils.get(PostDisplayActivity.this, "newcomment", query, (BackendUtils.BackendCallback) json -> {
                    try {
                        long retCode = json.getLong("code");
                        if (retCode == 1) {
                            int replyID = json.getInt("post_id");
                            CommentItem commentItem = new CommentItem(replyID, "小明", commentContent, "小花", "帖子", "刚刚");
                            adapter.addCommentData(commentItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                Toast.makeText(PostDisplayActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
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

    private void like() {
        Map<String, String> query = new HashMap<>();
        query.put("username", UserInfo.userID);
        query.put("post_id", Integer.valueOf(postID).toString());
        BackendUtils.get(this, "like", query, this::likeCallback);
    }

    private void likeCallback(JSONObject json) {
        if (post == null)
            return;
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                post.toggleLiked();
                if (post.isLiked())
                    likeIcon.setImageResource(R.drawable.thumbs_up_selected);
                else
                    likeIcon.setImageResource(R.drawable.thumbs_up_unselected);
                int likes = json.getInt("likes");
                post.setLikes(likes);
                tvLikes.setText(Integer.valueOf(post.getLikes()).toString());
            } else {
                Toast.makeText(this, "点赞失败, 请重试!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void star() {
        Map<String, String> query = new HashMap<>();
        query.put("username", UserInfo.userID);
        query.put("post_id", Integer.valueOf(postID).toString());
        BackendUtils.get(this, "star", query, this::starCallback);
    }

    private void starCallback(JSONObject json) {
        if (post == null)
            return;
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                post.toggleStared();
                if (post.isStared())
                    starIcon.setImageResource(R.drawable.star_selected);
                else
                    starIcon.setImageResource(R.drawable.star_unselected);
            } else {
                Toast.makeText(this, "收藏失败, 请重试!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void downloadAvatar(String posterID) {
        Map<String, String> query = new HashMap<>();
        query.put("user_name", posterID);
        BackendUtils.get(this, "getavatar", query, this::downloadAvatarCallback);
    }

    private void downloadAvatarCallback(JSONObject json) {
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                String imgStr = json.getString("image");
                if (imgStr.length() != 0) {
                    byte[] bitmapArray = Base64.decode(imgStr.split(",")[1], Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                    userAvatar.setImageBitmap(bitmap);
                }
            }
            else
                Toast.makeText(this, "出错!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
