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

import com.example.mysecondapp.BackendUtils;
import com.example.mysecondapp.models.CommentItem;
import com.example.mysecondapp.adapters.CommentExpandAdapter;
import com.example.mysecondapp.models.Constants;
import com.example.mysecondapp.models.Post;
import com.example.mysecondapp.R;
import com.example.mysecondapp.UserInfo;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    TextView tvLikes;
    CircleImageView userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 后端返回是否收藏的信息
        postID = getIntent().getIntExtra(Constants.POST_ID, -1);

        setContentView(R.layout.post);
        tvPosterID = findViewById(R.id.usr_id);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvLikes = findViewById(R.id.like_number);
        likeIcon = findViewById(R.id.like_icon);
        starIcon = findViewById(R.id.star_icon);
        userAvatar = findViewById(R.id.usr_portrait);

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
        fetchPost(postID);

        // 初始化评论界面
        initView();
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
        commentItemList = fetchData();
        initExpandableListView(commentItemList);
    }

    // 初始化评论列表
    private void initExpandableListView(final List<CommentItem> commentList) {
        expandableListView.setGroupIndicator(null);
        adapter = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        for (int i = 0; i < commentList.size(); i++) {
            expandableListView.expandGroup(i);
        }
        // 点击某条评论：弹出输入框 发送后更新
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                // 全部线性展开，没有分组展开了
                // boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: 当前的评论id>>>" + commentList.get(groupPosition).getId());
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                showReplyDialog(groupPosition);
                return true;
            }
        });
    }

    // 去后端拿评论数据
    private List<CommentItem> fetchData(){
        List<CommentItem> commentList = testData();
        return commentList;
    }
    private List<CommentItem> testData(){
        List<CommentItem> commentList = new ArrayList();
        commentList.add(new CommentItem("甲", "沙发", "刚刚"));
        commentList.add(new CommentItem("乙", "楼上牛逼", "刚刚"));
        return commentList;
    }

    // 弹出评论框：comment_box
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_box,null);
        EditText commentText = (EditText) commentView.findViewById(R.id.comment_text);
        Button commentSend = (Button) commentView.findViewById(R.id.comment_send);
        dialog.setContentView(commentView);

        commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){
                    dialog.dismiss();
                    // TODO:
                    // 直接在这个类里拿到tvTitle tvContent...
                    // 发给后端
                    // 后端回复以后展示commentItem
                    CommentItem commentItem = new CommentItem("小明", commentContent,"刚刚");
                    adapter.addCommentData(commentItem);
                }else {
                    Toast.makeText(PostDisplayActivity.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    // 弹出回复框：comment_box
    // 和showCommentDialog差不多
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_box,null);
        EditText commentText = (EditText) commentView.findViewById(R.id.comment_text);
        Button commentSend = (Button) commentView.findViewById(R.id.comment_send);
        commentText.setHint("回复 " + commentItemList.get(position).getReplyName() + " 的评论:");
        dialog.setContentView(commentView);

        commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){
                    dialog.dismiss();
                    // TODO:
                    // 通过position拿到被回复者的post_id、名字、内容等: commentItemList.get(position).get...()
                    // 发给后端
                    // 后端回复以后展示commentItem
                    CommentItem commentItem = new CommentItem("嘿嘿", commentContent,"刚刚");
                    adapter.addCommentData(commentItem);
                }else {
                    Toast.makeText(PostDisplayActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
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
        query.put("like", Integer.valueOf(1).toString());
        BackendUtils.get(this, "like", query, this::likeCallback);
    }

    private void likeCallback(JSONObject json) {
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                post.toggleLiked();
                if (post.isLiked())
                    likeIcon.setImageResource(R.drawable.thumbs_up_selected);
                else
                    likeIcon.setImageResource(R.drawable.thumbs_up_unselected);
                tvLikes.setText(Integer.valueOf(post.getLikes()).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchPost(int postID) {
        Map<String, String> query = new HashMap<>();
        query.put("user_name", UserInfo.userID);
        query.put("post_id", Integer.valueOf(postID).toString());
        BackendUtils.get(this, "getpost", query, this::fetchPostCallback);
    }

    private void fetchPostCallback(JSONObject json) {
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                String posterID = json.getString("poster");
                String title = json.getString("title");
                String content = json.getString("content");
                int likes = json.getInt("likes");
                boolean isLiked = json.getInt("liked") == 1;
                boolean isStared = json.getInt("stared") == 1;
                post = new Post(postID, posterID, title, content, likes, isLiked, isStared);
                tvPosterID.setText(posterID);
                tvTitle.setText(title);
                tvContent.setText(content);
                tvLikes.setText(Integer.valueOf(likes).toString());
                if (post.isLiked())
                    likeIcon.setImageResource(R.drawable.thumbs_up_selected);
                else
                    likeIcon.setImageResource(R.drawable.thumbs_up_unselected);
                downloadAvatar(posterID);
            }
            else
                Toast.makeText(this, "出错!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void downloadAvatar(String posterID) {
        Map<String, String> query = new HashMap<>();
        query.put("user_name", posterID);
        BackendUtils.get(this, "getavater", query, this::downloadAvatarCallback);
    }

    private void downloadAvatarCallback(JSONObject json) {
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                String imgStr = json.getString("image");
                byte[] bitmapArray = Base64.decode(imgStr.split(",")[1], Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                userAvatar.setImageBitmap(bitmap);
            }
            else
                Toast.makeText(this, "出错!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
