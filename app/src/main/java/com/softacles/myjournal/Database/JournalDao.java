package com.softacles.myjournal.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.softacles.myjournal.Journal;

import java.util.List;

@Dao
public interface JournalDao {
    @Query("SELECT * FROM journal ORDER BY id")
    LiveData<List<Journal>> loadAllJournal();

    @Insert
    void insertJournal(Journal journal);

    @Update(onConflict  = OnConflictStrategy.REPLACE)
    void updateJournal(Journal journal);

    @Delete
    void deleteTask(Journal journal);

    @Delete
    void deleteAllTask(List<Journal> journal);

    @Query("SELECT * FROM journal WHERE id = :id")
    LiveData<Journal> loadJournalById(int id);
}
