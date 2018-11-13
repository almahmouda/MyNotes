package com.example.admin.mynotes;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditNote extends AppCompatActivity implements DatePickerFragment.OnFragmentInteractionListener {

    Bundle bundle;
    long id;
    DBHandler dbHandler;

    EditText edit_title;
    EditText edit_create_date;
    EditText edit_end_date;
    EditText edit_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bundle = this.getIntent().getExtras();

        if (bundle != null) {
            id = bundle.getLong("_id");
        }

        dbHandler = new DBHandler(this, null);

        String activityTitle = "Edit: " + dbHandler.getNoteAttribute((int) id, "title");

        this.setTitle(activityTitle);

        edit_title = findViewById(R.id.change_title);
        edit_create_date = findViewById(R.id.change_cDate);
        edit_end_date = findViewById(R.id.change_eDate);
        edit_note = findViewById(R.id.change_description);

        edit_title.setText(dbHandler.getNoteAttribute((int) id, "title"));
        edit_create_date.setText(dbHandler.getNoteAttribute((int) id, "date_created"));
        edit_end_date.setText(dbHandler.getNoteAttribute((int) id, "date_end"));
        edit_note.setText(dbHandler.getNoteAttribute((int) id, "description"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                discardEdit(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void saveEdit(MenuItem item) {
        String title = edit_title.getText().toString();
        String create_date = edit_create_date.getText().toString();
        String end_date = edit_end_date.getText().toString();
        String note = edit_note.getText().toString();

        if (title.trim().equals("") || create_date.trim().equals("") || note.trim().equals("")) {
            Toast.makeText(this, "Please enter a Title, Created Date, and Description.",
                    Toast.LENGTH_SHORT).show();
        } else if (!end_date.trim().equals("") && create_date.compareToIgnoreCase(end_date) > 0) {
            Toast.makeText(this, "End Date cannot be before Create Date.",
                    Toast.LENGTH_SHORT).show();
        } else {
            dbHandler.updateNote((int) id, title, create_date, end_date, note);
            Toast.makeText(this, "Note Edited.", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    public void discardEdit(MenuItem item) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void setDate(int year, int month, int day) {

    }
}
