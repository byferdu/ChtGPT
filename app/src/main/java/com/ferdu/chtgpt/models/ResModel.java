package com.ferdu.chtgpt.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResModel {

    @SerializedName("id")
    private String id;
    @SerializedName("object")
    private String object;
    @SerializedName("created")
    private Integer created;
    @SerializedName("choices")
    private List<ChoicesDTO> choices;
    @SerializedName("usage")
    private UsageDTO usage;
   private String errorMessage;
    private ResponseModel.ErrorParent errorParent;

    public ResponseModel.ErrorParent getErrorParent() {
        return errorParent;
    }

    public void setErrorParent(ResponseModel.ErrorParent errorParent) {
        this.errorParent = errorParent;
    }
    public ResModel(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

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

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public List<ChoicesDTO> getChoices() {
        return choices;
    }

    public void setChoices(List<ChoicesDTO> choices) {
        this.choices = choices;
    }

    public UsageDTO getUsage() {
        return usage;
    }

    public void setUsage(UsageDTO usage) {
        this.usage = usage;
    }

    public static class UsageDTO {
        @SerializedName("prompt_tokens")
        private Integer promptTokens;
        @SerializedName("completion_tokens")
        private Integer completionTokens;
        @SerializedName("total_tokens")
        private Integer totalTokens;

        public Integer getPromptTokens() {
            return promptTokens;
        }

        public void setPromptTokens(Integer promptTokens) {
            this.promptTokens = promptTokens;
        }

        public Integer getCompletionTokens() {
            return completionTokens;
        }

        public void setCompletionTokens(Integer completionTokens) {
            this.completionTokens = completionTokens;
        }

        public Integer getTotalTokens() {
            return totalTokens;
        }

        public void setTotalTokens(Integer totalTokens) {
            this.totalTokens = totalTokens;
        }
    }

    public static class ChoicesDTO {
        @SerializedName("index")
        private Integer index;
        @SerializedName("message")
        private MessageDTO message;
        @SerializedName("finish_reason")
        private String finishReason;

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public MessageDTO getMessage() {
            return message;
        }

        public void setMessage(MessageDTO message) {
            this.message = message;
        }

        public String getFinishReason() {
            return finishReason;
        }

        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }

        public static class MessageDTO {
            @SerializedName("role")
            private String role;
            @SerializedName("content")
            private String content;

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
}
