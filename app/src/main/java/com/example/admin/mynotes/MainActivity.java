package com.example.admin.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    Intent intent;

    DBHandler dbHandler;

    NoteList noteListAdapter;

    ListView noteListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHandler = new DBHandler(this, null);
//
        noteListView = findViewById(R.id.note_list_view);

        noteListAdapter = new NoteList(this, dbHandler.getMyNote(), 0);

        noteListView.setAdapter(noteListAdapter);

        // OnItemClickListener for the noteListView
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // initial the intent for the ViewNote activity
                intent = new Intent(MainActivity.this, ViewNote.class);

                // put the note id of teh clicked in the intent
                intent.putExtra("_id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_create_note:
                intent = new Intent(this, CreateNote.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createNote(View view) {
        intent = new Intent(this, CreateNote.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteListAdapter = new NoteList(this, dbHandler.getMyNote(), 0);
        noteListView.setAdapter(noteListAdapter);
    }
}
