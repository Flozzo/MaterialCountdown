package com.oxapps.materialcountdown.creation

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.oxapps.materialcountdown.db.Event
import com.oxapps.materialcountdown.db.EventDatabase
import com.oxapps.materialcountdown.model.Category
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