package com.example.admin.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.mynotes.model.TrashNoteMdl;
import com.example.admin.mynotes.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrashListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int RESTORE = -1;
    private static final int PERMANENT_DELETE = 1;
    private static final int VIEW_CODE = 2;
    private static final int UPBUTTON = 0;

    Intent intent;
    DBHandler dbHandler;
    List<TrashNoteMdl> trashNotes = new ArrayList<>();
    RecyclerView trashReView;
    TrashListAdapter listAdapter;
    Spinner spinner;
    boolean test = false;

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

        spinner = findViewById(R.id.trash_sort_spinner_bar);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_label, R.layout.sort_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setVisibility(View.GONE);
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
            if ((resultCode == RESTORE || resultCode == PERMANENT_DELETE) && data != null) {
                position = data.getIntExtra("position", 0);
                trashNotes.remove(position);
                listAdapter.notifyItemRemoved(position);
            }
            if (resultCode == UPBUTTON) {
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trash_list, menu);

        return true;
    }

    void displayToast(String i) {
        Toast.makeText(this, i, Toast.LENGTH_SHORT).show();
    }


    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Implementers can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String spinnerLabel = parent.getItemAtPosition(position).toString();
        displayToast(spinnerLabel);
        switch (spinnerLabel) {
            case "Default":
                Collections.sort(trashNotes, TrashNoteMdl.Comparators.ID);
                listAdapter.notifyDataSetChanged();
//                menuItem.collapseActionView();
                break;
            case "Alphabetically":
                Collections.sort(trashNotes, TrashNoteMdl.Comparators.ALPHA);
                listAdapter.notifyDataSetChanged();
//                menuItem.collapseActionView();
                break;
        }
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showSort(MenuItem item) {

        if (!test) {
            spinner.setVisibility(View.VISIBLE);
            test = !test;
        } else {
            spinner.setVisibility(View.GONE);
            test = !test;
        }


    }
}
