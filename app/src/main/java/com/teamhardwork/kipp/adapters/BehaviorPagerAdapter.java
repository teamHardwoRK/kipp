package com.teamhardwork.kipp.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.teamhardwork.kipp.fragments.BehaviorFragment;
import com.teamhardwork.kipp.fragments.BehaviorPagerFragment;

import java.util.ArrayList;

public class BehaviorPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private boolean isPositive;
    private BehaviorPagerFragment pagerFragment;
    private FragmentManager fm;
    private ArrayList<Fragment> pagerFragments = new ArrayList<Fragment>();

    public BehaviorPagerAdapter(BehaviorPagerFragment pagerFragment, FragmentManager fragmentManager, boolean isPositive) {
        super(fragmentManager);
        this.pagerFragment = pagerFragment;
        this.fm = fragmentManager;
        this.isPositive = isPositive;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    public void clearAll() {
        // Clear all pages
        for (int i = 0; i < pagerFragments.size(); i++) {
            fm.beginTransaction().remove(pagerFragments.get(i)).commit();
        }
        pagerFragments.clear();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0: // page # 0 - This will show positive list of behaviors
                fragment = (Fragment) BehaviorFragment.newInstance(true);
                pagerFragments.add(fragment);
                return fragment;
            case 1: // page # 1 - This will show negative list of behaviors
                fragment = (Fragment) BehaviorFragment.newInstance(false);
                pagerFragments.add(fragment);
                return fragment;
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Positive";
            case 1:
                return "Negative";
            default:
                return "New Page";
        }
    }
}
