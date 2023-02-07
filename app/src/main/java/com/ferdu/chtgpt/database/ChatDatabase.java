package com.ferdu.chtgpt.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ferdu.chtgpt.models.dao.ChatModelDao;
import com.ferdu.chtgpt.models.dao.ChatThreadDao;
import com.ferdu.chtgpt.models.dao.Example2Dao;
import com.ferdu.chtgpt.models.dao.TypeAndExampleDao;
import com.ferdu.chtgpt.models.dao.TypeDao;
import com.ferdu.chtgpt.models.dao.UserDao;
import com.ferdu.chtgpt.models.data.ChatModel;
import com.ferdu.chtgpt.models.data.ChatThread;
import com.ferdu.chtgpt.models.data.Converters;
import com.ferdu.chtgpt.models.data.Example2;
import com.ferdu.chtgpt.models.data.TypeAndExample;
import com.ferdu.chtgpt.models.data.Types;
import com.ferdu.chtgpt.models.data.User;


@Database(entities = {ChatModel.class, ChatThread.class, TypeAndExample.class, Example2.class, Types.class, User.class}
        ,version = 8,
        exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ChatDatabase extends RoomDatabase {
    private static ChatDatabase INSTANCE;
    // ...
    public static synchronized ChatDatabase getDatabase(Context context) {
       if (INSTANCE == null) {
           INSTANCE = Room.databaseBuilder(context.getApplicationContext(),ChatDatabase.class,"chat_database")
                   .allowMainThreadQueries()
                   .createFromAsset("database/chat_yu.db")
                   //.fallbackToDestructiveMigration()
                   //.createFromAsset("chat_database.db")
                   .build();

       }
       return INSTANCE;
   }


    public abstract ChatModelDao chatModelDao();
    public abstract TypeDao typeDao();
    public abstract ChatThreadDao chatThreadDao();
    public abstract Example2Dao exampleDao();
    public abstract UserDao userDao();

    public abstract TypeAndExampleDao typeAndExampleDao();
}
