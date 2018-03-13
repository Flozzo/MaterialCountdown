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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.oxapps.materialcountdown.creation.EventCreationActivity;
import com.oxapps.materialcountdown.db.Event;
import com.oxapps.materialcountdown.db.EventDatabase;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeActionAdapter.SwipeActionListener, AdapterView.OnItemClickListener {
    ListView mListView;
    RelativeLayout mEmptyStateView;
    SwipeActionAdapter mSwipeAdapter;
    MainAdapter mMainAdapter;
    private List<Event> mEventsList;
    PopulateListTask listTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        mListView = findViewById(R.id.main_list);
        mEmptyStateView = findViewById(R.id.main_empty_view);
        mListView.setOnItemClickListener(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listTask == null || !listTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            listTask = new PopulateListTask();
            listTask.execute();
        }
    }

    public void createNewEvent(View v) {
        Intent i = new Intent(this, EventCreationActivity.class);
        startActivity(i);
    }

    @Override
    public boolean hasActions(int position, SwipeDirection direction) {
        return direction.isLeft() || direction.isRight();
    }

    @Override
    public boolean shouldDismiss(int position, SwipeDirection direction) {
        return direction.isLeft() || direction.isRight();
    }

    @Override
    public void onSwipe(final int[] position, SwipeDirection[] direction) {
        final Event event = mMainAdapter.getItem(position[0]);
        mEventsList.remove(position[0]);
        if (mEventsList.isEmpty()) {
            showEmptyMessage();
        }
        mSwipeAdapter.notifyDataSetChanged();
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_main), R.string.item_removed, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventsList.add(position[0], event);
                mSwipeAdapter.notifyDataSetChanged();
                hideEmptyMessage();
            }
        });
        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int swipeEvent) {
                super.onDismissed(snackbar, swipeEvent);
                if (swipeEvent != DISMISS_EVENT_ACTION) {
                    EventDatabase eventDatabase = EventDatabase.Companion.getInstance(MainActivity.this);
                    eventDatabase.eventDao().delete(event);
                }

            }
        });
        snackbar.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, EventDetailActivity.class);
        Event event = mEventsList.get(position);
//        i.putExtra("event", event);
        startActivity(i);
    }

    class PopulateListTask extends AsyncTask<Void, Void, List<Event>> {

        @Override
        protected List<Event> doInBackground(Void... params) {
            EventDatabase eventDatabase = EventDatabase.Companion.getInstance(MainActivity.this);
            List<Event> events = eventDatabase.eventDao().getEvents();
            return events;
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);
            if (events.isEmpty()) {
                showEmptyMessage();
                return;
            }
            hideEmptyMessage();
            MainActivity.this.mEventsList = events;
            mMainAdapter = new MainAdapter(events, MainActivity.this);

            mSwipeAdapter = new SwipeActionAdapter(mMainAdapter);
            mSwipeAdapter.setListView(mListView);
            mSwipeAdapter.setFixedBackgrounds(true);
            mListView.setAdapter(mSwipeAdapter);
            mSwipeAdapter.setSwipeActionListener(MainActivity.this);
            mSwipeAdapter.addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT, R.layout.swipe_bg_main_left)
                    .addBackground(SwipeDirection.DIRECTION_NORMAL_RIGHT, R.layout.swipe_bg_main_right);
        }
    }

    private void hideEmptyMessage() {
        mListView.setVisibility(View.VISIBLE);
        mEmptyStateView.setVisibility(View.GONE);
    }

    private void showEmptyMessage() {
        mListView.setVisibility(View.GONE);
        mEmptyStateView.setVisibility(View.VISIBLE);
    }
}
