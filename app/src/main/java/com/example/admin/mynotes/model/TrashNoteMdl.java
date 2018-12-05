package com.example.admin.mynotes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class TrashNoteMdl implements Parcelable, Comparable<TrashNoteMdl> {

    // columns for SQL query
    public static final String TABLE_NAME = "trash";
    public static final String COLUMN_LIST_ID = "_id";
    public static final String COLUMN_NOTE_TITLE = "title";
    public static final String COLUMN_NOTE_DATE_CREATE = "date_created";
    public static final String COLUMN_NOTE_DATE_END = "date_end";
    public static final String COLUMN_NOTE_DESCRIPTION = "description";
    public static final String COLUMN_NOTE_COLOR = "color";
    public static final String COLUMN_NOTE_STAMP_CREATE = "created";
    public static final String COLUMN_NOTE_STAMP_EDIT = "edited";
    public static final String COLUMN_NOTE_STAMP_DELETE = "deleted";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOTE_TITLE + " TEXT, " +
                    COLUMN_NOTE_DATE_CREATE + " TEXT, " +
                    COLUMN_NOTE_DATE_END + " TEST, " +
                    COLUMN_NOTE_DESCRIPTION + " TEST, " +
                    COLUMN_NOTE_COLOR + " INTEGER, " +
                    COLUMN_NOTE_STAMP_CREATE + " TEXT, " +
                    COLUMN_NOTE_STAMP_EDIT + " TEXT, " +
                    COLUMN_NOTE_STAMP_DELETE + " DATETIME DEFAULT (datetime('now', 'localtime'))" +
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
    public String timestamp_create;
    public String timestamp_edit;
    public String timestamp_delete;

    public TrashNoteMdl() {
    }

    public TrashNoteMdl(int _id, String title, String date_created, String date_end,
                        String description, int color, String stamp_create, String stamp_edit,
                        String stamp_delete) {
        this._id = _id;
        this.title = title;
        this.date_created = date_created;
        this.date_end = date_end;
        this.description = description;
        this.color = color;
        this.timestamp_create = stamp_create;
        this.timestamp_edit = stamp_edit;
        this.timestamp_delete = stamp_delete;

    }

    private TrashNoteMdl(Parcel in) {
        _id = in.readInt();
        title = in.readString();
        date_created = in.readString();
        date_end = in.readString();
        description = in.readString();
        color = in.readInt();
        timestamp_create = in.readString();
        timestamp_edit = in.readString();
        timestamp_delete = in.readString();
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

    public String getTimestamp_create() {
        return timestamp_create;
    }

    public void setTimestamp_create(String timestamp_create) {
        this.timestamp_create = timestamp_create;
    }

    public String getTimestamp_edit() {
        return timestamp_edit;
    }

    public void setTimestamp_edit(String timestamp_edit) {
        this.timestamp_edit = timestamp_edit;
    }

    public String getTimestamp_delete() {
        return timestamp_delete;
    }

    public void setTimestamp_delete(String timestamp_delete) {
        this.timestamp_delete = timestamp_delete;
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
        dest.writeString(timestamp_create);
        dest.writeString(timestamp_edit);
        dest.writeString(timestamp_delete);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(TrashNoteMdl o) {
        return Comparators.ALPHA.compare(this, o);
    }

    /**
     *
     */
    public static class Comparators {
        public static Comparator<TrashNoteMdl> ID = new Comparator<TrashNoteMdl>() {
            @Override
            public int compare(TrashNoteMdl o1, TrashNoteMdl o2) {
                return o1._id - o2._id;
            }
        };
        public static Comparator<TrashNoteMdl> ALPHA = new Comparator<TrashNoteMdl>() {
            @Override
            public int compare(TrashNoteMdl o1, TrashNoteMdl o2) {
                return o1.title.compareTo(o2.title);
            }
        };
        public static Comparator<TrashNoteMdl> RE_CREATE_DATE = new Comparator<TrashNoteMdl>() {
            @Override
            public int compare(TrashNoteMdl o1, TrashNoteMdl o2) {
                return o2.timestamp_create.compareTo(o1.timestamp_create);
            }
        };
        public static Comparator<TrashNoteMdl> CREATE_DATE = new Comparator<TrashNoteMdl>() {
            @Override
            public int compare(TrashNoteMdl o1, TrashNoteMdl o2) {
                return o1.timestamp_create.compareTo(o2.timestamp_create);
            }
        };
        public static Comparator<TrashNoteMdl> RE_DELETE_DATE = new Comparator<TrashNoteMdl>() {
            @Override
            public int compare(TrashNoteMdl o1, TrashNoteMdl o2) {
                return o2.timestamp_delete.compareTo(o1.timestamp_delete);
            }
        };

    }
}
