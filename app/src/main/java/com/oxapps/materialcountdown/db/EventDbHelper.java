package com.oxapps.materialcountdown.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.oxapps.materialcountdown.Category;
import com.oxapps.materialcountdown.Event;
import com.oxapps.materialcountdown.R;

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
    public static final String DAY = "day";
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
        values.put(KEY_CATEGORY, event.getCategory().ordinal());
        boolean success = (db.insert(TABLE_NAME, null, values) != -1L);
        db.close();
        return(success);
    }

    public void editEvent(Event event) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName());
        values.put(KEY_DESC, event.getDescription());
        values.put(KEY_END, event.getEndTime());
        values.put(KEY_CATEGORY, event.getCategory().ordinal());
        db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(event.getId())});
        db.close();
    }

    public ArrayList<Event> getEvents() {
        SQLiteDatabase db = getReadableDatabase();
        //String[] columns = new String[]{KEY_NAME, KEY_DESC, KEY_END, KEY_CATEGORY};
        String selection = KEY_END + " >= " + System.currentTimeMillis();
        Cursor cursor = db.query(TABLE_NAME,null , selection, null, null, null, KEY_END + " ASC");
        ArrayList<Event> events = new ArrayList<>();
        Category[] categories = Category.values();
        while(cursor.moveToNext()) {
            Category category = categories[cursor.getInt(3)];
            events.add(new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getLong(4), category));
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
            String[] time = getDayString(end);
            map.put(DAY, time[1]);
            map.put(KEY_END, time[0]);
            events.add(map);
        }
        cursor.close();
        db.close();
        return events;
    }

    private String getDayString(String remaining) {
        int dayRes = remaining.equals("1") ? R.string.day_remaining : R.string.days_remaining;
        return context.getString(dayRes);
    }

    private String[] getDayString(long end) {
        long remaining = end - System.currentTimeMillis();
        int timeUnit;
        long timeValue;

        long dayMillis = TimeUnit.DAYS.toMillis(1);
        long hourMillis = dayMillis / 24;
        long minuteMillis = hourMillis / 60;

        if (remaining > 2 * dayMillis) {
            timeValue = TimeUnit.MILLISECONDS.toDays(remaining);
            timeUnit = R.string.days_remaining;
        } else if (remaining > dayMillis) {
            timeValue = 1;
            timeUnit = R.string.day_remaining;
        } else if (remaining > 2 * hourMillis) {
            timeValue = TimeUnit.MILLISECONDS.toHours(remaining);
            timeUnit = R.string.hours_remaining;
        } else if (remaining > hourMillis) {
            timeValue = 1;
            timeUnit = R.string.hour_remaining;
        } else if (remaining > 2 * minuteMillis) {
            timeValue = TimeUnit.MILLISECONDS.toMinutes(remaining);
            timeUnit = R.string.minutes_remaining;
        } else {
            timeValue = 1;
            timeUnit = R.string.minute_remaining;
        }
        return new String[]{String.valueOf(timeValue), context.getString(timeUnit)};
    }

    private String getRemainingTime(long end) {
        String dateText;
        long remaining = end - System.currentTimeMillis();
        dateText = TimeUnit.MILLISECONDS.toDays(remaining) + "";

        return dateText;
    }

    public long getEndTime(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[]{KEY_END};
        String selection = KEY_ID + " = ?";
        Cursor cursor = db.query(TABLE_NAME, columns, selection, new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        long time = cursor.getLong(0);
        cursor.close();
        db.close();
        return time;
    }

    public Event getEvent(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = KEY_ID + " = ?";
        Cursor cursor = db.query(TABLE_NAME, null, selection, new String[]{String.valueOf(id)}, null, null, null);
        Category[] categories = Category.values();
        Category category = categories[cursor.getInt(3)];
        Event event = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getLong(4), category);
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
