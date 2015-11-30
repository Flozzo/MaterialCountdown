package com.oxapps.materialcountdown;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.oxapps.materialcountdown.db.EventDbHelper;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SwipeActionAdapter.SwipeActionListener, AdapterView.OnItemClickListener {
    @Bind(R.id.main_list)
    ListView mListView;
    SwipeActionAdapter mAdapter;
    private static final String TAG = "MainActivity";
    private ArrayList<Map<String, String>> mEventsList;
    PopulateListTask listTask;


    @Override
    protected void onResume() {
        super.onResume();
        if (listTask == null || !listTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            listTask = new PopulateListTask();
            listTask.execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = ButterKnife.findById(MainActivity.this, R.id.toolbar_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mListView.setOnItemClickListener(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onFloatingActionClicked(View v) {
        Intent i = new Intent(this, NewEventActivity.class);
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
        final Map<String, String> data = (Map<String, String>) mAdapter.getItem(position[0]);
        mEventsList.remove(position[0]);
        mAdapter.notifyDataSetChanged();
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_main), R.string.item_removed, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventsList.add(position[0], data);
                mAdapter.notifyDataSetChanged();
            }
        });
        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                if (event != DISMISS_EVENT_ACTION) {
                    new EventDbHelper(MainActivity.this).removeEvent(data.get(EventDbHelper.KEY_ID));
                }
            }
        });
        snackbar.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(MainActivity.this, EventDetailActivity.class);
        Map<String, String> map = mEventsList.get(position);
        int eventId = Integer.parseInt(map.get(EventDbHelper.KEY_ID));
        int category = Integer.parseInt(map.get(EventDbHelper.KEY_CATEGORY));
        String title = map.get(EventDbHelper.KEY_NAME);
        String desc = map.get(EventDbHelper.KEY_DESC);
        String days = map.get(EventDbHelper.DAY);
        i.putExtra(EventDbHelper.KEY_CATEGORY, category);
        i.putExtra("eventId", eventId);
        Log.d(TAG, "onItemClick: " + eventId);
        i.putExtra(EventDbHelper.KEY_NAME, title);
        i.putExtra(EventDbHelper.KEY_DESC, desc);
        i.putExtra(EventDbHelper.DAY, days);
        startActivity(i);
    }

    class PopulateListTask extends AsyncTask<Void, Void, ArrayList<Map<String, String>>> {

        @Override
        protected ArrayList<Map<String, String>> doInBackground(Void... params) {
            EventDbHelper helper = new EventDbHelper(MainActivity.this);
            ArrayList<Map<String, String>> events = helper.getEventsForMainList();
            return events;
        }

        @Override
        protected void onPostExecute(ArrayList<Map<String, String>> events) {
            super.onPostExecute(events);
            MainActivity.this.mEventsList = events;
            String[] from = new String[]{EventDbHelper.KEY_NAME, EventDbHelper.KEY_DESC, EventDbHelper.KEY_END, EventDbHelper.KEY_CATEGORY, EventDbHelper.DAY};
            int[] to = new int[]{R.id.event_name, R.id.event_desc, R.id.event_days, R.id.iv_event_logo, R.id.event_days_remaining};
            SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, events, R.layout.event_item, from, to);
            final Category[] categories = Category.values();

            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if (view instanceof CircularIconImageView) {
                        Category c = categories[Integer.valueOf((String) data)];
                        ImageView iv = ((ImageView) view);
                        iv.setImageResource(c.getIcon());
                        iv.setBackgroundColor(ContextCompat.getColor(MainActivity.this, c.getColor()));
                        return true;
                    } else
                        return false;
                }
            });

            mAdapter = new SwipeActionAdapter(adapter);
            mAdapter.setListView(mListView);
            mAdapter.setFixedBackgrounds(true);
            mListView.setAdapter(mAdapter);
            mAdapter.setSwipeActionListener(MainActivity.this);
            mAdapter.addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT, R.layout.swipe_bg_main_left)
                    .addBackground(SwipeDirection.DIRECTION_NORMAL_RIGHT, R.layout.swipe_bg_main_right);
        }
    }
}
