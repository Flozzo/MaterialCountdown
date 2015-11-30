package com.oxapps.materialcountdown;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.oxapps.materialcountdown.db.EventDbHelper;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EventDetailActivity extends AppCompatActivity {
    @Bind(R.id.tv_event_detail_title)
    TextView mTitleView;
    @Bind(R.id.tv_event_detail_desc)
    TextView mDescView;
    @Bind(R.id.event_detail_days)
    TextView mDaysView;
    @Bind(R.id.event_detail_hours)
    TextView mHoursView;
    @Bind(R.id.event_detail_minutes)
    TextView mMinutesView;
    @Bind(R.id.event_detail_seconds)
    TextView mSecondsView;

    private long endTime;
    private boolean daysEnabled = true;
    private static final String TAG = "EventDetailActivity";
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_event_detail);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_event_detail_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();


    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new CountDownTimer(endTime - System.currentTimeMillis(), 1000) {

            public void onTick(long millisUntilFinished) {
                setTimeTexts();
            }

            public void onFinish() {

            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        timer = null;
    }

    private void initViews() {
        Intent source = getIntent();
        int cat = source.getIntExtra(EventDbHelper.KEY_CATEGORY, -1);
        Category category = Category.values()[cat];
        int color = ContextCompat.getColor(EventDetailActivity.this, category.getColor());
        int darkColor = ContextCompat.getColor(EventDetailActivity.this, category.getStatusBarColor());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        setStatusBarColor(darkColor);
        mTitleView.setText(source.getStringExtra(EventDbHelper.KEY_NAME));
        mDescView.setText(source.getStringExtra(EventDbHelper.KEY_DESC));
        mDaysView.setText(source.getStringExtra(EventDbHelper.DAY));
        int id = source.getIntExtra("eventId", -1);
        Log.d(TAG, "initViews: " + id);
        endTime = new EventDbHelper(EventDetailActivity.this).getEndTime(id);
        setTimeTexts();
    }

    private void setTimeTexts() {
        long remaining = endTime - System.currentTimeMillis();
        long days = TimeUnit.MILLISECONDS.toDays(remaining);
        remaining = remaining - TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(remaining);
        remaining = remaining - TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining);
        remaining = remaining - TimeUnit.MINUTES.toMillis(minutes);
        long seconds = remaining / 1000;

        if (daysEnabled && days > 0) {
            mDaysView.setText(days + " " + getDayString(days));
        } else {
            mDaysView.setVisibility(View.GONE);
            daysEnabled = false;
        }
        String minuteString = minutes < 10L ? "0" + minutes : String.valueOf(minutes);
        String secondString = seconds < 10L ? "0" + seconds : String.valueOf(seconds);
        mHoursView.setText(hours + "");
        mMinutesView.setText(minuteString);
        mSecondsView.setText(secondString);

    }

    private String getDayString(long days) {
        int dayRes = days == 1 ? R.string.day_remaining : R.string.days_remaining;
        return getString(dayRes);
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }
}
