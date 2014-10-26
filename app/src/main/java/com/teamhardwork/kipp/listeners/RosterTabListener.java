package com.teamhardwork.kipp.listeners;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.fortysevendeg.swipelistview.SwipeListView;

public class RosterTabListener implements ActionBar.TabListener {
    private final String mTag;
    private final int mfragmentContainerId;
    private final Bundle mfragmentArgs;
    private Fragment mFragment;
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

    public static void smoothScrollToPositionFromTop(final AbsListView view, final int position) {
        View child = getChildAtPosition(view, position);
        // There's no need to scroll if child is already at top or view is already scrolled to its end
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
            return;
        }

        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.setOnScrollListener(null);

                    // Fix for scrolling bug
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(position);
                        }
                    });
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                                 final int totalItemCount) {
            }
        });

        // Perform scrolling to position
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.smoothScrollToPositionFromTop(position, 0);
            }
        });
    }

    public static View getChildAtPosition(final AdapterView view, final int position) {
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }

    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        if (lv == null) return;
//        lv.setSelection(jumpPos);
//        lv.post(new Runnable() {
//            @Override
//            public void run() {
//                lv.smoothScrollToPosition(jumpPos);
//            }
//        });
        smoothScrollToPositionFromTop(lv, jumpPos);
    }

    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
    }

    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }
}
