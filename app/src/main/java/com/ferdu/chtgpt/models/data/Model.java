package com.ferdu.chtgpt.models.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Model {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String model;
    private String summary;

    public Model(String model, String summary) {
        this.model = model;
        this.summary = summary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
