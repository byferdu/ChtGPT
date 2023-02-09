package com.ferdu.chtgpt.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ferdu.chtgpt.models.dao.ChatModelDao;
import com.ferdu.chtgpt.models.dao.ChatThreadDao;
import com.ferdu.chtgpt.models.dao.Example2Dao;
import com.ferdu.chtgpt.models.dao.PromptDAO;
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
import com.ferdu.chtgpt.ui.home.PromptModel;


@Database(entities = {ChatModel.class, ChatThread.class, TypeAndExample.class, Example2.class, Types.class, User.class, PromptModel.class}
        ,version = 10,
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
                   .addMigrations(new Migration(9,10) {
                       @Override
                       public void migrate(@NonNull SupportSQLiteDatabase database) {
                           database.execSQL("create table PromptModel_dg_tmp\n" +
                                   "(\n" +
                                   "    id         TEXT not null\n" +
                                   "        primary key,\n" +
                                   "    act        TEXT,\n" +
                                   "    prompt     TEXT,\n" +
                                   "    createTime TEXT,\n" +
                                   "    updateTime text\n" +
                                   ");\n" +
                                   "\n"
                                );
                           database.execSQL("drop table PromptModel");
                           database.execSQL("alter table PromptModel_dg_tmp rename to PromptModel;");
                       }
                   })
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
    public abstract PromptDAO promptDAO();

    public abstract TypeAndExampleDao typeAndExampleDao();
}
