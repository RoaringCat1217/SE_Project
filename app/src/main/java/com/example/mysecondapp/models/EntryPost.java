package com.example.mysecondapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class EntryPost implements Comparable<EntryPost> {
    private int postID;
    private String title;
    private int hotIndex;
    private String group;
    private String content;

    public EntryPost(int postID, String title, int hotIndex, String group, String content) {
        this.postID = postID;
        this.title = title;
        this.hotIndex = hotIndex;
        this.group = group;
        this.content = content;
    }

    @Override
    public int compareTo(EntryPost entryPost) {
        return entryPost.hotIndex - this.hotIndex;
    }

    public int getPostID() {
        return postID;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTag() {
        return group;
    }
}
