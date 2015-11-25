package com.oxapps.materialcountdown;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    @Bind(R.id.new_event_category) RelativeLayout mCategoryView;
    @Bind(R.id.tv_set_category) TextView mSetCategoryView;
    @Bind(R.id.tv_set_date) TextView mSetDateView;
    @Bind(R.id.tv_set_time) TextView mSetTimeView;


    private Category mCategory;
    private Calendar mCalendar;
    private long mSetDate = 43200000L; //Init set time to be 0
    private long mSetTime = 43200000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar_new_event);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCalendar = new GregorianCalendar(0,0,0,0,0);
    }

    private void initTime() {
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
                //TODO: Set Toolbar colour to category colour and animate this
                dialog.hide();
                ((TextView) mCategoryView.findViewById(R.id.tv_set_category)).setText(mCategory.getName());
            }
        });

        dialog.show();
    }

    @OnClick(R.id.new_event_date)
    public void setDate() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(NewEventActivity.this, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show(getFragmentManager(), "Date picker");
    }

    @OnClick(R.id.new_event_time)
    public void setTime() {
        Calendar cal = Calendar.getInstance();
        boolean is24HourMode = DateFormat.is24HourFormat(NewEventActivity.this);
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(NewEventActivity.this, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), is24HourMode);
        timePickerDialog.show(getFragmentManager(), "Time picker");
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(year, monthOfYear, dayOfMonth);

        String dateText = DateUtils.formatDateTime(NewEventActivity.this, mCalendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_YEAR);
        mSetDateView.setText(dateText);
        Log.v("Set date", String.valueOf(mCalendar.getTimeInMillis()));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        String timeText = DateUtils.formatDateTime(NewEventActivity.this, mCalendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);
        mSetTimeView.setText(timeText);
        Log.v("Set time", String.valueOf(mCalendar.getTimeInMillis()));
    }
}
