package com.ferdu.chtgpt.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import com.ferdu.chtgpt.models.data.Types;

import java.util.List;

@Dao
public interface TypeDao extends BaseDao<Types> {

    @Query("SELECT * FROM Types")
    LiveData<List<Types>> getTypes();

    @Query("SELECT * FROM Types WHERE id = :id")
    LiveData<Types> getTypes(int id);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//  @Query("SELECT * FROM Example2 e inner join TypeAndExample te on e.id=te.exampleId inner join Types t on te.typeId=t.id where t.type= :type")
    @Query("SELECT * FROM Types t inner join TypeAndExample te on te.typeId=t.id inner join Example2 e on e.id=te.exampleId where e.id= :id")
    LiveData<List<Types>> getTypesFromEx(int id);
}
