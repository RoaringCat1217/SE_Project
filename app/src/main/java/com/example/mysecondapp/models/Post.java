package com.example.mysecondapp.models;

public class Post {
    private int postID;
    private String posterID;
    private String title;
    private String content;
    private int likes;
    private boolean liked;
    private boolean stared;

    public Post(int postID, String posterID, String title, String content, int likes, boolean liked, boolean stared) {
        this.postID = postID;
        this.posterID = posterID;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.liked = liked;
        this.stared = stared;
    }

    public boolean isLiked() {
        return liked;
    }

    public void toggleLiked() {
        this.liked = !this.liked;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isStared() {
        return stared;
    }



}
