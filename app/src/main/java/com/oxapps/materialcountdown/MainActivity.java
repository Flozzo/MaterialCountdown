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

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

public class MainActivity extends AppCompatActivity implements SwipeActionAdapter.SwipeActionListener, AdapterView.OnItemClickListener {
    MainViewModel viewModel;

    ListView mainList;
    RelativeLayout emptyStateView;
    SwipeActionAdapter swipeAdapter;
    MainAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainList = findViewById(R.id.main_list);
        emptyStateView = findViewById(R.id.main_empty_view);
        mainList.setOnItemClickListener(MainActivity.this);
        mainAdapter = new MainAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getEvents().observe(this, events -> {
            if (events == null || events.isEmpty()) {
                showEmptyMessage();
                return;
            }
            hideEmptyMessage();
            mainAdapter.setItems(events);

            swipeAdapter = new SwipeActionAdapter(mainAdapter);
            swipeAdapter.setListView(mainList);
            swipeAdapter.setFixedBackgrounds(true);
            mainList.setAdapter(swipeAdapter);
            swipeAdapter.setSwipeActionListener(MainActivity.this);
            swipeAdapter.addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT, R.layout.swipe_bg_main_left)
                    .addBackground(SwipeDirection.DIRECTION_NORMAL_RIGHT, R.layout.swipe_bg_main_right);
        });
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
        final Event event = mainAdapter.getItem(position[0]);
        mainAdapter.removeItem(position[0]);
        if (mainAdapter.isEmpty()) {
            showEmptyMessage();
        }
        swipeAdapter.notifyDataSetChanged();
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_main), R.string.item_removed, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undo, v -> {
            mainAdapter.addItem(position[0], event);
            swipeAdapter.notifyDataSetChanged();
            hideEmptyMessage();
        });
        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int swipeEvent) {
                super.onDismissed(snackbar, swipeEvent);
                if (swipeEvent != DISMISS_EVENT_ACTION) {
                    viewModel.removeEvent(event);
                }

            }
        });
        snackbar.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, EventDetailActivity.class);
        Event event = mainAdapter.getItem(position);
        i.putExtra("event", event);
        startActivity(i);
    }

    private void hideEmptyMessage() {
        mainList.setVisibility(View.VISIBLE);
        emptyStateView.setVisibility(View.GONE);
    }

    private void showEmptyMessage() {
        mainList.setVisibility(View.GONE);
        emptyStateView.setVisibility(View.VISIBLE);
    }
}
