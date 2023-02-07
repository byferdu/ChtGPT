package com.ferdu.chtgpt.models.dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface BaseDao<T> {

    @Insert
    void insertD(T... t);

    @Update
    void updateD(T... t);

    @Delete
    void deleteD(T... t);

}
