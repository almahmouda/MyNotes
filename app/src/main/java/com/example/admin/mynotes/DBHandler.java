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

import static com.example.admin.mynotes.model.TrashNoteMdl.COLUMN_NOTE_TITLE;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "myNote.db";

    private static final String TABLE_MY_NOTE = "my_notes";

    // Common column names
    private static final String ID = TrashNoteMdl.COLUMN_LIST_ID;
    private static final String TITLE = COLUMN_NOTE_TITLE;
    private static final String DATE_CREATE = TrashNoteMdl.COLUMN_NOTE_DATE_CREATE;
    private static final String DATE_END = TrashNoteMdl.COLUMN_NOTE_DATE_END;
    private static final String DESCRIPTION = TrashNoteMdl.COLUMN_NOTE_DESCRIPTION;
    private static final String COLOR = TrashNoteMdl.COLUMN_NOTE_COLOR;
    private static final String TIMESTAMP_CREATE= TrashNoteMdl.COLUMN_NOTE_STAMP_CREATE;
    private static final String TIMESTAMP_EDIT = TrashNoteMdl.COLUMN_NOTE_STAMP_EDIT;


    DBHandler (Context context, SQLiteDatabase.CursorFactory factory){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String qCrateNoteTable = "CREATE TABLE " + TABLE_MY_NOTE + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT, " +
                DATE_CREATE + " TEXT, " +
                DATE_END + " TEST, " +
                DESCRIPTION + " TEST, " +
                COLOR + " INTEGER, " +
                TIMESTAMP_CREATE + " DATETIME DEFAULT (datetime('now', 'localtime')), " +
                TIMESTAMP_EDIT + " DATETIME DEFAULT (datetime('now', 'localtime'))" +
                ");";

        sqLiteDatabase.execSQL(qCrateNoteTable);
        sqLiteDatabase.execSQL(TrashNoteMdl.CREATE_TABLE);
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

        values.put(TITLE, title);
        values.put(DATE_CREATE, dateCreated);
        values.put(DATE_END, dateEnd);
        values.put(DESCRIPTION, description);
        values.put(COLOR, color);

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
                trash.set_id(cursor.getInt(0));
                trash.setTitle(cursor.getString(1));
                trash.setDate_created(cursor.getString(2));
                trash.setDate_end(cursor.getString(3));
                trash.setDescription(cursor.getString(4));
                trash.setColor(cursor.getInt(5));
                trash.setTimestamp_create(cursor.getString(6));
                trash.setTimestamp_edit(cursor.getString(7));
                trash.setTimestamp_delete(cursor.getString(8));

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
        String query = "SELECT * FROM " + TABLE_MY_NOTE + " WHERE " + ID + " = " + id;

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

        db.delete(TrashNoteMdl.TABLE_NAME, ID + " = ?", new String[]{String.valueOf(id)});

        db.close();
    }

    void trashNote(int id) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM my_notes WHERE _id = " + id, null);

        cursor.moveToFirst();

        ContentValues values = new ContentValues();

        values.put(TITLE, cursor.getString(1));
        values.put(DATE_CREATE, cursor.getString(2));
        values.put(DATE_END, cursor.getString(3));
        values.put(DESCRIPTION, cursor.getString(4));
        values.put(COLOR, cursor.getInt(5));
        values.put(TIMESTAMP_CREATE, cursor.getString(6));
        values.put(TIMESTAMP_EDIT, cursor.getString(7));

        db.insert(TrashNoteMdl.TABLE_NAME, null, values);
        db.delete(TABLE_MY_NOTE, ID + " = ?", new String[]{String.valueOf(id)});

        cursor.close();
        db.close();
    }

    void restoreNote(int id) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TrashNoteMdl.TABLE_NAME + " WHERE _id = " + id, null);

        cursor.moveToFirst();

        ContentValues values = new ContentValues();

        values.put(TITLE, cursor.getString(1));
        values.put(DATE_CREATE, cursor.getString(2));
        values.put(DATE_END, cursor.getString(3));
        values.put(DESCRIPTION, cursor.getString(4));
        values.put(COLOR, cursor.getInt(5));
        values.put(TIMESTAMP_CREATE, cursor.getString(6));
        values.put(TIMESTAMP_EDIT, cursor.getString(7));

        db.insert(TABLE_MY_NOTE, null, values);
        db.delete(TrashNoteMdl.TABLE_NAME, ID + " = ?", new String[]{String.valueOf(id)});

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

        String dateNow = "UPDATE " + TABLE_MY_NOTE + " SET " + TIMESTAMP_EDIT +
                " = datetime('now', 'localtime') WHERE _id = " + id;

        db.execSQL(dateNow);

        ContentValues values = new ContentValues();

        values.put(TITLE, title);
        values.put(DATE_CREATE, dateCreated);
        values.put(DATE_END, dateEnd);
        values.put(DESCRIPTION, description);

        db.update(TABLE_MY_NOTE, values, ID + " = ?", new String[]{String.valueOf(id)});

        db.close();
    }

    List<TrashNoteMdl> getNote2() {
        List<TrashNoteMdl> trashList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TrashNoteMdl.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TrashNoteMdl trash = new TrashNoteMdl();
                trash.set_id(cursor.getInt(0));
                trash.setTitle(cursor.getString(1));
                trash.setDate_created(cursor.getString(2));
                trash.setDate_end(cursor.getString(3));
                trash.setDescription(cursor.getString(4));
                trash.setColor(cursor.getInt(5));
                trash.setTimestamp_create(cursor.getString(6));
                trash.setTimestamp_edit(cursor.getString(7));
//                trash.setTimestamp_delete(cursor.getString(8));

                trashList.add(trash);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return trashList;
    }

}
