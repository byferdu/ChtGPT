package com.ferdu.chtgpt.models;

public class TreadSelectModel {
    private int position;
    private boolean isChoose = false;

    public TreadSelectModel(int position, boolean isChoose) {
        this.position = position;
        this.isChoose = isChoose;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
