package com.ferdu.chtgpt.models;

public class Choice {
    public String text;
    public int index;
    public Object log_probs;
    public String finish_reason;
    public Choice() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Object getLog_probs() {
        return log_probs;
    }

    public void setLog_probs(Object log_probs) {
        this.log_probs = log_probs;
    }

    public String getFinish_reason() {
        return finish_reason;
    }

    public void setFinish_reason(String finish_reason) {
        this.finish_reason = finish_reason;
    }


}

