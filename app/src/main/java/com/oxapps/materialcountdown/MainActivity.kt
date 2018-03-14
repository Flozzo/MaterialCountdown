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

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView

import com.oxapps.materialcountdown.creation.EventCreationActivity
import com.oxapps.materialcountdown.detail.EventDetailActivity
import com.wdullaer.swipeactionadapter.SwipeActionAdapter
import com.wdullaer.swipeactionadapter.SwipeDirection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main_empty.*

class MainActivity : AppCompatActivity(), SwipeActionAdapter.SwipeActionListener,
    AdapterView.OnItemClickListener {
    private lateinit var viewModel: MainViewModel

    private var mainAdapter = MainAdapter(this)
    private var swipeAdapter: SwipeActionAdapter = SwipeActionAdapter(mainAdapter)
        .setFixedBackgrounds(true)
        .setSwipeActionListener(this)
        .addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT, R.layout.swipe_bg_main_left)
        .addBackground(SwipeDirection.DIRECTION_NORMAL_RIGHT, R.layout.swipe_bg_main_right)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainList.onItemClickListener = this@MainActivity
        swipeAdapter.setListView(mainList)
    }

    override fun onResume() {
        super.onResume()

        viewModel.getEvents().observe(this, Observer { events ->
            if (events == null || events.isEmpty()) {
                showEmptyMessage()
                return@Observer
            }
            hideEmptyMessage()
            mainAdapter.setItems(events.toMutableList())
            mainList.adapter = swipeAdapter
        })
    }

    fun createNewEvent(view: View) {
        val intent = Intent(this, EventCreationActivity::class.java)
        startActivity(intent)
    }

    override fun hasActions(position: Int, direction: SwipeDirection): Boolean {
        return direction.isLeft || direction.isRight
    }

    override fun shouldDismiss(position: Int, direction: SwipeDirection): Boolean {
        return direction.isLeft || direction.isRight
    }

    override fun onSwipe(position: IntArray, direction: Array<SwipeDirection>) {
        val event = mainAdapter.getItem(position[0])
        mainAdapter.removeItem(position[0])
        if (mainAdapter.isEmpty) {
            showEmptyMessage()
        }
        swipeAdapter.notifyDataSetChanged()
        val snackbar = Snackbar.make(
            findViewById(R.id.coordinator_main),
            R.string.item_removed,
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(R.string.undo) { _ ->
            mainAdapter.addItem(position[0], event)
            swipeAdapter.notifyDataSetChanged()
            hideEmptyMessage()
        }
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(snackbar: Snackbar?, swipeEvent: Int) {
                super.onDismissed(snackbar, swipeEvent)
                if (swipeEvent != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    viewModel.removeEvent(event)
                }

            }
        })
        snackbar.show()
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val i = Intent(this, EventDetailActivity::class.java)
        val event = mainAdapter.getItem(position)
        i.putExtra("event", event)
        startActivity(i)
    }

    private fun hideEmptyMessage() {
        mainList.visibility = View.VISIBLE
        mainEmptyView.visibility = View.GONE
    }

    private fun showEmptyMessage() {
        mainList.visibility = View.GONE
        mainEmptyView.visibility = View.VISIBLE
    }
}
