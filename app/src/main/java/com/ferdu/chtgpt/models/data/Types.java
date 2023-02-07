package com.ferdu.chtgpt.models.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Types {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Types(String type) {
        this.type = type;
    }
}
