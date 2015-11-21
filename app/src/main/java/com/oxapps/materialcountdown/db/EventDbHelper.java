package com.oxapps.materialcountdown.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.oxapps.materialcountdown.Event;

import java.util.ArrayList;

/**
 * Created by flynn on 21/11/15.
 */
public class EventDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "EventStorage.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "events";
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_END = "end";

    public EventDbHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, " + KEY_CATEGORY + " TEXT, "+ KEY_END + " INTEGER);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //No upgrades needed yet
    }

    public boolean addEvent(Event event) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName());
        values.put(KEY_END, event.getEndTime());
        values.put(KEY_CATEGORY, event.getCategory());
        boolean success = (db.insert(TABLE_NAME, null, values) != -1L);
        db.close();
        return(success);
    }

    public ArrayList<Event> getEvents() {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[]{KEY_NAME, KEY_END, KEY_CATEGORY};
        String selection = KEY_END + " >= " + System.currentTimeMillis();
        Cursor cursor = db.query(TABLE_NAME,columns, selection, null, null, null, KEY_END + " ASC");
        ArrayList<Event> events = new ArrayList<>();
        while(cursor.moveToNext()) {
            events.add(new Event(cursor.getString(0), cursor.getLong(1), cursor.getInt(2)));
        }
        cursor.close();
        db.close();
        return events;
    }

    public Event getEvent(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = KEY_ID + " = " + id;
        Cursor cursor = db.query(TABLE_NAME, null, selection, null, null, null, null);
        Event event = new Event(cursor.getInt(0), cursor.getString(1), cursor.getLong(2), cursor.getInt(3));
        cursor.close();
        db.close();
        return event;
    }

    public int size() {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }
}
