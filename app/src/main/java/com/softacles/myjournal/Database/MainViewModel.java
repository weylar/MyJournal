package com.softacles.myjournal.Database;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import com.softacles.myjournal.Journal;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Journal>> journals;

    public MainViewModel(Application application) {
        super(application);
        AppDataBase database = AppDataBase.getsInstance(this.getApplication());
        journals = database.journalDao().loadAllJournal();
    }

    public LiveData<List<Journal>> getJournals() {
        return journals;
    }
}

