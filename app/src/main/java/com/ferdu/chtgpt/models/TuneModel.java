package com.ferdu.chtgpt.models;

public class TuneModel {
    private String model="text-davinci-003";
    private boolean isTrans = false;
    private float temperature = 0.5f;
    private int max_tokens = 1000;
    private float top_p = 1f;
    private String stop="";
    private int frequency_penalty = 0;
    private int presence_penalty = 0;
    private String injStart ="";
    private String injRestart ="";

    private boolean isOpen=false;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isTrans() {
        return isTrans;
    }

    public void setTrans(boolean trans) {
        isTrans = trans;
    }

    public String getInjStart() {
        return injStart;
    }

    public void setInjStart(String injStart) {
        this.injStart = injStart;
    }

    public String getInjRestart() {
        return injRestart;
    }

    public void setInjRestart(String injRestart) {
        this.injRestart = injRestart;
    }



    public TuneModel() {
    }

    public TuneModel(String model, float temperature, int max_tokens, float top_p, String stop) {
        this.model = model;
        this.temperature = temperature;
        this.max_tokens = max_tokens;
        this.top_p = top_p;
        this.stop = stop;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }

    public float getTop_p() {
        return top_p;
    }

    public void setTop_p(float top_p) {
        this.top_p = top_p;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public int getFrequency_penalty() {
        return frequency_penalty;
    }

    public void setFrequency_penalty(int frequency_penalty) {
        this.frequency_penalty = frequency_penalty;
    }

    public int getPresence_penalty() {
        return presence_penalty;
    }

    public void setPresence_penalty(int presence_penalty) {
        this.presence_penalty = presence_penalty;
    }
}
