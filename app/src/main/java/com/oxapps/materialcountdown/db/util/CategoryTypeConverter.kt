package com.oxapps.materialcountdown.db.util

import android.arch.persistence.room.TypeConverter
import com.oxapps.materialcountdown.model.Category


class CategoryTypeConverter {
    @TypeConverter
    fun toCategory(value: String): Category = Category.valueOf(value)

    @TypeConverter
    fun fromCategory(value: Category): String = value.toString();
}