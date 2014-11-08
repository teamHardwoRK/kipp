package com.teamhardwork.kipp.utilities;

import android.os.Handler;
import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.teamhardwork.kipp.R;

public class ReboundAnimator {

    public static void startClickAnimation(final View view) {
        boolean isReboundEnabled = view.getTag(R.string.is_rebound_enabled_tag_key) != null;
        if (!isReboundEnabled) {
            final Spring spring = SpringSystem.create().createSpring();
            spring.addListener(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(
                            spring.getCurrentValue(), 0, 1, 1, 0.5);
                    view.setScaleX(mappedValue);
                    view.setScaleY(mappedValue);
                }
            });
            view.setTag(R.string.is_rebound_enabled_tag_key, spring);
        }
        final Spring spring = (Spring) view.getTag(R.string.is_rebound_enabled_tag_key);
        spring.setEndValue(1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                spring.setEndValue(0);
            }
        }, 50);
    }
}
