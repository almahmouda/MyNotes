package com.example.admin.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.admin.mynotes.model.TrashNoteMdl;

public class ViewTrashActivity extends AppCompatActivity {

    private static final int RESTORE = -1;
    private static final int PERMANENT_DELETE = 1;
    private static final int UPBUTTON = 0;

    Intent intent;
    Bundle bundle;
    int position;
    TrashNoteMdl trashNote;
    DBHandler db;
    TextView create_date;
    TextView end_date;
    TextView description;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trash);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_restore, fab_delete;
        fab_restore = findViewById(R.id.fab_restore);
        fab_delete = findViewById(R.id.fab_pDelete);
        fab_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restore(trashNote.get_id());
            }
        });
        fab_delete.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                delete(trashNote.get_id());
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bundle = this.getIntent().getExtras();

        if (bundle != null) {
            trashNote = bundle.getParcelable("viewTrash");
            position = bundle.getInt("position");
        }

        db = new DBHandler(this, null);

        create_date = findViewById(R.id.trash_view_cDate);
        end_date = findViewById(R.id.trash_view_eDate);
        description = findViewById(R.id.trash_view_note);

        setViews(trashNote);
    }

    private void setViews(TrashNoteMdl note) {
        create_date.setText(note.getDate_created());
        end_date.setText(note.getDate_end());
        description.setText(note.getDescription());
        this.setTitle(note.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                setResult(UPBUTTON);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    void restore(long id) {
        db.restoreNote((int) id);
        intent = new Intent();
        intent.putExtra("position", position);
        setResult(RESTORE, intent);
        finish();
    }

    void delete(long id) {
        db.deleteNote((int) id);
        intent = new Intent();
        intent.putExtra("position", position);
        setResult(PERMANENT_DELETE, intent);
        finish();
    }

    public void testClose(View view) {
        finish();
    }
}
