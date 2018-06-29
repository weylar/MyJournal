package com.softacles.myjournal;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.softacles.myjournal.Authentication.Login;
import com.softacles.myjournal.Database.AppDataBase;
import com.softacles.myjournal.Database.AppExecutors;
import com.softacles.myjournal.Database.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class JournalList extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, JournalAdapter.OnItemClickListener {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    JournalAdapter journalAdapter;
    LinearLayout emptyJournal;
    ArrayList<Journal> journalList;
    private AppDataBase mDb;

    /**
     * I commented this out for the purpose of testing, its actually in charge of data persisting on cloud
     * FirebaseDatabase database;
     * DatabaseReference myRef;
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JournalList.this, EnterJournal.class);
                startActivity(intent);
            }
        });

        mDb = AppDataBase.getsInstance(getApplicationContext());
        recyclerView = findViewById(R.id.recycler_journals);
        emptyJournal = findViewById(R.id.empty_journal);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        journalAdapter = new JournalAdapter(this, this);
        recyclerView.setLayoutManager(linearLayoutManager);  //I set manager for recycler here
        recyclerView.setAdapter(journalAdapter);
        setUpViewModel();

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof JournalAdapter.MyViewHolder) {
            String itemName = journalAdapter.getJournalList().get(position).getTitle();// a dummy way of getting the gig title to display on snackbar whenm swipped
            /*This removes the item from the recycler view*/
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.journalDao().deleteTask(journalAdapter.getJournalList().get(position));


                }
            });

            Snackbar snackbar = Snackbar.make(emptyJournal, itemName + " deleted!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (journalAdapter.getItemCount() == 0){
            emptyJournal.setVisibility(View.VISIBLE);
        }else{
            emptyJournal.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journal_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_all) {
            journalAdapter.removeAllItem(emptyJournal);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.journalDao().deleteAllTask(journalAdapter.getJournalList());

                }
            });
            return true;
        }
        if (id == R.id.action_sign_out) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(JournalList.this, Login.class));
                        finish();
                    } else {
                        Toast toast = Toast.makeText(JournalList.this, "Encounter error signing out!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });
        }
        if (id == R.id.action_sync_data) {
            /** myRef = database.getReference("Journal Entries");
             myRef.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
            Journal journal = dataSnapshot.getValue(Journal.class);
            List<Journal> journals = new ArrayList<>();
            journals.add(journal);
            recyclerView.setAdapter(new JournalAdapter(JournalList.this, journals, JournalList.this));
            Toast.makeText(JournalList.this, "Journal synced successfully!", Toast.LENGTH_LONG).show();

            }


            @Override public void onCancelled(DatabaseError error) {
            Toast.makeText(JournalList.this, "Failure to read data, you might want to check internet connection", Toast.LENGTH_LONG).show();

            }
            });*/
        }

        return super.onOptionsItemSelected(item);
    }


    private void setUpViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getJournals().observe(this, new Observer<List<Journal>>() {
            @Override
            public void onChanged(@Nullable List<Journal> journals) {
                journalAdapter.updateJournal(journals);
            }
        });

    }

    public void onItemClick(int id) {
        Intent intent = new Intent(JournalList.this, EnterJournal.class);
        intent.putExtra(EnterJournal.EXTRA_JOURNAL_ID, id);
        startActivity(intent);
    }
}
