/*
 * Copyright 2016 Flynn van Os
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

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import com.oxapps.materialcountdown.db.Event
import com.oxapps.materialcountdown.util.getRemainingTimeValues
import kotlinx.android.synthetic.main.event_item.view.*

class MainAdapter(private val mContext: Context) : BaseAdapter() {

    private var dataset: MutableList<Event>? = null

    internal class ViewHolder(view: View) {
        val adapterEventName = view.adapterEventName!!
        val adapterEventDescription = view.adapterEventDescription!!
        val adapterTimeCount = view.adapterTimeCount!!
        val adapterTimeText = view.adapterTimeText!!
        val adapterLogo = view.adapterLogo!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder
        if (view != null) {
            holder = view.tag as ViewHolder
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.event_item, parent, false)
            holder = ViewHolder(view)
            view!!.tag = holder
        }
        val event = dataset!![position]
        val (unitsLeft, unitText) = getRemainingTimeValues(event.getRemainingTime())
        val category = event.category

        holder.adapterEventName.text = event.name
        holder.adapterEventDescription.text = event.description
        holder.adapterTimeCount.text = unitsLeft.toString()
        holder.adapterTimeText.setText(unitText)
        holder.adapterLogo.setImageResource(category.icon)
        holder.adapterLogo.setBackgroundColor(ContextCompat.getColor(mContext, category.color))

        return view
    }

    override fun getCount() = dataset!!.size

    override fun getItem(position: Int) = dataset!![position]

    override fun getItemId(position: Int) = getItem(position).id!!

    internal fun setItems(items: MutableList<Event>) {
        this.dataset = items
    }

    internal fun removeItem(position: Int) {
        this.dataset!!.removeAt(position)
    }

    internal fun addItem(position: Int, event: Event) {
        this.dataset!!.add(position, event)
    }
}