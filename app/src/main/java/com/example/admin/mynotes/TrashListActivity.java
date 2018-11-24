package com.example.admin.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.admin.mynotes.model.TrashNoteMdl;
import com.example.admin.mynotes.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class TrashListActivity extends AppCompatActivity {

    Intent intent;
    DBHandler dbHandler;
    List<TrashNoteMdl> trashNotes = new ArrayList<>();
    RecyclerView trashReView;
    TrashListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHandler = new DBHandler(this, null);

        trashReView = findViewById(R.id.trash_list);

        trashNotes.addAll(dbHandler.getTrash());

        listAdapter = new TrashListAdapter(this, trashNotes);

        trashReView.setLayoutManager(new LinearLayoutManager(this));
        trashReView.setAdapter(listAdapter);

        trashReView.addOnItemTouchListener(new RecyclerTouchListener(this, trashReView, new RecyclerTouchListener.MyClickListener() {
            @Override
            public void onClick(View view, int position) {
                TrashNoteMdl trashNote = trashNotes.get(position);
                viewTrashNote(trashNote);
                Toast.makeText(TrashListActivity.this, "onClick works.", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void viewTrashNote(TrashNoteMdl note) {
        intent = new Intent(this, ViewTrashActivity.class);
        intent.putExtra("viewTrash", note);
        startActivity(intent);
    }

}
