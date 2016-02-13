package com.oxapps.materialcountdown;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private boolean daysEnabled = true;
    private static final String TAG = "EventDetailActivity";
    CountDownTimer timer;
    public final static String ACTION_EDIT = "edit";
    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_event_detail);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        mEvent = getIntent().getParcelableExtra("event");
        initViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new CountDownTimer(mEvent.getRemainingTime(), 1000) {

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
        Category category = mEvent.getCategory();
        int color = ContextCompat.getColor(EventDetailActivity.this, category.getColor());
        int darkColor = ContextCompat.getColor(EventDetailActivity.this, category.getStatusBarColor());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        setStatusBarColor(darkColor);
        mTitleView.setText(mEvent.getName());
        mDescView.setText(mEvent.getDescription());
        int id = mEvent.getId();
        Log.d(TAG, "initViews: " + id);
        setTimeTexts();
    }

    @OnClick(R.id.fab_event_detail_edit)
    public void editEvent() {
        Intent intent = new Intent(this, NewEventActivity.class);
        intent.setAction(ACTION_EDIT);
        intent.putExtra("event", mEvent);
        startActivityForResult(intent, 123);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mEvent = data.getParcelableExtra("event");
            initViews();
        }
    }

    private void setTimeTexts() {
        long remaining = mEvent.getRemainingTime();
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
