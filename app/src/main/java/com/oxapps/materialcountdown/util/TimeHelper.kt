/*
 * Copyright 2018 Flynn van Os
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

package com.oxapps.materialcountdown.util

import com.oxapps.materialcountdown.R
import java.util.concurrent.TimeUnit

data class RemainingTimeValue(val unitsLeft: Int, val unitText: Int)

internal fun getRemainingTimeValues(remainingMillis: Long): RemainingTimeValue {
    val timeUnit: Int
    val timeValue: Int

    when {
        remainingMillis >= TimeUnit.DAYS.toMillis(2) -> {
            timeValue = TimeUnit.MILLISECONDS.toDays(remainingMillis).toInt()
            timeUnit = R.string.days_remaining
        }
        remainingMillis >= TimeUnit.DAYS.toMillis(1) -> {
            timeValue = 1
            timeUnit = R.string.day_remaining
        }
        remainingMillis >= TimeUnit.HOURS.toMillis(2) -> {
            timeValue = TimeUnit.MILLISECONDS.toHours(remainingMillis).toInt()
            timeUnit = R.string.hours_remaining
        }
        remainingMillis >= TimeUnit.HOURS.toMillis(1) -> {
            timeValue = 1
            timeUnit = R.string.hour_remaining
        }
        remainingMillis >= TimeUnit.MINUTES.toMillis(2) -> {
            timeValue = TimeUnit.MILLISECONDS.toMinutes(remainingMillis).toInt()
            timeUnit = R.string.minutes_remaining
        }
        else -> {
            timeValue = 1
            timeUnit = R.string.minute_remaining
        }
    }
    return RemainingTimeValue(timeValue, timeUnit)
}
