package com.oxapps.materialcountdown;

/**
 * Created by flynn on 22/11/15.
 */
public enum Category {

    /*...
    MASSACHUSETTS("Massachusetts",  "MA",   true),
    MICHIGAN     ("Michigan",       "MI",   false),
    ...; // all 50 of those

    <item>@string/academic</item>
        <item>@string/birthday</item>
        <item>@string/child</item>
        <item>@string/entertainment</item>
        <item>@string/other</item>
        <item>@string/product_release</item>
        <item>@string/relaxation</item>
        <item>@string/sport</item>
        <item>@string/travel</item>
        <item>@string/work</item>


    BIRTHDAY(R.string.birthday)
    */
    ACADEMIC(R.string.academic, R.color.material_brown_500, R.drawable.ic_academic),
    BIRTHDAY(R.string.birthday, R.color.material_red_500 , R.drawable.ic_birthday),
    /*CHILD(R.string.child, R.color.material_orange_500, R.drawable.)*/
    ENTERTAINMENT(R.string.entertainment, R.color.material_blue_500, R.drawable.ic_movies),
    OTHER(R.string.other, R.color.material_green_500, R.drawable.ic_other),
    PRODUCT_RELEASE(R.string.product_release, R.color.material_yellow_500, R.drawable.ic_new_releases),
    SPORT(R.string.sport, R.color.material_pink_500, R.drawable.ic_sport),
    TRAVEL(R.string.travel, R.color.material_purple_500, R.drawable.ic_travel),
    WORK(R.string.work, R.color.material_blue_grey_500, R.drawable.ic_work);


    private final int name, color, icon;

    private Category(int name, int color, int icon) {
        this.name = name;
        this.color = color;
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
}
