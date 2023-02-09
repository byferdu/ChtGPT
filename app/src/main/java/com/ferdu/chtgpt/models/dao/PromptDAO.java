package com.ferdu.chtgpt.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.ferdu.chtgpt.ui.home.PromptModel;

import java.util.List;
@Dao
public interface PromptDAO extends BaseDao<PromptModel>{
    @Query("Select * from PromptModel")
    LiveData<List<PromptModel>> getAll();
    @Query("SELECT * FROM PromptModel Where act like :search or prompt like :search")
    LiveData<List<PromptModel>> getQuery(String search);

    @Query("SELECT * FROM PromptModel Where id=:search")
    LiveData<PromptModel> getAtQuery(String search);
}
