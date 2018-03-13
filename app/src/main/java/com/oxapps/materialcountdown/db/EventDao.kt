package com.oxapps.materialcountdown.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query


@Dao
interface EventDao {

    @Query("SELECT * FROM events")
    fun getEvents(): List<Event>

    @Insert(onConflict = REPLACE)
    fun insertAll(event: Event)

    @Delete
    fun delete(event: Event)
}