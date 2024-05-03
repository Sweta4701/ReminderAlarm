package com.example.remainderalarm;

public class TableDataModel {
    private int srno;
    private String postTime;
    private String status;

    public TableDataModel() {
        this.srno = srno;
        this.postTime = postTime;
        this.status = status;
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
