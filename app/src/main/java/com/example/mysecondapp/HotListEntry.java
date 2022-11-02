package com.example.mysecondapp;

public class HotListEntry implements Comparable<HotListEntry>{
    private String group;
    private int hotIndex;
    private int rank;
    private int usrPortrait;
    private String usrID = "test";
    private String title;
    private String content = "test";
    private int likes = -1;

    public HotListEntry(String group, String title, int hotIndex, int rank) {
        this.group = group;
        this.title = title;
        this.hotIndex = hotIndex;
        this.rank = rank;
    }

    public HotListEntry(String group, int hotIndex, int rank,
                        int usrPortrait, String usrID, String title, String content,
                        int likes) {
        this.group = group;
        this.hotIndex = hotIndex;
        this.rank = rank;
        this.usrPortrait = usrPortrait;
        this.usrID = usrID;
        this.title = title;
        this.content = content;
        this.likes = likes;
    }

    @Override
    public int compareTo(HotListEntry hotListEntry) {
        return this.rank - hotListEntry.rank;
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

    public int getLikes() {
        return likes;
    }
}
