package com.teamhardwork.kipp.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.teamhardwork.kipp.fragments.BehaviorFragment;
import com.teamhardwork.kipp.fragments.BehaviorPagerFragment;

import java.util.ArrayList;

/**
 * Created by hugh_sd on 10/18/14.
 */
public class BehaviorPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private Context context;
    private FragmentManager fm;
    private ArrayList<String> studentIds;
    private String schoolClassId;
    private boolean isPositive;
    private BehaviorPagerFragment pagerFragment;

    public BehaviorPagerAdapter(Context context, FragmentManager fragmentManager, BehaviorPagerFragment pagerFragment, ArrayList<String> studentIds, String schoolClassId, boolean isPositive) {
        super(fragmentManager);
        this.context = context;
        this.fm = fragmentManager;
        this.pagerFragment = pagerFragment;
        this.studentIds = studentIds;
        this.schoolClassId = schoolClassId;
        this.isPositive = isPositive;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // page # 0 - This will show positive list of behaviors
                return (Fragment) BehaviorFragment.newInstance(pagerFragment, studentIds, schoolClassId, true);
            case 1: // page # 1 - This will show negative list of behaviors
                return (Fragment) BehaviorFragment.newInstance(pagerFragment, studentIds, schoolClassId, false);
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}
