package com.ferdu.chtgpt.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.ferdu.chtgpt.models.data.User;
@Dao
public interface UserDao extends BaseDao<User> {

    @Query("Select * from User")
    LiveData<User> getUser();
    @Query("Delete from User")
    void deleteUser();
}
