package com.teamhardwork.kipp.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

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
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_behavior_pager, container, false);

        vpPager = (ViewPager) v.findViewById(R.id.vpPager);
        titleIndicator = (TitlePageIndicator) v.findViewById(R.id.titleIndicator);
        behaviorPagerAdapter = new BehaviorPagerAdapter(this, getChildFragmentManager(), isPositive);
        vpPager.setAdapter(behaviorPagerAdapter);
        titleIndicator.setViewPager(vpPager);
        setTitleIndicatorListener();

        return v;
    }

    void setTitleIndicatorListener() {
        titleIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                boolean isPositive = i == 0;
                setTitleIndicatorTheme(isPositive);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    void setTitleIndicatorTheme(boolean isPositive) {
        Resources resources = getActivity().getResources();
        int redColor = resources.getColor(R.color.PaleVioletRed);
        int greenColor = resources.getColor(R.color.AlgaeGreen);

        if(isPositive) {
            titleIndicator.setSelectedColor(greenColor);
            titleIndicator.setFooterColor(greenColor);
        }
        else {
            titleIndicator.setSelectedColor(redColor);
            titleIndicator.setFooterColor(redColor);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        if(dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
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
