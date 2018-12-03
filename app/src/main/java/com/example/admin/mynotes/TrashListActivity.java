package com.example.admin.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import android.view.ActionMode;

public class TrashListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        TrashListAdapter.ItemClickListener {

    private static final int RESTORE = -1;
    private static final int PERMANENT_DELETE = 1;
    private static final int VIEW_CODE = 2;

    Intent intent;
    DBHandler dbHandler;
    List<TrashNoteMdl> trashNotes = new ArrayList<>();
    RecyclerView trashReView;
    TrashListAdapter listAdapter;
    Spinner spinner;
    boolean showHideSort = false;
    ActionMode actionMode;
    ActionModeCallback actionModeCallback;

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

        listAdapter = new TrashListAdapter(this, trashNotes, this);

        trashReView.setLayoutManager(new LinearLayoutManager(this));
        trashReView.setAdapter(listAdapter);

//        trashReView.addOnItemTouchListener(new RecyclerTouchListener(this, trashReView,
//                new RecyclerTouchListener.MyClickListener() {
//            @Override
//            public void onClick(View view, int position) {
////                TrashNoteMdl trashNote = trashNotes.get(position);
////                viewTrashNote(trashNote, position);
//                Snackbar.make(view, "onClick works, position: " + position, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        }));

        spinner = findViewById(R.id.trash_sort_spinner_bar);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_label, R.layout.sort_spinner_item);

        adapter.setDropDownViewResource(R.layout.sort_spinner_dropdown);

        spinner.setAdapter(adapter);
        spinner.setVisibility(View.GONE);

        actionModeCallback = new ActionModeCallback();
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
            case "Recent Created":
                Collections.sort(trashNotes, TrashNoteMdl.Comparators.RE_CREATE_DATE);
                listAdapter.notifyDataSetChanged();
//                menuItem.collapseActionView();
                break;
            case "Recent Deleted":
                Collections.sort(trashNotes, TrashNoteMdl.Comparators.RE_DELETE_DATE);
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

        if (!showHideSort) {
            spinner.setVisibility(View.VISIBLE);
            showHideSort = !showHideSort;
        } else {
            spinner.setVisibility(View.GONE);
            showHideSort = !showHideSort;
        }
    }

    @Override
    public void viewInfo(View view, int position) {
        Snackbar.make(view, "onClick works, INFO position: " + position, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void itemSelect(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        listAdapter.toggleSelection(position);
        int count = listAdapter.selectCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_trash_list_am, menu);

            // disable swipe refresh if action mode is enabled
//            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_perma_delete_t:
                    // delete all the selected messages
//                    deleteMessages();
                    Toast.makeText(TrashListActivity.this, "workiing", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            listAdapter.clearSelected();
//            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
//            recyclerView.post(new Runnable() {
//                @Override
//                public void run() {
//                    mAdapter.resetAnimationIndex();
//                    // mAdapter.notifyDataSetChanged();
//                }
//            });
        }
    }
}
