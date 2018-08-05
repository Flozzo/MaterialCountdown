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

package com.oxapps.materialcountdown.detail

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.oxapps.materialcountdown.R
import com.oxapps.materialcountdown.creation.EventCreationActivity
import com.oxapps.materialcountdown.model.Event
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.content_event_detail.*
import kotlinx.android.synthetic.main.toolbar_event_detail.*

import java.util.concurrent.TimeUnit

class EventDetailActivity : AppCompatActivity() {

    private var timer: CountDownTimer? = null
    private lateinit var event: Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        setSupportActionBar(eventDetailToolbar)
        event = intent.getParcelableExtra("event")
        initViews()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        timer = object : CountDownTimer(event.getRemainingTime(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                updateTimeTexts()
            }

            override fun onFinish() {}
        }.start()
    }

    override fun onPause() {
        super.onPause()
        timer!!.cancel()
        timer = null
    }

    private fun initViews() {
        val category = event.category
        val color = ContextCompat.getColor(this@EventDetailActivity, category.color)
        val darkColor = ContextCompat.getColor(this@EventDetailActivity, category.statusBarColor)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(color))
        setStatusBarColor(darkColor)
        eventDetailTitle.text = event.name
        eventDetailDescription.text = event.description
        updateTimeTexts()
    }

    fun editEvent(view: View) {
        val intent = Intent(this, EventCreationActivity::class.java)
        intent.action = ACTION_EDIT
        intent.putExtra("event", event)
        startActivityForResult(intent, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            event = data.getParcelableExtra("event")
            initViews()
        }
    }

    private fun updateTimeTexts() {
        var remainingTime = event.getRemainingTime()
        val days = TimeUnit.MILLISECONDS.toDays(remainingTime)
        remainingTime -= TimeUnit.DAYS.toMillis(days)
        val hours = TimeUnit.MILLISECONDS.toHours(remainingTime)
        remainingTime -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTime)
        remainingTime -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = remainingTime / 1000

        if (days > 0) {
            eventDetailDays.text = "$days ${getDayString(days)}"
        } else {
            eventDetailDays.visibility = View.GONE
        }
        val minuteString = String.format("%02d", minutes)
        val secondString = String.format("%02d", seconds)
        eventDetailHours.text = "$hours"
        eventDetailMinutes.text = minuteString
        eventDetailSeconds.text = secondString

    }

    private fun getDayString(days: Long): String {
        val dayRes = if (days == 1L) R.string.day_remaining else R.string.days_remaining
        return getString(dayRes)
    }

    private fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
        }
    }

    companion object {
        const val ACTION_EDIT = "edit"
    }
}
