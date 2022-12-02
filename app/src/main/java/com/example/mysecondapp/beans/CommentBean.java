package com.example.mysecondapp.beans;

import com.example.mysecondapp.models.CommentItem;

import java.util.List;
// 暂时没用 不过先别删
public class CommentBean {
    private int code;
    private String message;
    private Data data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }

    public class Data {

        private int total;
        private List<CommentItem> list;
        public void setTotal(int total) {
            this.total = total;
        }
        public int getTotal() {
            return total;
        }

        public void setList(List<CommentItem> list) {
            this.list = list;
        }
        public List<CommentItem> getList() {
            return list;
        }

    }

}
