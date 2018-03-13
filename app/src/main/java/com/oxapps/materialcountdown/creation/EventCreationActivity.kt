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

package com.oxapps.materialcountdown.creation

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View

import com.oxapps.materialcountdown.EventDetailActivity
import com.oxapps.materialcountdown.R
import com.oxapps.materialcountdown.db.Event
import com.oxapps.materialcountdown.model.Category
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog

import kotlinx.android.synthetic.main.activity_new_event.*
import kotlinx.android.synthetic.main.content_new_event.*
import kotlinx.android.synthetic.main.toolbar_new_event.*

import java.util.Calendar

class EventCreationActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private var editId: Long = -1

    private lateinit var viewModel: EventCreationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event)

        viewModel = ViewModelProviders.of(this).get(EventCreationViewModel::class.java)

        setSupportActionBar(newEventToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (intent.action != null && intent.action == EventDetailActivity.ACTION_EDIT) {
            initForEdit()
        }
    }

    private fun initForEdit() {
        val event = intent.getParcelableExtra<Event>("event")
        viewModel.setTimeInMillis(event.endTime)
        newEventTitleView.setText(event.name)
        newEventDescriptionView.setText(event.description)
        viewModel.category = event.category
        setDateText()
        setTimeText()
        onCategorySet()
        editId = event.id!!
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_new_event, menu)
        return true
    }

    fun setCategory(view: View) {
        val dialog = CategoryPickerDialog(this@EventCreationActivity)
        dialog.setOnItemClickListener { _, _, position, _ ->
            viewModel.category = Category.values()[position]
            onCategorySet()
            dialog.hide()
        }

        dialog.show()
    }

    private fun onCategorySet() {
        //TODO: Set Toolbar colour to category colour and animate this
        val category = viewModel.category
        val color = ContextCompat.getColor(this@EventCreationActivity, category!!.color)
        val sbColor = ContextCompat.getColor(this@EventCreationActivity, category.statusBarColor)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(color))
        setStatusBarColor(sbColor)
        newEventCategoryView.setText(category.getName())
    }

    private fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
        }
    }

    fun setDate(view: View) {
        val calendar = viewModel.calendar
        val datePickerDialog = DatePickerDialog.newInstance(
            this@EventCreationActivity, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.minDate = Calendar.getInstance()
        datePickerDialog.show(fragmentManager, "Date picker")
    }

    fun setTime(view: View) {
        val calendar = viewModel.calendar
        val is24HourMode = DateFormat.is24HourFormat(this@EventCreationActivity)
        val timePickerDialog = TimePickerDialog.newInstance(
            this@EventCreationActivity,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            is24HourMode
        )
        timePickerDialog.show(fragmentManager, "Time picker")
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_save) {
            saveEvent()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveEvent() {
        if (!isValidEvent()) return
        val name = newEventTitleView.text.toString().trim { it <= ' ' }
        val description = newEventDescriptionView.text.toString().trim { it <= ' ' }
        val event =
            Event(null, name, description, viewModel.calendar.timeInMillis, viewModel.category!!)
        if (editId != -1L) {
            // We are editing an existing event
            event.id = editId
            viewModel.addEvent(event)
            val i = intent
            i.putExtra("event", event)
            setResult(Activity.RESULT_OK, i)
            finish()
            return
        }
        viewModel.addEvent(event)
        onBackPressed()
    }

    private fun isValidEvent(): Boolean {
        var errorRes = -1
        when {
            newEventTitleView.text.toString().isBlank() -> errorRes = R.string.no_title_set
            viewModel.isDateInPast() -> errorRes = R.string.no_date_set
            viewModel.category == null -> errorRes = R.string.no_category_set
        }
        if (errorRes != -1) {
            Snackbar.make(
                newEventCoordinatorView,
                errorRes,
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        viewModel.setDate(year, monthOfYear, dayOfMonth)
        setDateText()

    }

    override fun onTimeSet(view: RadialPickerLayout, hourOfDay: Int, minute: Int, second: Int) {
        viewModel.setTime(hourOfDay, minute)
        setTimeText()
    }

    private fun setDateText() {
        val dateText = DateUtils.formatDateTime(
            this@EventCreationActivity,
            viewModel.calendar.timeInMillis,
            DateUtils.FORMAT_SHOW_YEAR
        )
        newEventDateView.text = dateText
    }

    private fun setTimeText() {
        val timeText = DateUtils.formatDateTime(
            this@EventCreationActivity,
            viewModel.calendar.timeInMillis,
            DateUtils.FORMAT_SHOW_TIME
        )
        newEventTimeView.text = timeText

    }
}
