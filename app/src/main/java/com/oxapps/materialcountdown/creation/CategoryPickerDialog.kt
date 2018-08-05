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

package com.oxapps.materialcountdown.creation

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatDialog
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleAdapter
import com.oxapps.materialcountdown.R
import com.oxapps.materialcountdown.model.Category

class CategoryPickerDialog(context: Context) : AppCompatDialog(context) {

    lateinit var onCategorySet: (category: Category) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.set_category)


        val categoryList = layoutInflater.inflate(R.layout.dialog_category_list, null) as ListView

        val categoryMap = Category.values().map { category ->
            mapOf(
                "icon" to getGreyDrawable(category.icon),
                "name" to context.getString(category.getName())
            )
        }
        val from = arrayOf("icon", "name")
        val to = intArrayOf(R.id.iv_category_logo, R.id.category_name)
        val adapter = SimpleAdapter(context, categoryMap, R.layout.category_item, from, to)
        adapter.viewBinder = SimpleAdapter.ViewBinder { view, data, _ ->
            if (view is ImageView) {
                view.setImageDrawable(data as Drawable)
                return@ViewBinder true
            }
            false
        }
        categoryList.divider = null
        categoryList.adapter = adapter
        setContentView(categoryList)
        categoryList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val category = Category.values()[position]
            onCategorySet(category)
            hide()
        }
    }

    private fun getGreyDrawable(icon: Int): Drawable {
        val drawable = ContextCompat.getDrawable(context, icon)!!.mutate()
        drawable.setColorFilter(-0x76000000, PorterDuff.Mode.MULTIPLY)
        return drawable
    }
}
