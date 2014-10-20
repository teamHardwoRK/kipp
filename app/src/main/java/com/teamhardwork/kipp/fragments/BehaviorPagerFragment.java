package com.teamhardwork.kipp.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.BehaviorPagerAdapter;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.ArrayList;

public class BehaviorPagerFragment extends Fragment {
    private static final String ARG_PARAM1 = "student_ids";
    private static final String ARG_PARAM2 = "school_class";
    private static final String ARG_PARAM3 = "isPositive";

    private ArrayList<String> studentIds;
    private String schoolClassId;
    private boolean isPositive;

    private BehaviorPagerAdapter behaviorPagerAdapter;
    private ViewPager vpPager;
    private TitlePageIndicator titleIndicator;

    public BehaviorPagerFragment() {
        // Required empty public constructor
    }

    public static BehaviorPagerFragment newInstance(ArrayList<String> studentIds, String schoolClassId, boolean isPositive) {
        BehaviorPagerFragment fragment = new BehaviorPagerFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, studentIds);
        args.putString(ARG_PARAM2, schoolClassId);
        args.putBoolean(ARG_PARAM3, isPositive);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.studentIds = null;
        this.schoolClassId = null;
        this.isPositive = true;

        if (getArguments() != null) {
            this.studentIds = getArguments().getStringArrayList(ARG_PARAM1);
            this.schoolClassId = getArguments().getString(ARG_PARAM2);
            this.isPositive = getArguments().getBoolean(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_behavior_pager, container, false);

        vpPager = (ViewPager) v.findViewById(R.id.vpPager);
        titleIndicator = (TitlePageIndicator) v.findViewById(R.id.titleIndicator);
        setupPager();

        return v;
    }

    public void setupPager() {
        if (vpPager != null) {
            behaviorPagerAdapter = new BehaviorPagerAdapter(this, getFragmentManager(), studentIds, schoolClassId, isPositive);
            vpPager.setAdapter(behaviorPagerAdapter);

            if (titleIndicator != null) {
                titleIndicator.setViewPager(vpPager);
            }
        }
    }

    public void reset(ArrayList<String> studentIds, String schoolClassId, boolean isPositive) {
        this.studentIds.clear();
        this.studentIds = null;
        this.studentIds = new ArrayList<String>(studentIds);

        this.schoolClassId = schoolClassId;
        this.isPositive = isPositive;
        if (behaviorPagerAdapter != null) {
            behaviorPagerAdapter.clearAll();
        }
        if (vpPager != null) {
            vpPager.removeAllViews();
            vpPager.setAdapter(null);
        }
        behaviorPagerAdapter = null;
        setupPager();
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
