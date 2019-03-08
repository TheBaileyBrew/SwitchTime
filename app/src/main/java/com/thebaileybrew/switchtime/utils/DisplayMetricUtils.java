package com.thebaileybrew.switchtime.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DisplayMetricUtils {

    public static int displayToPixel(Context context, float dp) {
        final int scale = (int)context.getResources().getDisplayMetrics().density;
        return (int)((int) dp * scale + 0.5f);
    }

    public static float screenToPixel(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

}
