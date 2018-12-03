package com.example.admin.mynotes.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TrashNoteMdl implements Parcelable {

    // columns for SQL query
    public static final String TABLE_NAME = "trash";
    public static final String COLUMN_LIST_ID = "_id";
    public static final String COLUMN_NOTE_TITLE = "title";
    public static final String COLUMN_NOTE_DATE_CREATE = "date_created";
    public static final String COLUMN_NOTE_DATE_END = "date_end";
    public static final String COLUMN_NOTE_DESCRIPTION = "description";
    public static final String COLUMN_NOTE_COLOR = "color";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOTE_TITLE + " TEXT, " +
                    COLUMN_NOTE_DATE_CREATE + " TEXT, " +
                    COLUMN_NOTE_DATE_END + " TEST, " +
                    COLUMN_NOTE_DESCRIPTION + " TEST, " +
                    COLUMN_NOTE_COLOR + " INTEGER" +
                    ");";
    public static final Parcelable.Creator<TrashNoteMdl> CREATOR = new Creator<TrashNoteMdl>() {
        @Override
        public TrashNoteMdl createFromParcel(Parcel source) {
            return new TrashNoteMdl(source);
        }

        @Override
        public TrashNoteMdl[] newArray(int size) {
            return new TrashNoteMdl[0];
        }
    };
    // fields for creating trash note
    public int _id;
    public String title;
    public String date_created;
    public String date_end;
    public String description;
    public int color;

    public TrashNoteMdl() {
    }

    public TrashNoteMdl(int _id, String title, String date_created, String date_end,
                        String description, int color) {
        this._id = _id;
        this.title = title;
        this.date_created = date_created;
        this.date_end = date_end;
        this.description = description;
        this.color = color;
    }

    public TrashNoteMdl(Parcel in) {
        _id = in.readInt();
        title = in.readString();
        date_created = in.readString();
        date_end = in.readString();
        description = in.readString();
        color = in.readInt();
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return hashCode();
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(title);
        dest.writeString(date_created);
        dest.writeString(date_end);
        dest.writeString(description);
        dest.writeInt(color);
    }
}
