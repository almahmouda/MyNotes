package com.example.admin.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.admin.mynotes.model.TrashNoteMdl;
import com.example.admin.mynotes.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class TrashListActivity extends AppCompatActivity {

    private static final int RESTORE = -1;
    private static final int PERMANENT_DELETE = 1;
    private static final int NO_ACTION = 0;
    private static final int VIEW_CODE = 2;

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
                viewTrashNote(trashNote, position);
//                Toast.makeText(TrashListActivity.this, "onClick works.", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void viewTrashNote(TrashNoteMdl note, int position) {
        intent = new Intent(this, ViewTrashActivity.class);
        intent.putExtra("viewTrash", note);
        intent.putExtra("position", position);
        startActivityForResult(intent, VIEW_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIEW_CODE) {
            int position;
            if (resultCode == RESTORE || resultCode == PERMANENT_DELETE) {
                position = data.getIntExtra("position", 0);
                trashNotes.remove(position);
                listAdapter.notifyItemRemoved(position);
            }
        }
    }
}
