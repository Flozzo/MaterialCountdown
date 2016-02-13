package com.oxapps.materialcountdown;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by flynn on 21/11/15.
 */
public class Event implements Parcelable {
    protected static final String CATEGORY = "category";
    private int id;
    private String name;
    private String description;
    private long endTime;
    private Category category;

    public Event(String name, String description, long endTime, Category category) {
        this.name = name;
        this.description = description;
        this.endTime = endTime;
        this.category = category;
    }

    public Event(int id, String name, String description, long endTime, Category category) {
        this(name, description, endTime, category);
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getRemainingTime() {
        return endTime - System.currentTimeMillis();
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() { return description;}

    protected Event(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        endTime = in.readLong();
        category = (Category) in.readValue(Category.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(endTime);
        dest.writeValue(category);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public int getId() {
        return id;
    }
}
