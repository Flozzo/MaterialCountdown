package com.oxapps.materialcountdown.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.oxapps.materialcountdown.db.util.CATEGORY
import com.oxapps.materialcountdown.db.util.DESC
import com.oxapps.materialcountdown.db.util.END
import com.oxapps.materialcountdown.db.util.NAME
import com.oxapps.materialcountdown.model.Category
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "events")
@Parcelize
data class Event(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = NAME) var name: String,
    @ColumnInfo(name = DESC) var description: String?,
    @ColumnInfo(name = END) var endTime: Long,
    @ColumnInfo(name = CATEGORY) var category: Category
): Parcelable {


    fun getRemainingTime(): Long {
        return endTime - System.currentTimeMillis()
    }
}