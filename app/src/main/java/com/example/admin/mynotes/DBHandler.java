package com.example.admin.mynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myNote.db";

    private static final String TABLE_MY_NOTE = "my_notes";
    private static final String COLUMN_LIST_ID ="_id";
    private static final String COLUMN_NOTE_TITLE = "title";
    private static final String COLUMN_NOTE_DATE_CREATE = "date_created";
    private static final String COLUMN_NOTE_DATE_END = "date_end";
    private static final String COLUMN_NOTE_DESCRIPTION = "description";

    public DBHandler (Context context, SQLiteDatabase.CursorFactory factory){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + TABLE_MY_NOTE + "(" +
                COLUMN_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTE_TITLE + " TEXT, " +
                COLUMN_NOTE_DATE_CREATE + " TEXT, " +
                COLUMN_NOTE_DATE_END + " TEST, " +
                COLUMN_NOTE_DESCRIPTION + " TEST" +
                ");";


        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_NOTE );

        onCreate(sqLiteDatabase);
    }
    public void addMyNote (String title, String dateCreated, String dateEnd, String description){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NOTE_TITLE, title);
        values.put(COLUMN_NOTE_DATE_CREATE, dateCreated);
        values.put(COLUMN_NOTE_DATE_END, dateEnd);
        values.put(COLUMN_NOTE_DESCRIPTION, description);

        db.insert(TABLE_MY_NOTE,null,values);
        db.close();
    }
    public Cursor getMyNote(){
        SQLiteDatabase db = getWritableDatabase();

        return db.rawQuery("SELECT * FROM "+ TABLE_MY_NOTE,null);
    }

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

}
