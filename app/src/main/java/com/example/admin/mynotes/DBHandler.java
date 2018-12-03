package com.example.admin.mynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.SparseIntArray;

import com.example.admin.mynotes.model.TrashNoteMdl;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "myNote.db";

    private static final String TABLE_MY_NOTE = "my_notes";
    private static final String COLUMN_LIST_ID ="_id";
    private static final String COLUMN_NOTE_TITLE = "title";
    private static final String COLUMN_NOTE_DATE_CREATE = "date_created";
    private static final String COLUMN_NOTE_DATE_END = "date_end";
    private static final String COLUMN_NOTE_DESCRIPTION = "description";
    private static final String COLUMN_NOTE_COLOR = "color";
//    private static final String TABLE_NAME = "trash";


    DBHandler (Context context, SQLiteDatabase.CursorFactory factory){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String qCrateNoteTable = "CREATE TABLE " + TABLE_MY_NOTE + "(" +
                COLUMN_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTE_TITLE + " TEXT, " +
                COLUMN_NOTE_DATE_CREATE + " TEXT, " +
                COLUMN_NOTE_DATE_END + " TEST, " +
                COLUMN_NOTE_DESCRIPTION + " TEST, " +
                COLUMN_NOTE_COLOR + " INTEGER" +
                ");";

        sqLiteDatabase.execSQL(qCrateNoteTable);

        String qCreateTrashTable = TrashNoteMdl.CREATE_TABLE;

        sqLiteDatabase.execSQL(qCreateTrashTable);

    }
    SparseIntArray positionId() {
        SparseIntArray pi = new SparseIntArray();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_MY_NOTE;
        int pos = 0;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                pi.put(pos, cursor.getInt(0));
                ++pos;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return pi;
    }

    /**
     * Override the onUpgrade method to drop tables when the database version is changed
     *
     * @param sqLiteDatabase database being changed
     * @param oldVersion     previous version of database
     * @param newVersion     new version of database
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_NOTE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrashNoteMdl.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    void addMyNote(String title, String dateCreated, String dateEnd, String description, int color) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NOTE_TITLE, title);
        values.put(COLUMN_NOTE_DATE_CREATE, dateCreated);
        values.put(COLUMN_NOTE_DATE_END, dateEnd);
        values.put(COLUMN_NOTE_DESCRIPTION, description);
        values.put(COLUMN_NOTE_COLOR, color);

        db.insert(TABLE_MY_NOTE,null,values);
        db.close();
    }

    Cursor getMyNote(){
        SQLiteDatabase db = getWritableDatabase();

        return db.rawQuery("SELECT * FROM "+ TABLE_MY_NOTE, null);
    }

    List<TrashNoteMdl> getTrash() {
        List<TrashNoteMdl> trashList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TrashNoteMdl.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TrashNoteMdl trash = new TrashNoteMdl();
                trash.set_id(cursor.getInt(cursor.getColumnIndex(TrashNoteMdl.COLUMN_LIST_ID)));
                trash.setColor(cursor.getInt(cursor.getColumnIndex(
                        TrashNoteMdl.COLUMN_NOTE_COLOR)));
                trash.setDate_created(cursor.getString(cursor.getColumnIndex(
                        TrashNoteMdl.COLUMN_NOTE_DATE_CREATE)));
                trash.setDate_end(cursor.getString(cursor.getColumnIndex(
                        TrashNoteMdl.COLUMN_NOTE_DATE_END)));
                trash.setDescription(cursor.getString(cursor.getColumnIndex(
                        TrashNoteMdl.COLUMN_NOTE_DESCRIPTION)));
                trash.setTitle(cursor.getString(cursor.getColumnIndex(
                        TrashNoteMdl.COLUMN_NOTE_TITLE)));

                trashList.add(trash);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return trashList;
    }

    /**
     * This method fetches the specified attribute form the notes table that matches the id
     * @param id id of the note
     * @param attribute column to fetch data from
     * @return String representation of the data in the specified column
     */
    String getNoteAttribute(int id, String attribute) {
        SQLiteDatabase db = getWritableDatabase();

        // String the method will return
        String dbString;

        // select statement for the specific id
        String query = "SELECT * FROM " + TABLE_MY_NOTE + " WHERE " + COLUMN_LIST_ID + " = " + id;

        // execute statement
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        // if note in the Cursor isn't null
        if (!cursor.getString(cursor.getColumnIndex(attribute)).isEmpty()) {
            // get the note name and store it in the dbString
            dbString = cursor.getString(cursor.getColumnIndex(attribute));
        } else {
            dbString = "None";
        }

        cursor.close();

        db.close();

        return dbString;
    }

    void deleteNote(int id) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TrashNoteMdl.TABLE_NAME, COLUMN_LIST_ID + " = ?", new String[]{String.valueOf(id)});

        db.close();
    }

    void trashNote(int id) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT title, date_created, date_end, description, " +
                "color FROM my_notes WHERE _id = " + id, null);

        cursor.moveToFirst();

        ContentValues values = new ContentValues();

        values.put(TrashNoteMdl.COLUMN_NOTE_TITLE, cursor.getString(0));
        values.put(TrashNoteMdl.COLUMN_NOTE_DATE_CREATE, cursor.getString(1));
        values.put(TrashNoteMdl.COLUMN_NOTE_DATE_END, cursor.getString(2));
        values.put(TrashNoteMdl.COLUMN_NOTE_DESCRIPTION, cursor.getString(3));
        values.put(TrashNoteMdl.COLUMN_NOTE_COLOR, cursor.getInt(4));

        db.insert(TrashNoteMdl.TABLE_NAME, null, values);
        db.delete(TABLE_MY_NOTE, COLUMN_LIST_ID + " = ?", new String[]{String.valueOf(id)});

        cursor.close();
        db.close();
    }

    void restoreNote(int id) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT title, date_created, date_end, description, " +
                "color FROM " + TrashNoteMdl.TABLE_NAME + " WHERE _id = " + id, null);

        cursor.moveToFirst();

        ContentValues values = new ContentValues();

        values.put(TrashNoteMdl.COLUMN_NOTE_TITLE, cursor.getString(0));
        values.put(TrashNoteMdl.COLUMN_NOTE_DATE_CREATE, cursor.getString(1));
        values.put(TrashNoteMdl.COLUMN_NOTE_DATE_END, cursor.getString(2));
        values.put(TrashNoteMdl.COLUMN_NOTE_DESCRIPTION, cursor.getString(3));
        values.put(TrashNoteMdl.COLUMN_NOTE_COLOR, cursor.getInt(4));

        db.insert(TABLE_MY_NOTE, null, values);
        db.delete(TrashNoteMdl.TABLE_NAME, COLUMN_LIST_ID + " = ?", new String[]{String.valueOf(id)});

        cursor.close();
        db.close();
    }

    /**
     * Updates the column values for specific id
     * @param id id of note to edit
     * @param title replaces the old title
     * @param dateCreated replaces the old create date
     * @param dateEnd replaced the old end date if added
     * @param description replaces the old description
     */
    void updateNote(int id, String title, String dateCreated, String dateEnd, String description) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NOTE_TITLE, title);
        values.put(COLUMN_NOTE_DATE_CREATE, dateCreated);
        values.put(COLUMN_NOTE_DATE_END, dateEnd);
        values.put(COLUMN_NOTE_DESCRIPTION, description);

        db.update(TABLE_MY_NOTE, values, COLUMN_LIST_ID + " = ?", new String[]{String.valueOf(id)});

        db.close();
    }

    List<TrashNoteMdl> getNote2() {
        List<TrashNoteMdl> trashList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TrashNoteMdl.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TrashNoteMdl trash = new TrashNoteMdl();
                trash.set_id(cursor.getInt(cursor.getColumnIndex(TrashNoteMdl.COLUMN_LIST_ID)));
                trash.setColor(cursor.getInt(cursor.getColumnIndex(
                        TrashNoteMdl.COLUMN_NOTE_COLOR)));
                trash.setDate_created(cursor.getString(cursor.getColumnIndex(
                        TrashNoteMdl.COLUMN_NOTE_DATE_CREATE)));
                trash.setDate_end(cursor.getString(cursor.getColumnIndex(
                        TrashNoteMdl.COLUMN_NOTE_DATE_END)));
                trash.setDescription(cursor.getString(cursor.getColumnIndex(
                        TrashNoteMdl.COLUMN_NOTE_DESCRIPTION)));
                trash.setTitle(cursor.getString(cursor.getColumnIndex(
                        TrashNoteMdl.COLUMN_NOTE_TITLE)));

                trashList.add(trash);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return trashList;
    }

}
