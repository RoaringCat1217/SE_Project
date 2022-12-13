package com.example.mysecondapp.models;

public class Post {
    private int postID;
    private String posterID;
    private String title;
    private String content;
    private int likes;
    private boolean liked;
    private boolean stared;
    private String time;

    public Post(int postID, String posterID, String title, String content, int likes, boolean liked, boolean stared, String time) {
        this.postID = postID;
        this.posterID = posterID;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.liked = liked;
        this.stared = stared;
        this.time = time;
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

    public int getLikes() {
        return this.likes;
    }

    public boolean isStared() {
        return stared;
    }

    public void toggleStared() {
        this.stared = !this.stared;
    }
}
