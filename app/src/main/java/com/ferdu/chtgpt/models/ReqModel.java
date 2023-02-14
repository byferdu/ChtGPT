package com.ferdu.chtgpt.models;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class ReqModel {
    private String model="text-davinci-003";
    private String prompt="hello";
    private float temperature = 0.5f;
    private int max_tokens = 1000;
    private float top_p = 1;
    private float frequency_penalty = 0.5f;
    private float presence_penalty = 0.5f;
    private String[] stop;

    public ReqModel() {
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
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

    public float getFrequency_penalty() {
        return frequency_penalty;
    }

    public void setFrequency_penalty(float frequency_penalty) {
        this.frequency_penalty = frequency_penalty;
    }

    public float getPresence_penalty() {
        return presence_penalty;
    }

    public void setPresence_penalty(float presence_penalty) {
        this.presence_penalty = presence_penalty;
    }

    public String[] getStop() {
        return stop;
    }

    public void setStop(String[] stop) {
        this.stop = stop;
    }

    @NonNull
    @Override
    public String toString() {
        return "ReqModel{" +
                "model='" + model + '\'' +
                ", prompt='" + prompt + '\'' +
                ", temperature=" + temperature +
                ", max_tokens=" + max_tokens +
                ", top_p=" + top_p +
                ", frequency_penalty=" + frequency_penalty +
                ", presence_penalty=" + presence_penalty +
                ", stop=" + Arrays.toString(stop) +
                '}';
    }
}
