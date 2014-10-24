package com.teamhardwork.kipp.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.BehaviorPagerAdapter;
import com.viewpagerindicator.TitlePageIndicator;

public class BehaviorPagerFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "isPositive";

    private boolean isPositive;

    private BehaviorPagerAdapter behaviorPagerAdapter;
    private ViewPager vpPager;
    private TitlePageIndicator titleIndicator;

    public BehaviorPagerFragment() {
        // Required empty public constructor
    }

    public static BehaviorPagerFragment newInstance(boolean isPositive) {
        BehaviorPagerFragment fragment = new BehaviorPagerFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, isPositive);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isPositive = true;

        if (getArguments() != null) {
            this.isPositive = getArguments().getBoolean(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("ERROR", "BehaviorPagerFragment onCreateView() called");

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_behavior_pager, container, false);

        vpPager = (ViewPager) v.findViewById(R.id.vpPager);
        titleIndicator = (TitlePageIndicator) v.findViewById(R.id.titleIndicator);
        behaviorPagerAdapter = new BehaviorPagerAdapter(this, getChildFragmentManager(), isPositive);
        vpPager.setAdapter(behaviorPagerAdapter);
        titleIndicator.setViewPager(vpPager);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isPositive == true) {
            vpPager.setCurrentItem(0);
        } else {
            vpPager.setCurrentItem(1);
        }
    }
}
