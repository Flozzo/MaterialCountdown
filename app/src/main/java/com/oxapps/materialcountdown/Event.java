package com.oxapps.materialcountdown;

/**
 * Created by flynn on 21/11/15.
 */
public class Event {
    private int id;
    private String name;
    private long endTime;
    private int category;

    public Event(String name, long endTime, int category) {
        this.name = name;
        this.endTime = endTime;
        this.category = category;
    }

    public Event(int id, String name, long endTime, int category) {
        this.id = id;
        this.name = name;
        this.endTime = endTime;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getCategory() {
        return category;
    }
}
