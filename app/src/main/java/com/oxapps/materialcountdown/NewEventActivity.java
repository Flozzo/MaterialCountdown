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

package com.oxapps.materialcountdown;

import android.app.Activity;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.oxapps.materialcountdown.db.EventDbHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    @BindView(R.id.tv_new_event_title)
    EditText mTitleView;
    @BindView(R.id.tv_new_event_desc)
    EditText mDescriptionView;
    @BindView(R.id.tv_set_category)
    TextView mSetCategoryView;
    @BindView(R.id.tv_set_date)
    TextView mSetDateView;
    @BindView(R.id.tv_set_time)
    TextView mSetTimeView;
    @BindView(R.id.toolbar_new_event)
    Toolbar mToolbar;
    int editId = -1;


    private Category mCategory;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getAction() != null && getIntent().getAction().equals(EventDetailActivity.ACTION_EDIT)) {
            initForEdit();
        } else {
            initTime();
        }
    }

    private void initForEdit() {
        Event event = getIntent().getParcelableExtra("event");
        mCalendar = new GregorianCalendar();
        mCalendar.setTimeInMillis(event.getEndTime());
        mTitleView.setText(event.getName());
        mDescriptionView.setText(event.getDescription());
        mCategory = event.getCategory();
        setDateText();
        setTimeText();
        onCategorySet();
        editId = event.getId();
    }


    private void initTime() {
        mCalendar = new GregorianCalendar();
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_event, menu);
        return true;
    }

    @OnClick(R.id.new_event_category)
    public void setCategory() {
        final CategoryPickerDialog dialog = new CategoryPickerDialog(NewEventActivity.this);
        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCategory = Category.values()[position];
                onCategorySet();
                dialog.hide();
            }
        });

        dialog.show();
    }

    private void onCategorySet() {
        //TODO: Set Toolbar colour to category colour and animate this
        int color = ContextCompat.getColor(NewEventActivity.this, mCategory.getColor());
        int sbColor = ContextCompat.getColor(NewEventActivity.this, mCategory.getStatusBarColor());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        setStatusBarColor(sbColor);
        mSetCategoryView.setText(mCategory.getName());
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    @OnClick(R.id.new_event_date)
    public void setDate() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(NewEventActivity.this, mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setMinDate(Calendar.getInstance());
        datePickerDialog.show(getFragmentManager(), "Date picker");
    }

    @OnClick(R.id.new_event_time)
    public void setTime() {
        boolean is24HourMode = DateFormat.is24HourFormat(NewEventActivity.this);
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(NewEventActivity.this, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), is24HourMode);
        timePickerDialog.show(getFragmentManager(), "Time picker");
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            int errorRes;
            if (isEmpty(mTitleView)) {
                errorRes = R.string.no_title_set;
            } else if (!mCalendar.after(Calendar.getInstance())) {
                errorRes = R.string.no_date_set;
            } else if(mCategory == null) {
                errorRes = R.string.no_category_set;
            } else {
                String name = mTitleView.getText().toString().trim();
                String description = isEmpty(mDescriptionView) ? getString(R.string.no_description) : mDescriptionView.getText().toString().trim();
                Event event = new Event(name, description, mCalendar.getTimeInMillis(), mCategory);
                EventDbHelper helper = new EventDbHelper(NewEventActivity.this);
                if (editId != -1) {
                    event.setId(editId);
                    helper.editEvent(event);
                    Intent i = getIntent();
                    i.putExtra("event", event);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                    return true;
                }
                helper.addEvent(event);
                onBackPressed();
                return true;
            }
            Snackbar.make(findViewById(R.id.coordinator_new_event), errorRes, Snackbar.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(year, monthOfYear, dayOfMonth);
        setDateText();

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        setTimeText();
    }

    private void setDateText() {
        String dateText = DateUtils.formatDateTime(NewEventActivity.this, mCalendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_YEAR);
        mSetDateView.setText(dateText);
        Log.v("Set date", String.valueOf(mCalendar.getTimeInMillis()));
    }

    private void setTimeText() {
        String timeText = DateUtils.formatDateTime(NewEventActivity.this, mCalendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);
        mSetTimeView.setText(timeText);
        Log.v("Set time", String.valueOf(mCalendar.getTimeInMillis()));

    }
}
