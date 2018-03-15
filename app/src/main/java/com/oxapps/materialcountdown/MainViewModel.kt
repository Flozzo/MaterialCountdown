package com.oxapps.materialcountdown

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.oxapps.materialcountdown.db.EventDatabase
import com.oxapps.materialcountdown.model.Event
import java.util.concurrent.Executors

/**
 * Created by Flynn on 14/03/2018.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val executor = Executors.newFixedThreadPool(2)

    private val eventDao = EventDatabase.getInstance(application).eventDao()

    fun getEvents(): LiveData<List<Event>> = eventDao.getEvents()

    fun removeEvent(event: Event) {
        executor.execute { eventDao.delete(event) }
    }

}