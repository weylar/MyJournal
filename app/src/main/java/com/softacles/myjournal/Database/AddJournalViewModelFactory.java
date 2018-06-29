package com.softacles.myjournal.Database;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.softacles.myjournal.Database.AddJournalViewModel;
import com.softacles.myjournal.Database.AppDataBase;


public class AddJournalViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDataBase mDb;
    private final int mJournalId;

    public AddJournalViewModelFactory(AppDataBase database, int taskId) {
        mDb = database;
        mJournalId = taskId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AddJournalViewModel(mDb, mJournalId);
    }
}
