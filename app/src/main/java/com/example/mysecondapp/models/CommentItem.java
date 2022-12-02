package com.example.mysecondapp.models;

import com.example.mysecondapp.beans.ReplyDetailBean;

import java.util.List;

public class CommentItem {
    private int id;
    private String replyPortrait;
    private String replyName;
    private String replyTime;
    private String replyContent;
    private String repliedName;
    private String repliedContent;
    private int replyTotal;
    private List<ReplyDetailBean> replyList;

    public CommentItem(String name, String content, String time) {
        this.replyName = name;
        this.replyContent = content;
        this.replyTime = time;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setReplyName(String name) {
        this.replyName = name;
    }
    public String getReplyName() {
        return replyName;
    }

    public void setReplyContent(String content) {
        this.replyContent = content;
    }
    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyTime(String time) {
        this.replyTime = time;
    }
    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyList(List<ReplyDetailBean> replyList) {
        this.replyList = replyList;
    }
    public List<ReplyDetailBean> getReplyList() {
        return replyList;
    }
}
