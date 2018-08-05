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