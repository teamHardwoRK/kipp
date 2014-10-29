package com.teamhardwork.kipp.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.teamhardwork.kipp.R;

import java.util.ArrayList;
import java.util.List;

public class GraphicsUtils {
    public static int dpToPx(int dp) {
        return Math.round(dp * (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(int px) {
        return Math.round(px / (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static List<Integer> colorList(Context context) {
        List<Integer> colorList = new ArrayList<Integer>();

        Resources resources = context.getResources();
        colorList.add(resources.getColor(R.color.DodgerBlue));
        colorList.add(resources.getColor(R.color.ForestGreen));
        colorList.add(resources.getColor(R.color.Beer));
        colorList.add(resources.getColor(R.color.PumpkinOrange));
        colorList.add(resources.getColor(R.color.PaleVioletRed));
        colorList.add(resources.getColor(R.color.MediumVioletRed));

        return colorList;
    }
}
