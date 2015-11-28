package com.oxapps.materialcountdown;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.oxapps.materialcountdown.db.EventDbHelper;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.main_list)
    ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = ButterKnife.findById(MainActivity.this, R.id.toolbar_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        PopulateListTask task = new PopulateListTask();
        task.execute();
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
            String[] from = new String[]{EventDbHelper.KEY_NAME, EventDbHelper.KEY_DESC, EventDbHelper.KEY_END, EventDbHelper.KEY_CATEGORY};
            int[] to = new int[]{R.id.event_name, R.id.event_desc, R.id.event_days, R.id.iv_event_logo};
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
            mListView.setAdapter(adapter);
        }

        private Drawable colourCircleDrawable(int color) {
            GradientDrawable d = (GradientDrawable) getDrawable(R.drawable.circle_drawable);
            //noinspection ConstantConditions
            d.setColor(ContextCompat.getColor(MainActivity.this, color));
            return d;
        }
    }
}
