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

package com.oxapps.materialcountdown.creation;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.oxapps.materialcountdown.EventDetailActivity;
import com.oxapps.materialcountdown.R;
import com.oxapps.materialcountdown.db.Event;
import com.oxapps.materialcountdown.model.Category;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class EventCreationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    EditText mTitleView;
    EditText mDescriptionView;
    TextView mSetCategoryView;
    TextView mSetDateView;
    TextView mSetTimeView;
    Toolbar mToolbar;
    long editId = -1;

    EventCreationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        viewModel = ViewModelProviders.of(this).get(EventCreationViewModel.class);

        mTitleView = findViewById(R.id.tv_new_event_title);
        mDescriptionView = findViewById(R.id.tv_new_event_desc);
        mSetCategoryView = findViewById(R.id.tv_set_category);
        mSetDateView = findViewById(R.id.tv_set_date);
        mSetTimeView = findViewById(R.id.tv_set_time);
        mToolbar = findViewById(R.id.toolbar_new_event);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getAction() != null && getIntent().getAction().equals(EventDetailActivity.ACTION_EDIT)) {
            initForEdit();
        }
    }

    private void initForEdit() {
        Event event = getIntent().getParcelableExtra("event");
        viewModel.setTimeInMillis(event.getEndTime());
        mTitleView.setText(event.getName());
        mDescriptionView.setText(event.getDescription());
        viewModel.setCategory(event.getCategory());
        setDateText();
        setTimeText();
        onCategorySet();
        editId = event.getId();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_event, menu);
        return true;
    }

    public void setCategory(View view) {
        final CategoryPickerDialog dialog = new CategoryPickerDialog(EventCreationActivity.this);
        dialog.setOnItemClickListener((parent, view1, position, id) -> {
            viewModel.setCategory(Category.values()[position]);
            onCategorySet();
            dialog.hide();
        });

        dialog.show();
    }

    private void onCategorySet() {
        //TODO: Set Toolbar colour to category colour and animate this
        Category category = viewModel.getCategory();
        int color = ContextCompat.getColor(EventCreationActivity.this, category.getColor());
        int sbColor = ContextCompat.getColor(EventCreationActivity.this, category.getStatusBarColor());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        setStatusBarColor(sbColor);
        mSetCategoryView.setText(category.getName());
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    public void setDate(View view) {
        Calendar calendar = viewModel.getCalendar();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(EventCreationActivity.this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setMinDate(Calendar.getInstance());
        datePickerDialog.show(getFragmentManager(), "Date picker");
    }

    public void setTime(View view) {
        Calendar calendar = viewModel.getCalendar();
        boolean is24HourMode = DateFormat.is24HourFormat(EventCreationActivity.this);
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(EventCreationActivity.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24HourMode);
        timePickerDialog.show(getFragmentManager(), "Time picker");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            saveEvent();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveEvent() {
        boolean valid = isValidEvent();
        if (!valid) return;
        String name = mTitleView.getText().toString().trim();
        String description = mDescriptionView.getText().toString().trim();
        Event event = new Event(null, name, description, viewModel.getCalendar().getTimeInMillis(), viewModel.getCategory());
        if (editId != -1) {
            // We are editing an existing event
            event.setId(editId);
            Intent i = getIntent();
//                    i.putExtra("event", event);
            setResult(Activity.RESULT_OK, i);
            finish();
            return;
        }
        viewModel.addEvent(event);
        onBackPressed();
    }

    private boolean isValidEvent() {
        int errorRes = -1;
        if (isEmpty(mTitleView)) {
            errorRes = R.string.no_title_set;
        } else if (viewModel.isDateInPast()) {
            errorRes = R.string.no_date_set;
        } else if (viewModel.getCategory() == null) {
            errorRes = R.string.no_category_set;
        }
        if (errorRes != -1) {
            Snackbar.make(findViewById(R.id.coordinator_new_event), errorRes, Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        viewModel.setDate(year, monthOfYear, dayOfMonth);
        setDateText();

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        viewModel.setTime(hourOfDay, minute);
        setTimeText();
    }

    private void setDateText() {
        String dateText = DateUtils.formatDateTime(EventCreationActivity.this, viewModel.getCalendar().getTimeInMillis(), DateUtils.FORMAT_SHOW_YEAR);
        mSetDateView.setText(dateText);
    }

    private void setTimeText() {
        String timeText = DateUtils.formatDateTime(EventCreationActivity.this, viewModel.getCalendar().getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);
        mSetTimeView.setText(timeText);

    }
}
