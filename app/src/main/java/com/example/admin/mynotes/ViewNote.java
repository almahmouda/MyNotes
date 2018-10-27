package com.example.admin.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ViewNote extends AppCompatActivity {

    Bundle bundle;

    Intent intent;

    long id;

    DBHandler dbHandler;

    TextView view_create_date;
    TextView view_end_date;
    TextView viewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle = this.getIntent().getExtras();

        if (bundle != null) {
            id = bundle.getLong("_id");
        }

        dbHandler = new DBHandler(this, null);

        String noteTitle = dbHandler.getNoteAttribute((int) id, "title");

        this.setTitle(noteTitle);

        view_create_date = findViewById(R.id.view_create_date);
        view_end_date = findViewById(R.id.view_end_date);
        viewNote = findViewById(R.id.viewNote);

        view_create_date.setText(dbHandler.getNoteAttribute((int) id, "date_created"));
        view_end_date.setText(dbHandler.getNoteAttribute((int) id, "date_end"));
        viewNote.setText(dbHandler.getNoteAttribute((int) id, "description"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
