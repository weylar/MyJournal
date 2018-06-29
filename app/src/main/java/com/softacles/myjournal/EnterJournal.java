package com.softacles.myjournal;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.softacles.myjournal.Database.AddJournalViewModel;
import com.softacles.myjournal.Database.AddJournalViewModelFactory;
import com.softacles.myjournal.Database.AppDataBase;
import com.softacles.myjournal.Database.AppExecutors;

public class EnterJournal extends AppCompatActivity {
    private AppDataBase mDb;
    private EditText title, content;
    public static final String EXTRA_JOURNAL_ID = "extraJournalId";
    private int id;
    private Intent intent;

    /**
     * I commented this out for the purpose of testing, its actually in charge of data persisting on cloud
     * FirebaseDatabase database;
     * DatabaseReference myRef;
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_journal);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        mDb = AppDataBase.getsInstance(getApplicationContext());

        /**I commented this out for the purpose of testing, its actually in charge of data persisting on cloud
         database = FirebaseDatabase.getInstance();
         myRef = database.getReference("Journal Entries");
         */

        intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)) {
            getSupportActionBar().setTitle("Edit Journal");
            id = getIntent().getExtras().getInt(EXTRA_JOURNAL_ID);
            AddJournalViewModelFactory factory = new AddJournalViewModelFactory(mDb, id);
            final AddJournalViewModel viewModel = ViewModelProviders.of(this, factory).get(AddJournalViewModel.class);
            viewModel.getJournal().observe(this, new Observer<Journal>() {
                @Override
                public void onChanged(@Nullable Journal journal) {
                    viewModel.getJournal().removeObserver(this);
                    showUi(journal);
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_journal_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            final Journal journal = new Journal(title.getText().toString(), content.getText().toString());

            /**I commented this out for the purpose of testing, its actually in charge of data persisting on cloud
             myRef.setValue(journal);
             */

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (!intent.hasExtra(EXTRA_JOURNAL_ID)) {
                        mDb.journalDao().insertJournal(journal);

                    } else {
                        journal.setId(id);
                        mDb.journalDao().updateJournal(journal);

                        /**I commented this out for the purpose of testing, its actually in charge of data persisting on cloud
                         myRef.setValue(journal);
                         */

                    }
                    finish();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*Method in charge of displaying UI with data*/
    private void showUi(Journal journal) {
        if (journal == null) {
            return;
        }
        title.setText(journal.getTitle());
        content.setText(journal.getContent());
    }


}


