package com.oxapps.materialcountdown;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

/**
 * Created by flynn on 28/11/15.
 */
public class CircularIconImageView extends ImageView {

    public CircularIconImageView(Context context) {
        super(context);
    }

    public CircularIconImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularIconImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setBackgroundColor(int color) {
        GradientDrawable d = (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.circle_drawable);
        d.setColor(color);
        super.setBackground(d);
    }

    @Override
    public void setBackground(Drawable background) {
        ColorDrawable cd = (ColorDrawable) background;
        GradientDrawable d = (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.circle_drawable);
        d.setColor(cd.getColor());
        super.setBackground(d);
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
