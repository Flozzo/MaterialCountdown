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

package com.oxapps.materialcountdown.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.oxapps.materialcountdown.db.util.CATEGORY
import com.oxapps.materialcountdown.db.util.DESC
import com.oxapps.materialcountdown.db.util.END
import com.oxapps.materialcountdown.db.util.NAME
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