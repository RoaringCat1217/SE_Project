package com.example.mysecondapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysecondapp.BackendUtils;
import com.example.mysecondapp.models.Post;
import com.example.mysecondapp.R;
import com.example.mysecondapp.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    }

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
