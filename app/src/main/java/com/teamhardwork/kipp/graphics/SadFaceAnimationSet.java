package com.teamhardwork.kipp.graphics;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.teamhardwork.kipp.utilities.GraphicsUtils;

public class SadFaceAnimationSet extends AnimationSet implements Animation.AnimationListener {
    static final int BUFFER_OFFSET = GraphicsUtils.dpToPx(200);
    Context context;
    SadFaceAnimationSetListener listener;
    View view;


    public SadFaceAnimationSet(Context context, SadFaceAnimationSetListener listener, View view) {
        super(context, null);
        this.context = context;
        this.view = view;
        this.listener = listener;

    }

    public void setListener() {
        setAnimationListener(this);
    }

    public void setTranslateAnimation() {
        Point screenSize = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(screenSize);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, screenSize.y + BUFFER_OFFSET);
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

    public interface SadFaceAnimationSetListener {
        public void onAnimationEnd();
    }
}

