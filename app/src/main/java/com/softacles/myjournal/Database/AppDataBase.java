package com.softacles.myjournal.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.softacles.myjournal.Journal;

@Database(entities = {Journal.class},version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static final String DATABASE_NAME = "myjournals";
    private static final Object LOCK = new Object();
    private static AppDataBase sInstance;

    public static AppDataBase getsInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                AppDataBase.class, AppDataBase.DATABASE_NAME).build();
            }
        }
        return sInstance;
    }
    public abstract JournalDao journalDao();
}
