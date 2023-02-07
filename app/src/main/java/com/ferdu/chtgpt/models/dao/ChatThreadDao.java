package com.ferdu.chtgpt.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ferdu.chtgpt.models.data.ChatThread;
import com.ferdu.chtgpt.models.data.TreadAndChats;

import java.util.List;

@Dao
public interface ChatThreadDao extends BaseDao<ChatThread> {


    @Query("SELECT * FROM ChatThread WHERE id = :id")
    ChatThread getById(int id);

    @Query("SELECT max(id) FROM ChatThread")
    LiveData<Integer> getId();

    @Query("SELECT * FROM ChatThread order by createTime desc")
    LiveData<List<ChatThread>> getAll();

    @Transaction
    @Query("SELECT * from ChatThread")
    LiveData<TreadAndChats> loadChatsAndTread();

    @Query("SELECT * FROM ChatThread Where title like :search")
    LiveData<List<ChatThread>> getQuery(String search);
}
