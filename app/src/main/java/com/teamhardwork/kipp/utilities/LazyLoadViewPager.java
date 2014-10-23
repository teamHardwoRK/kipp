package com.teamhardwork.kipp.utilities;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.viewpagerindicator.TitlePageIndicator;

public class LazyLoadViewPager extends ViewPager {
    PagerAdapter mPagerAdapter;
    TitlePageIndicator mPageIndicator;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPagerAdapter != null) {
            super.setAdapter(mPagerAdapter);
            mPageIndicator.setViewPager(this);
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
    }

    public void storeAdapter(PagerAdapter pagerAdapter, TitlePageIndicator pageIndicator) {
        mPagerAdapter = pagerAdapter;
        mPageIndicator = pageIndicator;
    }

    public LazyLoadViewPager(Context context) {
        super(context);
    }

    public LazyLoadViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}