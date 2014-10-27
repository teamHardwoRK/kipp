package com.teamhardwork.kipp.utilities;

import android.content.Context;

/**
 * Created by rcarino on 10/26/14.
 */
public class DimensionConverter {
    public static float pxFromDp(float dp, Context context) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
