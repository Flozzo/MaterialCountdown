package com.oxapps.materialcountdown;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flynn on 25/11/15.
 */
public class CategoryPickerDialog extends AppCompatDialog {

    private AdapterView.OnItemClickListener listener;

    public CategoryPickerDialog(Context context) {
        super(context);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.set_category);


        ListView categoryList = (ListView) getLayoutInflater().inflate(R.layout.dialog_category_list, null);
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        String[] from = new String[]{"icon", "name"};
        int[] to = new int[]{R.id.iv_category_logo, R.id.category_name};
        for(Category category : Category.values()) {
            Map<String, Object> map = new HashMap<>();
            Drawable d = getGreyDrawable(category.getIcon());
            map.put("icon", d);
            map.put("name", getContext().getString(category.getName()));
            data.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getContext(), data, R.layout.category_item, from, to);
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView) {
                    ((ImageView) view).setImageDrawable((Drawable) data);
                    return true;
                }
                return false;
            }
        });
        categoryList.setDivider(null);
        categoryList.setAdapter(adapter);
        setContentView(categoryList);
        categoryList.setOnItemClickListener(listener);
    }

    private Drawable getGreyDrawable(int icon) {
        Drawable d = getContext().getResources().getDrawable(icon);
        d.setColorFilter(0x8A000000, PorterDuff.Mode.MULTIPLY);
        return d;
    }
}
