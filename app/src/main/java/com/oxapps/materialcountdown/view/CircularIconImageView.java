/*
 * Copyright 2018 Flynn van Os
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

package com.oxapps.materialcountdown.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.oxapps.materialcountdown.R;

/**
 * Created by flynn on 28/11/15.
 */
public class CircularIconImageView extends android.support.v7.widget.AppCompatImageView {

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
