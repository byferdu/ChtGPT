package com.ferdu.chtgpt.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ferdu.chtgpt.models.dao.ChatModelDao;
import com.ferdu.chtgpt.models.dao.ChatThreadDao;
import com.ferdu.chtgpt.models.dao.Example2Dao;
import com.ferdu.chtgpt.models.dao.ModelDao;
import com.ferdu.chtgpt.models.dao.PromptDAO;
import com.ferdu.chtgpt.models.dao.TypeAndExampleDao;
import com.ferdu.chtgpt.models.dao.TypeDao;
import com.ferdu.chtgpt.models.dao.UserDao;
import com.ferdu.chtgpt.models.data.ChatModel;
import com.ferdu.chtgpt.models.data.ChatThread;
import com.ferdu.chtgpt.models.data.Converters;
import com.ferdu.chtgpt.models.data.Example2;
import com.ferdu.chtgpt.models.data.Model;
import com.ferdu.chtgpt.models.data.TypeAndExample;
import com.ferdu.chtgpt.models.data.Types;
import com.ferdu.chtgpt.models.data.User;
import com.ferdu.chtgpt.ui.home.PromptModel;


@Database(entities = {ChatModel.class, ChatThread.class, TypeAndExample.class, Example2.class, Types.class, User.class, PromptModel.class, Model.class}
        ,version = 12,
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
//                   .addMigrations(new Migration(11,12) {
//                       @Override
//                       public void migrate(@NonNull SupportSQLiteDatabase database) {
//                           database.execSQL("alter table ChatModel\n" +
//                                   "    add model TEXT;");
//                       }
//                   })
                   .build();

       }
       return INSTANCE;
   }


    public abstract ChatModelDao chatModelDao();
    public abstract TypeDao typeDao();
    public abstract ChatThreadDao chatThreadDao();
    public abstract Example2Dao exampleDao();
    public abstract UserDao userDao();
    public abstract PromptDAO promptDAO();

    public abstract ModelDao modelDao();
    public abstract TypeAndExampleDao typeAndExampleDao();
}
