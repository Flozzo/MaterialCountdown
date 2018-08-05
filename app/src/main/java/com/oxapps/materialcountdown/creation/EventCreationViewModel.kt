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

package com.oxapps.materialcountdown.creation

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.oxapps.materialcountdown.db.EventDatabase
import com.oxapps.materialcountdown.model.Category
import com.oxapps.materialcountdown.model.Event
import java.util.*
import java.util.concurrent.Executors


class EventCreationViewModel(application: Application) : AndroidViewModel(application) {

    private val executor = Executors.newFixedThreadPool(1)

    private val eventDao = EventDatabase.getInstance(application).eventDao()

    var calendar = GregorianCalendar()

    var category: Category? = null

    init {
        calendar = GregorianCalendar()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    fun addEvent(event: Event) {
        executor.execute { eventDao.insert(event) }
    }

    fun setTimeInMillis(millis: Long) {
        calendar.timeInMillis = millis
    }

    fun setDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        calendar.set(year, monthOfYear, dayOfMonth)
    }

    fun setTime(hourOfDay: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
    }

    fun isDateInPast() = calendar.before(Calendar.getInstance())

}