package com.ferdu.chtgpt.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseModel2 {

    /**
     * id : cmpl-6OPCrwxRNG2J4Ofx1wrDc9WpZfsnS
     * object : text_completion
     * created : 1671274785
     * model : text-davinci-003
     * choices : [{"text":"\n\nString 是指字符串，是一种由字母、数字或符号组成的字符序列，一般用于表示文本信息。它是一种重要的数据","index":0,"log_probs":null,"finish_reason":"length"}]
     * usage : {"prompt_tokens":9,"completion_tokens":100,"total_tokens":109}
     */

    @SerializedName("id")
    private String id;
    @SerializedName("object")
    private String object;
    @SerializedName("created")
    private int created;
    @SerializedName("model")
    private String model;
    private ErrorParent errorParent;

    public ErrorParent getErrorParent() {
        return errorParent;
    }

    public void setErrorParent(ErrorParent errorParent) {
        this.errorParent = errorParent;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String errorMessage;

    public ResponseModel2(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * prompt_tokens : 9
     * completion_tokens : 100
     * total_tokens : 109
     */

    @SerializedName("usage")
    private UsageBean usage;
    /**
     * text :

     String 是指字符串，是一种由字母、数字或符号组成的字符序列，一般用于表示文本信息。它是一种重要的数据
     * index : 0
     * log_probs : null
     * finish_reason : length
     */

    @SerializedName("choices")
    private List<ChoicesBean> choices;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public UsageBean getUsage() {
        return usage;
    }

    public void setUsage(UsageBean usage) {
        this.usage = usage;
    }

    public List<ChoicesBean> getChoices() {
        return choices;
    }

    public void setChoices(List<ChoicesBean> choices) {
        this.choices = choices;
    }


    public static class UsageBean {
        @SerializedName("prompt_tokens")
        private int promptTokens;
        @SerializedName("completion_tokens")
        private int completionTokens;
        @SerializedName("total_tokens")
        private int totalTokens;

        public int getPromptTokens() {
            return promptTokens;
        }

        public void setPromptTokens(int promptTokens) {
            this.promptTokens = promptTokens;
        }

        public int getCompletionTokens() {
            return completionTokens;
        }

        public void setCompletionTokens(int completionTokens) {
            this.completionTokens = completionTokens;
        }

        public int getTotalTokens() {
            return totalTokens;
        }

        public void setTotalTokens(int totalTokens) {
            this.totalTokens = totalTokens;
        }
    }

    public static class ErrorParent{
        @SerializedName("error")
        private ErrorBean error;
        public ErrorBean getError() {
            return error;
        }

        public void setError(ErrorBean error) {
            this.error = error;
        }
    }
    public static class ChoicesBean {
        @SerializedName("text")
        private String text;
        @SerializedName("index")
        private int index;
        @SerializedName("log_probs")
        private Object logProbs;
        @SerializedName("finish_reason")
        private String finishReason;

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

        public Object getLogProbs() {
            return logProbs;
        }

        public void setLogProbs(Object logProbs) {
            this.logProbs = logProbs;
        }

        public String getFinishReason() {
            return finishReason;
        }

        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }
    }

    public static class ErrorBean {
        @SerializedName("message")
        protected String message;
        @SerializedName("type")
        protected String type;
        @SerializedName("param")
        protected Object param;
        @SerializedName("code")
        protected Object code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getParam() {
            return param;
        }

        public void setParam(Object param) {
            this.param = param;
        }

        public Object getCode() {
            return code;
        }

        public void setCode(Object code) {
            this.code = code;
        }
    }
}
