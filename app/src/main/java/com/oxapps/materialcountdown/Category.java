package com.oxapps.materialcountdown;

/**
 * Created by flynn on 22/11/15.
 */
public enum Category {

    ACADEMIC(R.string.academic, R.color.material_brown_500, R.color.material_brown_700, R.drawable.ic_academic),
    BIRTHDAY(R.string.birthday, R.color.material_red_500, R.color.material_red_700, R.drawable.ic_birthday),
    /*CHILD(R.string.child, R.color.material_orange_500, R.drawable.)*/
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

    private Category(int name, int color, int statusBarColor, int icon) {
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
