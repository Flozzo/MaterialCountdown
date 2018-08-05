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

package com.oxapps.materialcountdown.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.oxapps.materialcountdown.db.util.CategoryTypeConverter
import com.oxapps.materialcountdown.model.Event


@Database(entities = [(Event::class)], version = 1)
@TypeConverters(CategoryTypeConverter::class)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        private const val DB_NAME = "EventStorage.db"
        private var INSTANCE: EventDatabase? = null

        fun getInstance(context: Context): EventDatabase {
            if (INSTANCE == null) {
                synchronized(EventDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        EventDatabase::class.java, DB_NAME
                    )
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}