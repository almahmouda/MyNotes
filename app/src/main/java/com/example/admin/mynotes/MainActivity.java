package com.example.admin.mynotes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.admin.mynotes.model.TrashNoteMdl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    SwipeMenuListView mListView;

    Intent intent;

    DBHandler dbHandler;

    List <TrashNoteMdl> notes = new ArrayList<>();

    NoteList noteListAdapter;

    SparseIntArray sparseIntArray;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHandler = new DBHandler(this, null);

        mListView = findViewById(R.id.note_list_view);

        noteListAdapter = new NoteList(this, dbHandler.getMyNote(), 0);

        mListView.setAdapter(noteListAdapter);

        sparseIntArray = dbHandler.positionId();

        notes.addAll(dbHandler.getNote2());


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // initial the intent for the ViewNote activity
                intent = new Intent(MainActivity.this, ViewNote.class);

                // put the note id of teh clicked in the intent
                intent.putExtra("_id", id);
                startActivity(intent);
                //getId(position);

            }
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
//                        dbHandler.deleteNote();
//                        break;
////                    case 1:
////                        // delete
////                        break;
//                }
//                // false : close the menu; true : not close the menu
//                return false;
//            }
        });

        initControl();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_create_note:
                intent = new Intent(this, CreateNote.class);
                startActivity(intent);
                return true;
            case R.id.action_view_trash:
                viewTrash();
                return true;
            default:

                return super.onOptionsItemSelected(item);

        }

    }

    private void initControl() {


        final SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                switch (menu.getViewType()) {
                    case 1:
                        break;
                }

                // these following lines are responsible for the color and icon for swipe to delete
                ((SwipeMenuListView) mListView).setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_sweep);
                // add to menu
                menu.addMenuItem(deleteItem);

            }

        };
        // set creator
        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        int i = sparseIntArray.get(position);
                        dbHandler.trashNote(i);
                        Log.d(TAG, "delete test!!!2");
                        sparseIntArray.delete(position);

                        dbHandler.getMyNote();
                        noteListAdapter.notifyDataSetChanged();
                        finish();
                        startActivity(getIntent());

                }
                return true;
            }
        });
    }

    public void createNote(View view) {
        intent = new Intent(this, CreateNote.class);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 3) {
            if (resultCode == 1) {
                noteListAdapter.notifyDataSetChanged();
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    public void viewTrash() {
        intent = new Intent(this, TrashListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteListAdapter = new NoteList(this, dbHandler.getMyNote(), 0);
        mListView.setAdapter(noteListAdapter);
    }

}
