package com.example.admin.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.admin.mynotes.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;


public class TrashListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        TrashListAdapter.ClickListener {

    private static final int RESTORE = -1;
    private static final int PERMANENT_DELETE = 1;
    private static final int VIEW_CODE = 2;

    Intent intent;
    DBHandler dbHandler;
    List<TrashNoteMdl> trashNotes = new ArrayList<>();
    RecyclerView trashReView;
    TrashListAdapter listAdapter;
    ActionMode actionMode;
    ActionModeCallback actionModeCallback;
    Spinner spinner;
    boolean showHideSort = false;

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

        listAdapter = new TrashListAdapter(this, trashNotes,this);

        trashReView.setLayoutManager(new LinearLayoutManager(this));
        trashReView.setAdapter(listAdapter);

//        trashReView.addOnItemTouchListener(new RecyclerTouchListener(this, trashReView, new RecyclerTouchListener.MyClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//
////                Toast.makeText(TrashListActivity.this, "onClick works.", Toast.LENGTH_SHORT).show();
//            }
//        }));

        actionModeCallback = new ActionModeCallback();

        spinner = findViewById(R.id.trash_sort_spinner_bar);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_label, R.layout.sort_spinner_item);

        adapter.setDropDownViewResource(R.layout.sort_spinner_dropdown);

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
        if (requestCode == VIEW_CODE && data != null) {
            int position;
            if (resultCode == RESTORE || resultCode == PERMANENT_DELETE) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_permaDelete:
                Toast.makeText(this, "All items Deleted: Not Implemeted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void icon_cell(View view, int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);
    }

    @Override
    public void note_cell(View view, int position) {
        if (listAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            TrashNoteMdl trashNote = trashNotes.get(position);
            viewTrashNote(trashNote, position);
        }
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        listAdapter.toggleSelection(position);
        int count = listAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback{
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_trash_list_am, menu);

            if(showHideSort){
                spinner.setVisibility(View.GONE);
                showHideSort = !showHideSort;
            }

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteNotes(listAdapter.getSelectedItems());
                    mode.finish();
                    return true;

                case R.id.action_restore:
                    // delete all the selected messages
                    restoreNotes(listAdapter.getSelectedItems());
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        private void restoreNotes(List<Integer> selectedItems) {
            TrashNoteMdl trashItem;

            for (int i = selectedItems.size() - 1; i >= 0; i--) {
                int position = selectedItems.get(i);
                trashItem = trashNotes.get(position);
                dbHandler.restoreNote(trashItem.get_id());
                listAdapter.removeData(position);
            }
            listAdapter.notifyDataSetChanged();
        }

        private void deleteNotes(List<Integer> selectedItems) {
            TrashNoteMdl trashItem;

            for (int i = selectedItems.size() - 1; i >= 0; i--) {
                int position = selectedItems.get(i);
                trashItem = trashNotes.get(position);
                dbHandler.deleteNote(trashItem.get_id());
                listAdapter.removeData(position);
            }
            listAdapter.notifyDataSetChanged();
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            listAdapter.clearSelections();
            actionMode = null;
        }
        
        
    }
}
