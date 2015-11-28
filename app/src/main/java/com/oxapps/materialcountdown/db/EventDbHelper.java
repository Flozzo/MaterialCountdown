package com.oxapps.materialcountdown.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.oxapps.materialcountdown.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by flynn on 21/11/15.
 */
public class EventDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "EventStorage.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "events";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESC = "description";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_END = "end";
    private Context context;

    public EventDbHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
        context = ctx;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, " + KEY_DESC + " TEXT, " + KEY_CATEGORY + " TEXT, "+ KEY_END + " INTEGER);";
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
        values.put(KEY_DESC, event.getDescription());
        values.put(KEY_END, event.getEndTime());
        values.put(KEY_CATEGORY, event.getCategory());
        boolean success = (db.insert(TABLE_NAME, null, values) != -1L);
        db.close();
        return(success);
    }

    public ArrayList<Event> getEvents() {
        SQLiteDatabase db = getReadableDatabase();
        //String[] columns = new String[]{KEY_NAME, KEY_DESC, KEY_END, KEY_CATEGORY};
        String selection = KEY_END + " >= " + System.currentTimeMillis();
        Cursor cursor = db.query(TABLE_NAME,null , selection, null, null, null, KEY_END + " ASC");
        ArrayList<Event> events = new ArrayList<>();
        while(cursor.moveToNext()) {
            events.add(new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getLong(4), cursor.getInt(3)));
        }
        cursor.close();
        db.close();
        return events;
    }

    public ArrayList<Map<String, String>> getEventsForMainList() {
        SQLiteDatabase db = getReadableDatabase();
        //String[] columns = new String[]{KEY_NAME, KEY_DESC, KEY_END, KEY_CATEGORY};
        String selection = KEY_END + " >= " + System.currentTimeMillis();
        Cursor cursor = db.query(TABLE_NAME, null, selection, null, null, null, KEY_END + " ASC");
        ArrayList<Map<String, String>> events = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(KEY_ID, cursor.getString(0));
            map.put(KEY_NAME, cursor.getString(1));
            map.put(KEY_DESC, cursor.getString(2));

            map.put(KEY_CATEGORY, cursor.getString(3));
            long end = cursor.getLong(4);
            map.put(KEY_END, getRemainingTime(end));
            events.add(map);
        }
        cursor.close();
        db.close();
        return events;
    }

    private String getRemainingTime(long end) {
        String dateText;
        long remaining = end - System.currentTimeMillis();
        dateText = TimeUnit.MILLISECONDS.toDays(remaining) + "";

        return dateText;
    }

    public Event getEvent(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = KEY_ID + " = " + id;
        Cursor cursor = db.query(TABLE_NAME, null, selection, null, null, null, null);
        Event event = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4));
        cursor.close();
        db.close();
        return event;
    }

    public boolean removeEvent(int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int result = db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(id)});
            return result == 1;
        } finally {
            db.close();
        }

    }

    public int size() {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }
}
