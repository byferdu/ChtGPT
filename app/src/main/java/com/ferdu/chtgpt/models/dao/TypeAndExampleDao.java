package com.ferdu.chtgpt.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.ferdu.chtgpt.models.data.TypeAndExample;

import java.util.List;
@Dao
public interface TypeAndExampleDao extends BaseDao<TypeAndExample> {
    @Query("SELECT * FROM TypeAndExample")
    LiveData<List<TypeAndExample>> getAll();

}
