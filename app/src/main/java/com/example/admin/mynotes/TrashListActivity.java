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
import android.widget.Toast;

import com.example.admin.mynotes.model.TrashNoteMdl;
import com.example.admin.mynotes.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class TrashListActivity extends AppCompatActivity implements TrashListAdapter.ClickListener {

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
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.menu_trash_list_am, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteNotes(listAdapter.getSelectedItems());
                    actionMode.finish();
                    return true;

                case R.id.action_restore:
                    // delete all the selected messages
                    restoreNotes(listAdapter.getSelectedItems());
                    actionMode.finish();
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
        public void onDestroyActionMode(ActionMode actionMode) {
            listAdapter.clearSelections();
            actionMode = null;
        }
        
        
    }
}
