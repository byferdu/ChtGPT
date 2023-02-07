package com.ferdu.chtgpt.models.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;


import java.sql.Date;
import java.util.List;

@Entity
public class ChatThread {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String urlPrompt;

    private Date createTime=new Date(new java.util.Date().getTime());

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public ChatThread(String title, String urlPrompt) {
        this.title = title;
        this.urlPrompt = urlPrompt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlPrompt() {
        return urlPrompt;
    }

    public void setUrlPrompt(String urlPrompt) {
        this.urlPrompt = urlPrompt;
    }
}
