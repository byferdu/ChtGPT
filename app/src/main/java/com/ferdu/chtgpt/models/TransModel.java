package com.ferdu.chtgpt.models;

import com.google.gson.annotations.SerializedName;

public class TransModel {
    @SerializedName("type")
    private String type;
    @SerializedName("desc")
    private String desc;
    @SerializedName("text")
    private String text;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
