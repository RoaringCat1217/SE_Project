package com.example.mysecondapp.models;

import java.util.List;

public class CommentItem {
    private int replyID;
    private String replyName;
    private String replyTime;
    private String replyContent;
    private String repliedName;
    private String repliedContent;
    private int replyTotal;
    //private List<ReplyDetailBean> replyList;

    public CommentItem(int replyID, String replyName, String content, String repliedContent, String repliedName, String time) {
        this.replyID = replyID;
        this.replyName = replyName;
        this.replyContent = content;
        this.repliedContent = repliedContent;
        this.repliedName = repliedName;
        this.replyTime = time;
    }

    public int getId() {
        return this.replyID;
    }

    public String getReplyName() {
        return replyName;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public String getReplyTime() {
        return replyTime;
    }

}
