package com.softacles.myjournal.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.softacles.myjournal.Database.AppDataBase;
import com.softacles.myjournal.Journal;

public class AddJournalViewModel extends ViewModel {
    private LiveData<Journal> journal;

    public AddJournalViewModel(AppDataBase database, int journalId) {
        journal = database.journalDao().loadJournalById(journalId);
    }
    public LiveData<Journal> getJournal() {
        return journal;
    }
}
