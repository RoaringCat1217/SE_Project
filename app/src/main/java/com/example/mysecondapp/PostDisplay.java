package com.example.mysecondapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PostDisplay extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        // 输出前请改一下xml，我在排版的时候填上了一些内容

        // 输出用户和帖子
        // ...

        ImageView likeIcon = findViewById(R.id.like_icon);
        likeIcon.setImageResource(R.drawable.thumbs_up_unselected); // 如果这个用户没点过赞
        // 如果这个用户点过赞：likeIcon.setImageResource(R.drawable.thumbs_up_selected);
        // 输出点赞数
        // ...
        likeIcon.setOnClickListener(new View.OnClickListener() {
            private boolean like = false; // 这个用户点赞还是没点赞
            @Override
            public void onClick(View v) {
                // 先告诉后端，成功了再继续
                if (!like) {
                    likeIcon.setImageResource(R.drawable.thumbs_up_selected);
                    like = true;
                    // 更改点赞数
                } else {
                    likeIcon.setImageResource(R.drawable.thumbs_up_unselected);
                    like = false;
                    // 更改点赞数
                }
            }
        });

        ImageView starIcon = findViewById(R.id.star_icon);
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
}
