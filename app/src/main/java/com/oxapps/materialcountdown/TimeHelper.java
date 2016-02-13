package com.oxapps.materialcountdown;

import java.util.concurrent.TimeUnit;

/**
 * Created by flynn on 13/02/16.
 */
public class TimeHelper {
    protected static int[] getRemainingShort(long remaining) {
        int timeUnit;
        int timeValue;

        long dayMillis = TimeUnit.DAYS.toMillis(1);
        long hourMillis = dayMillis / 24;
        long minuteMillis = hourMillis / 60;

        if (remaining > 2 * dayMillis) {
            timeValue = (int) TimeUnit.MILLISECONDS.toDays(remaining);
            timeUnit = R.string.days_remaining;
        } else if (remaining > dayMillis) {
            timeValue = 1;
            timeUnit = R.string.day_remaining;
        } else if (remaining > 2 * hourMillis) {
            timeValue = (int) TimeUnit.MILLISECONDS.toHours(remaining);
            timeUnit = R.string.hours_remaining;
        } else if (remaining > hourMillis) {
            timeValue = 1;
            timeUnit = R.string.hour_remaining;
        } else if (remaining > 2 * minuteMillis) {
            timeValue = (int) TimeUnit.MILLISECONDS.toMinutes(remaining);
            timeUnit = R.string.minutes_remaining;
        } else {
            timeValue = 1;
            timeUnit = R.string.minute_remaining;
        }
        return new int[]{timeValue, timeUnit};
    }
}
