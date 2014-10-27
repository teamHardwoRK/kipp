package com.teamhardwork.kipp.utilities;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class GraphicsUtils {
    public static int dpToPx(int dp) {
        return Math.round(dp * (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(int px) {
        return Math.round(px / (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
