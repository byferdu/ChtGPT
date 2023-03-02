package com.ferdu.chtgpt.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestModel {

    @SerializedName("temperature")
    private float temperature;
    @SerializedName("top_p")
    private float topP;
    @SerializedName("n")
    private Integer n;
    @SerializedName("stream")
    private Boolean stream;
    @SerializedName("stop")
    private String[] stop;
    @SerializedName("max_tokens")
    private int max_tokens;

    public int getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }

    @SerializedName("model")
    private String model;
    @SerializedName("messages")
    private List<MessagesDTO> messages;

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getTopP() {
        return topP;
    }

    public void setTopP(float topP) {
        this.topP = topP;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public void setMessages(List<MessagesDTO> messages) {
        this.messages = messages;
    }


    public String[] getStop() {
        return stop;
    }

    public void setStop(String[] stop) {
        this.stop = stop;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<MessagesDTO> getMessages() {
        return messages;
    }



    public static class MessagesDTO {
        @SerializedName("role")
        private String role;
        @SerializedName("content")
        private String content;

        public MessagesDTO(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
