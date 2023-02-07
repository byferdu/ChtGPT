package com.ferdu.chtgpt.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TreadAndChats implements Parcelable {
    @Embedded
    ChatThread chatThread;
    @Relation(parentColumn = "id",entityColumn = "chatThreadId",entity = ChatModel.class)
    private List<ChatModel> chatModels;

    public TreadAndChats() {
    }

    protected TreadAndChats(Parcel in) {
    }

    public static final Creator<TreadAndChats> CREATOR = new Creator<TreadAndChats>() {
        @Override
        public TreadAndChats createFromParcel(Parcel in) {
            return new TreadAndChats(in);
        }

        @Override
        public TreadAndChats[] newArray(int size) {
            return new TreadAndChats[size];
        }
    };

    public ChatThread getChatThread() {
        return chatThread;
    }

    public void setChatThread(ChatThread chatThread) {
        this.chatThread = chatThread;
    }

    public List<ChatModel> getChatModels() {
        return chatModels;
    }

    public void setChatModels(List<ChatModel> chatModels) {
        this.chatModels = chatModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
