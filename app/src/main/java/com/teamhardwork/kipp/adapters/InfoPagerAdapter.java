package com.teamhardwork.kipp.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.teamhardwork.kipp.fragments.ActionLogFragment;
import com.teamhardwork.kipp.fragments.BaseKippFragment;
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.StudentStatsFragment;

public class InfoPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    private String currentStudentId;

    public InfoPagerAdapter(FragmentManager fragmentManager, String currentStudentId) {
        super(fragmentManager);
        this.currentStudentId = currentStudentId;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return StudentStatsFragment.newInstance(currentStudentId);
            case 1:
                return FeedFragment.newInstance(currentStudentId);
            case 2:
                return ActionLogFragment.newInstance(currentStudentId);
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        BaseKippFragment fragment = (BaseKippFragment) getItem(position);
        return fragment.getTitle();
    }
}
