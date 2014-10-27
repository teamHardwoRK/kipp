package com.teamhardwork.kipp.graphics;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.teamhardwork.kipp.utilities.GraphicsUtils;

import java.util.Random;

public class StarAnimationSet extends AnimationSet implements Animation.AnimationListener {
    static final int BUFFER_IN_PX = GraphicsUtils.dpToPx(500);
    Context context;
    StarAnimationSetListener listener;
    View view;

    public StarAnimationSet(Context context, StarAnimationSetListener listener, View view) {
        super(context, null);
        this.context = context;
        this.listener = listener;
        this.view = view;
        setAnimationListener(this);
    }

    public void setScaleAnimation(float scale) {
        ScaleAnimation animation = new ScaleAnimation(0, scale, 0, scale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(getDuration());
        addAnimation(animation);
    }

    public void setTranslateAnimation(int xDirection, int yDirection) {
        int[] location = new int[2];
        view.getLocationInWindow(location);

        Point screenSize = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(screenSize);

        int[] nonZeroDirections = {-1, 1};

        if (xDirection == 0 && yDirection == 0) {
            xDirection = nonZeroDirections[new Random().nextInt(nonZeroDirections.length)];
            yDirection = nonZeroDirections[new Random().nextInt(nonZeroDirections.length)];
        }

        float xDelta = 0;
        float yDelta = 0;

        switch (xDirection) {
            case -1:
                xDelta = 0 - BUFFER_IN_PX;
                break;
            case 0:
                xDelta = 0;
                break;
            case 1:
                xDelta = screenSize.x + BUFFER_IN_PX;
                break;
        }

        switch (yDirection) {
            case -1:
                yDelta = 0 - BUFFER_IN_PX;
                break;
            case 0:
                yDelta = 0;
                break;
            case 1:
                yDelta = screenSize.y + BUFFER_IN_PX;
                break;
        }

        float multiplier = 1 + (new Random().nextInt(100)) / 100.0f;
        xDelta = xDelta * multiplier;
        yDelta = yDelta * multiplier;

        TranslateAnimation animation = new TranslateAnimation(0, xDelta, 0, yDelta);
        animation.setDuration(getDuration());
        addAnimation(animation);
    }

    public void setRotationAnimation(float angle) {
        RotateAnimation animation = new RotateAnimation(0, angle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(getDuration());
        addAnimation(animation);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        view.setVisibility(View.GONE);
        listener.onAnimationEnd();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public interface StarAnimationSetListener {
        public void onAnimationEnd();
    }
}
