package com.oxapps.materialcountdown;

/**
 * Created by flynn on 21/11/15.
 */
public class Event {
    protected static final String CATEGORY = "category";
    private int id;
    private String name;
    private String description;
    private long endTime;
    private int category;

    public Event(String name, String description, long endTime, int category) {
        this.name = name;
        this.description = description;
        this.endTime = endTime;
        this.category = category;
    }

    public Event(int id, String description, String name, long endTime, int category) {
        this(name, description, endTime, category);
        this.id = id;
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

    public String getDescription() { return description;}
}
