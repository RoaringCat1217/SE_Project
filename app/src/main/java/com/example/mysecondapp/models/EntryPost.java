package com.example.mysecondapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class EntryPost implements Comparable<EntryPost>, Parcelable {
    private int rank;
    private int postID;
    private String title;
    private int hotIndex;
    private String group;
    private String content;

    public EntryPost(int rank, int postID, String title, int hotIndex, String group, String content) {
        this.rank = rank;
        this.postID = postID;
        this.title = title;
        this.hotIndex = hotIndex;
        this.group = group;
        this.content = content;
    }

    protected EntryPost(Parcel in) {
        rank = in.readInt();
        postID = in.readInt();
        title = in.readString();
        hotIndex = in.readInt();
        group = in.readString();
        content = in.readString();
    }

    public static final Creator<EntryPost> CREATOR = new Creator<EntryPost>() {
        @Override
        public EntryPost createFromParcel(Parcel in) {
            return new EntryPost(in);
        }

        @Override
        public EntryPost[] newArray(int size) {
            return new EntryPost[size];
        }
    };

    @Override
    public int compareTo(EntryPost entryPost) {
        return this.rank - entryPost.rank;
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
        // groupId转换成tag
        return group;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(rank);
        parcel.writeInt(postID);
        parcel.writeString(title);
        parcel.writeInt(hotIndex);
        parcel.writeString(group);
        parcel.writeString(content);
    }
}
