package com.ferdu.chtgpt.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class Example2 implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id ;
    private String title;
    private String image="/svgs";
    private String summary;
    private String prompt;
    private String response;
    private String model="text-davinci-003";
    private float temperature = 0.5f;
    private int max_tokens = 1000;
    private float top_p = 1;
    private float frequency_penalty = 0;
    private float presence_penalty = 0;
    private String stop;
    private String injStart="";
    private String injRestart="";
    private Date createTime=new Date(new java.util.Date().getTime());
    @Ignore
    public Example2(String title, String summary) {
        this.title = title;
        this.summary = summary;
    }

    public Example2() {
    }

    protected Example2(Parcel in) {
        id = in.readInt();
        title = in.readString();
        image = in.readString();
        summary = in.readString();
        prompt = in.readString();
        response = in.readString();
        model = in.readString();
        temperature = in.readFloat();
        max_tokens = in.readInt();
        top_p = in.readInt();
        frequency_penalty = in.readInt();
        presence_penalty = in.readInt();
        stop = in.readString();
    }


    public static final Creator<Example2> CREATOR = new Creator<Example2>() {
        @Override
        public Example2 createFromParcel(Parcel in) {
            return new Example2(in);
        }

        @Override
        public Example2[] newArray(int size) {
            return new Example2[size];
        }
    };

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
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

    public String getStop() {

        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
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
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(summary);
        dest.writeString(prompt);
        dest.writeString(response);
        dest.writeString(model);
        dest.writeFloat(temperature);
        dest.writeInt(max_tokens);
        dest.writeFloat(top_p);
        dest.writeFloat(frequency_penalty);
        dest.writeFloat(presence_penalty);
        dest.writeString(stop);
        dest.writeString(injStart);
        dest.writeString(injRestart);
    }
}
