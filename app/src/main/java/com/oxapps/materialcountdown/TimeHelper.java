/*
 * Copyright 2016 Flynn van Os
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
