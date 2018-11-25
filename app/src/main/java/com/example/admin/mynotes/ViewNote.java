package com.example.admin.mynotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ViewNote extends AppCompatActivity {

    Bundle bundle;

    Intent intent;

    long id;

    DBHandler dbHandler;

    TextView view_create_date;
    TextView view_end_date;
    TextView viewNote;
    String noteTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bundle = this.getIntent().getExtras();

        if (bundle != null) {
            id = bundle.getLong("_id");
        }

        dbHandler = new DBHandler(this, null);

        view_create_date = findViewById(R.id.view_create_date);
        view_end_date = findViewById(R.id.view_end_date);
        viewNote = findViewById(R.id.viewNote);

        setViewFields((int) id);

    }

    void setViewFields(int id) {
        noteTitle = dbHandler.getNoteAttribute(id, "title");

        this.setTitle(noteTitle);

        view_create_date.setText(dbHandler.getNoteAttribute(id, "date_created"));
        view_end_date.setText(dbHandler.getNoteAttribute(id, "date_end"));
        viewNote.setText(dbHandler.getNoteAttribute(id, "description"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit_note:
                intent = new Intent(this, EditNote.class);
                intent.putExtra("_id", id);
                startActivityForResult(intent, 1);
                return true;
            case R.id.action_delete_note:
                Toast.makeText(this, noteTitle + " Deleted.", Toast.LENGTH_LONG).show();
                dbHandler.deleteNote((int) id);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void editNote(View view) {
//        Toast.makeText(this, "Edit Test OK.", Toast.LENGTH_LONG).show();
        intent = new Intent(this, EditNote.class);
        intent.putExtra("_id", id);
        startActivityForResult(intent, 1);
//        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                setViewFields((int) id);
                Toast.makeText(this, "Note edited.", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "No changes made.", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
////        this.recreate();
//        setViewFields((int) id);
//    }
}
