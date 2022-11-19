package com.example.mysecondapp;

public class EntryPost implements Comparable<EntryPost>{
    private String group;
    private int hotIndex;
    private int rank;
    private int usrPortrait;
    private String usrID = "test";
    private String title;
    private String content = "test";
    private int groupId;

    public EntryPost(String group, String title, int hotIndex, int rank, int groupId) {
        this.group = group;
        this.title = title;
        this.hotIndex = hotIndex;
        this.rank = rank;
        this.groupId = groupId;
    }

    public EntryPost(String group, int hotIndex, int rank,
                     int usrPortrait, String usrID, String title, String content,
                     int groupId) {
        this.group = group;
        this.hotIndex = hotIndex;
        this.rank = rank;
        this.usrPortrait = usrPortrait;
        this.usrID = usrID;
        this.title = title;
        this.content = content;
        this.groupId = groupId;
    }

    @Override
    public int compareTo(EntryPost entryPost) {
        return this.rank - entryPost.rank;
    }

    public int getUsrPortrait() {
        return usrPortrait;
    }

    public String getUsrId() {
        return usrID;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTag() {
        // groupId转换成tag
        return "";
    }
}
