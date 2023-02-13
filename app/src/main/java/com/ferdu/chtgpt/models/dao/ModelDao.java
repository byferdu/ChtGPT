package com.ferdu.chtgpt.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.ferdu.chtgpt.models.data.Model;

import java.util.List;
@Dao
public interface ModelDao extends BaseDao<Model> {
    @Query("Select * from Model")
    LiveData<List<Model>> getAll();
}
