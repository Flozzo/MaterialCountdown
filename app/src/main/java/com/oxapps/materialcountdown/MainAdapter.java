package com.oxapps.materialcountdown;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainAdapter extends BaseAdapter {


    private ArrayList<Event> mDataset;
    private Context mContext;
    private static final String TAG = "MainAdapter";

    public MainAdapter(ArrayList<Event> events, Context context) {
        this.mDataset = events;
        this.mContext = context;
    }

    @Override public int getCount() {
        return mDataset.size();
    }

    @Override public Event getItem(int position) {
        return mDataset.get(position);
    }

    @Override public long getItemId(int position) {
        Event event = getItem(position);
        return event.getId();
    }

    static class ViewHolder {
        @Bind(R.id.event_name) TextView nameView;
        @Bind(R.id.event_desc) TextView descriptionView;
        @Bind(R.id.event_days) TextView daysCountView;
        @Bind(R.id.event_days_remaining) TextView daysTextView;
        @Bind(R.id.iv_event_logo) ImageView logoView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.event_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        Event event = mDataset.get(position);
        holder.nameView.setText(event.getName());
        holder.descriptionView.setText(event.getDescription());

        long remaining = event.getRemainingTime();
        int[] timeValues = TimeHelper.getRemainingShort(remaining);
        holder.daysCountView.setText(String.valueOf(timeValues[0]));
        holder.daysTextView.setText(timeValues[1]);
        Category c = event.getCategory();
        holder.logoView.setImageResource(c.getIcon());
        holder.logoView.setBackgroundColor(ContextCompat.getColor(mContext, c.getColor()));

        return view;
    }


}