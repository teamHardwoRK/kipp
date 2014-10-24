package com.teamhardwork.kipp.listeners;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.fortysevendeg.swipelistview.SwipeListView;

public class RosterTabListener implements ActionBar.TabListener {
    private Fragment mFragment;
    private final String mTag;
    private final int mfragmentContainerId;
    private final Bundle mfragmentArgs;
    private FragmentManager fm;
    private SwipeListView lv;
    private int jumpPos;

    public RosterTabListener(int fragmentContainerId, String tag, SwipeListView lv, int jumpPos) {
        mTag = tag;
        mfragmentContainerId = fragmentContainerId;
        mfragmentArgs = new Bundle();

        this.lv = lv;
        this.jumpPos = jumpPos;
    }

    /* The following are each of the ActionBar.TabListener callbacks */

    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        if (lv == null) return;
        lv.setSelection(jumpPos);
//        lv.post(new Runnable() {
//            @Override
//            public void run() {
//                lv.smoothScrollToPosition(jumpPos);
//            }
//        });
    }

    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
    }

    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }
}
