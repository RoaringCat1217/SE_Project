package com.example.mysecondapp;

public class HotListEntry implements Comparable<HotListEntry>{
    private String group;
    private String title;
    private int hotIndex;
    private int rank;

    public HotListEntry(String group, String title, int hotIndex, int rank) {
        this.group = group;
        this.title = title;
        this.hotIndex = hotIndex;
        this.rank = rank;
    }

    @Override
    public int compareTo(HotListEntry hotListEntry) {
        return this.rank - hotListEntry.rank;
    }

    public String getTitle() {
        return title;
    }
}
