package com.oxapps.materialcountdown.creation

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.oxapps.materialcountdown.db.Event
import com.oxapps.materialcountdown.db.EventDatabase
import java.util.concurrent.Executors


class EventCreationViewModel(application: Application) : AndroidViewModel(application) {

    private val executor = Executors.newFixedThreadPool(1)

    private val eventDao = EventDatabase.getInstance(application).eventDao()

    fun addEvent(event: Event) {
        executor.execute { eventDao.insert(event) }
    }

}