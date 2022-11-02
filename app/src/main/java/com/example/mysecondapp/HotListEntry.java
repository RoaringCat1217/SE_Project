package com.example.mysecondapp;

public class HotListEntry implements Comparable<HotListEntry>{
    private String group;
    private int hotIndex;
    private int rank;

    public int usr_portrait;
    public String usr_id;
    public String post_title;
    public String post_content;
    public String liking_number;

    public HotListEntry(String group, int hotIndex, int rank,
                        int usr_portrait, String usr_id, String post_title, String post_content, String liking_number) {
        this.group = group;
        this.hotIndex = hotIndex;
        this.rank = rank;

        this.usr_portrait = usr_portrait;
        this.usr_id = usr_id;
        this.post_title = post_title;
        this.post_content = post_content;
        this.liking_number = liking_number;
    }

    @Override
    public int compareTo(HotListEntry hotListEntry) {
        return this.rank - hotListEntry.rank;
    }

    public int getUsrPortrait() {
        return usr_portrait;
    }

    public String getUsrId() {
        return usr_id;
    }

    public String getPostTitle() {
        return post_title;
    }

    public String getPostContent() {
        return post_content;
    }

    public String getLikingNumber() {
        return liking_number;
    }
}
