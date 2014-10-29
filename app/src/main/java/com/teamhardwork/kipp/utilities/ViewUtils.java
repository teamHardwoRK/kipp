package com.teamhardwork.kipp.utilities;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamhardwork.kipp.KippApplication;

public class ViewUtils {
    public static TextView tabTextView(Context context, String text) {
        Typeface typeface = KippApplication.getDefaultTypeFace(context);
        TextView textView = new TextView(context);
        textView.setTypeface(typeface, Typeface.BOLD);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
}
