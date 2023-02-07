package com.ferdu.chtgpt.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import com.ferdu.chtgpt.models.data.Example2;

import java.util.List;

@Dao
public interface Example2Dao extends BaseDao<Example2> {


    @Query("SELECT * FROM Example2 WHERE id = :id")
    LiveData<Example2> getById(int id);
//SELECT s.*
//FROM course c
//INNER JOIN student_course sc ON c.id = sc.course_id
//INNER JOIN student s ON sc.student_id = s.id
//WHERE c.name = 'Math';
    @Query("SELECT * FROM Example2 ")
    LiveData<List<Example2>> getAll();
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT e.id as id,title,image,summary,temperature,max_tokens,top_p,frequency_penalty,presence_penalty FROM Example2 e inner join TypeAndExample te on e.id=te.exampleId inner join Types t on te.typeId=t.id where t.type= :type")
    LiveData<List<Example2>> getTypes(String type);
    @Query("SELECT * FROM Example2 Where title like :search OR summary like :search")
    LiveData<List<Example2>> getExSearch(String search);

}