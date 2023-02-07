package com.ferdu.chtgpt.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.ferdu.chtgpt.models.data.ChatModel;

import java.util.List;

@Dao
public interface ChatModelDao extends BaseDao<ChatModel> {


    @Query("delete from ChatModel")
    void deleteAll();

    @Query("SELECT * FROM ChatModel WHERE chatThreadId = :id")
    LiveData<List<ChatModel>> getById(int id);

    @Query("Delete  FROM ChatModel WHERE chatThreadId = :id")
    int deleteByTreadId(int id);

    @Query("SELECT * FROM ChatModel")
    LiveData<List<ChatModel>> getAll();

    @Query("SELECT * from ChatModel where meText= :meText AND AIText=:AIText")
    LiveData<ChatModel> isExist(String meText,String AIText);

    @Query("SELECT * FROM ChatModel where meText like :query OR AIText like :query")
    LiveData<List<ChatModel>> getQuery(String query);
}