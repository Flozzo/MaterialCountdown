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

package com.oxapps.materialcountdown.detail;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.oxapps.materialcountdown.R;
import com.oxapps.materialcountdown.creation.EventCreationActivity;
import com.oxapps.materialcountdown.db.Event;
import com.oxapps.materialcountdown.model.Category;

import java.util.concurrent.TimeUnit;

public class EventDetailActivity extends AppCompatActivity {
    TextView eventDetailTitle;
    TextView eventDetailDescription;
    TextView eventDetailDays;
    TextView eventDetailHours;
    TextView eventDetailMinutes;
    TextView eventDetailSeconds;

    private boolean daysEnabled = true;
    CountDownTimer timer;
    public final static String ACTION_EDIT = "edit";
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_event_detail);
        setSupportActionBar(toolbar);
        eventDetailTitle = findViewById(R.id.eventDetailTitle);
        eventDetailDescription = findViewById(R.id.eventDetailDescription);
        eventDetailDays = findViewById(R.id.eventDetailDays);
        eventDetailHours = findViewById(R.id.eventDetailHours);
        eventDetailMinutes = findViewById(R.id.eventDetailMinutes);
        eventDetailSeconds = findViewById(R.id.eventDetailSeconds);
        event = getIntent().getParcelableExtra("event");
        initViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new CountDownTimer(event.getRemainingTime(), 1000) {

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
        Category category = event.getCategory();
        int color = ContextCompat.getColor(EventDetailActivity.this, category.getColor());
        int darkColor = ContextCompat.getColor(EventDetailActivity.this, category.getStatusBarColor());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        setStatusBarColor(darkColor);
        eventDetailTitle.setText(event.getName());
        eventDetailDescription.setText(event.getDescription());
        long id = event.getId();
        setTimeTexts();
    }

    public void editEvent(View view) {
        Intent intent = new Intent(this, EventCreationActivity.class);
        intent.setAction(ACTION_EDIT);
        intent.putExtra("event", event);
        startActivityForResult(intent, 123);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            event = data.getParcelableExtra("event");
            initViews();
        }
    }

    private void setTimeTexts() {
        long remaining = event.getRemainingTime();
        long days = TimeUnit.MILLISECONDS.toDays(remaining);
        remaining = remaining - TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(remaining);
        remaining = remaining - TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining);
        remaining = remaining - TimeUnit.MINUTES.toMillis(minutes);
        long seconds = remaining / 1000;

        if (daysEnabled && days > 0) {
            eventDetailDays.setText(days + " " + getDayString(days));
        } else {
            eventDetailDays.setVisibility(View.GONE);
            daysEnabled = false;
        }
        String minuteString = minutes < 10L ? "0" + minutes : String.valueOf(minutes);
        String secondString = seconds < 10L ? "0" + seconds : String.valueOf(seconds);
        eventDetailHours.setText(hours + "");
        eventDetailMinutes.setText(minuteString);
        eventDetailSeconds.setText(secondString);

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
