package com.ferdu.chtgpt.models;

public class PrompModel {
    public String content;
    public String content2;
    private Usage usage;




    public PrompModel(String content, String content2, Usage usage) {
        this.content = content;
        this.content2 = content2;
        this.usage = usage;
    }

    public PrompModel() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }


}
