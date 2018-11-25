package com.example.admin.mynotes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteList extends CursorAdapter {
    /**
     * initialize NoteList CursorAdapter
     */
    NoteList(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }

    /**
     * view to hold the data in the cursor
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.li_note_list, parent, false);
    }

    /**
     * binds view to the date in cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.label_title)).setText(cursor.getString(cursor.getColumnIndex("title")));
        ((TextView) view.findViewById(R.id.label_initial)).setText(cursor.getString(cursor.getColumnIndex("title")).substring(0,1));
        ((ImageView) view.findViewById(R.id.letter_plate)).setColorFilter(cursor.getInt(cursor.getColumnIndex("color")));
    }

}
