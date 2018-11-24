package com.example.admin.mynotes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.admin.mynotes.model.TrashNoteMdl;

public class ViewTrashActivity extends AppCompatActivity {

    Bundle bundle;
    long id;
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

        FloatingActionButton fab = findViewById(R.id.fab_restore);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restore(trashNote.get_id());
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bundle = this.getIntent().getExtras();

        if (bundle != null)
            trashNote = bundle.getParcelable("viewTrash");

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
    }

    void restore(long id) {
        db.restoreNote((int) id);
        finish();
    }
}
