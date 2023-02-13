package com.ferdu.chtgpt.models.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Date;


@Entity
public class ChatModel {
    @PrimaryKey(autoGenerate = true)
    private int id ;
    private int chatThreadId;
    private String meText;
    private Date createTime=new Date(new java.util.Date().getTime());
    private String AIText;
    private String AITransText;
    private String model;
    private int prompt_tokens;
    private int completion_tokens = 0;
    private int total_tokens;
    public int getPrompt_tokens() {
        return prompt_tokens;
    }

    public void setPrompt_tokens(int prompt_tokens) {
        this.prompt_tokens = prompt_tokens;
    }

    public int getCompletion_tokens() {
        return completion_tokens;
    }

    public void setCompletion_tokens(int completion_tokens) {
        this.completion_tokens = completion_tokens;
    }

    public int getTotal_tokens() {
        return total_tokens;
    }

    public void setTotal_tokens(int total_tokens) {
        this.total_tokens = total_tokens;
    }

    public String getAITransText() {
        return AITransText;
    }

    public void setAITransText(String AITransText) {
        this.AITransText = AITransText;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Ignore
    public ChatModel(int chatThreadId, String meText, String AIText) {
        this.chatThreadId = chatThreadId;
        this.meText = meText;
        this.AIText = AIText;
    }

    public ChatModel(int chatThreadId, String meText, String AIText, int prompt_tokens, int completion_tokens,
                     int total_tokens,String model) {
        this.chatThreadId = chatThreadId;
        this.meText = meText;
        this.AIText = AIText;
        this.prompt_tokens = prompt_tokens;
        this.completion_tokens = completion_tokens;
        this.total_tokens = total_tokens;
        this.model = model;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChatThreadId() {
        return chatThreadId;
    }

    public void setChatThreadId(int chatThreadId) {
        this.chatThreadId = chatThreadId;
    }

    public String getMeText() {
        return meText;
    }

    public void setMeText(String meText) {
        this.meText = meText;
    }

    public String getAIText() {
        return AIText;
    }

    public void setAIText(String AIText) {
        this.AIText = AIText;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
