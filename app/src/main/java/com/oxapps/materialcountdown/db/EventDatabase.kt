package com.oxapps.materialcountdown.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.oxapps.materialcountdown.db.util.CategoryTypeConverter


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