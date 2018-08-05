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
import android.widget.TextView

import com.oxapps.materialcountdown.model.Event
import com.oxapps.materialcountdown.util.getRemainingTimeValues
import com.oxapps.materialcountdown.view.CircularIconImageView
import kotlinx.android.synthetic.main.event_item_multi.view.*
import kotlinx.android.synthetic.main.event_item_single.view.*

class MainAdapter(private val mContext: Context) : BaseAdapter() {

    private var dataset: MutableList<Event>? = null

    internal abstract class ListItemViewHolder {
        abstract val adapterEventName: TextView
        abstract val adapterTimeCount: TextView
        abstract val adapterTimeText: TextView
        abstract val adapterLogo: CircularIconImageView
    }

    internal class MultiLineViewHolder(view: View) : ListItemViewHolder() {
        override val adapterEventName = view.adapterEventName!!
        val adapterEventDescription = view.adapterEventDescription!!
        override val adapterTimeCount = view.adapterTimeCount!!
        override val adapterTimeText = view.adapterTimeText!!
        override val adapterLogo = view.adapterLogo!!
    }

    internal class SingleLineViewHolder(view: View) : ListItemViewHolder() {
        override val adapterEventName = view.adapterEventNameSingle!!
        override val adapterTimeCount = view.adapterTimeCountSingle!!
        override val adapterTimeText = view.adapterTimeTextSingle!!
        override val adapterLogo = view.adapterLogoSingle!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val event = dataset!![position]
        val (unitsLeft, unitText) = getRemainingTimeValues(event.getRemainingTime())
        val category = event.category
        val holder: ListItemViewHolder

        if (event.description.isNullOrBlank()) {
            if (view != null) {
                holder = view.tag as SingleLineViewHolder
            } else {
                view = LayoutInflater.from(mContext)
                    .inflate(R.layout.event_item_single, parent, false)
                holder = SingleLineViewHolder(view)
                view!!.tag = holder
            }
        } else {
            if (view != null) {
                holder = view.tag as MultiLineViewHolder
            } else {
                view = LayoutInflater.from(mContext)
                    .inflate(R.layout.event_item_multi, parent, false)
                holder = MultiLineViewHolder(view)
                view!!.tag = holder
            }
            holder.adapterEventDescription.text = event.description
        }
        holder.adapterEventName.text = event.name
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