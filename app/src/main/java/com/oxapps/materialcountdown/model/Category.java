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

package com.oxapps.materialcountdown.model;

import com.oxapps.materialcountdown.R;

public enum Category {

    ACADEMIC(R.string.academic, R.color.material_brown_500, R.color.material_brown_700, R.drawable.ic_academic),
    BIRTHDAY(R.string.birthday, R.color.material_red_500, R.color.material_red_700, R.drawable.ic_birthday),
    ENTERTAINMENT(R.string.entertainment, R.color.material_blue_500, R.color.material_blue_700, R.drawable.ic_movies),
    OTHER(R.string.other, R.color.material_green_500, R.color.material_green_700, R.drawable.ic_other),
    PRODUCT_RELEASE(R.string.product_release, R.color.material_yellow_500, R.color.material_yellow_700, R.drawable.ic_new_releases),
    SPORT(R.string.sport, R.color.material_pink_500, R.color.material_pink_700, R.drawable.ic_sport),
    TRAVEL(R.string.travel, R.color.material_purple_500, R.color.material_purple_700, R.drawable.ic_travel),
    WORK(R.string.work, R.color.material_blue_grey_500, R.color.material_blue_grey_700, R.drawable.ic_work);


    private final int name;
    private final int color;


    private int statusBarColor;
    private final int icon;

    Category(int name, int color, int statusBarColor, int icon) {
        this.name = name;
        this.color = color;
        this.statusBarColor = statusBarColor;
        this.icon = icon;
    }

    public int getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public int getColor() {
        return color;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

}
