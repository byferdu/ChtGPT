package com.ferdu.chtgpt.models.data;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String userName;
    private int tokens;
    private double money;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTokens() {
        // var tokens =select total_token(sum) chatmodel;
        return tokens;
    }

    @Ignore
    public User(int tokens) {
        this.tokens = tokens;
    }

    public User() {
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public double getMoney() {

        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
