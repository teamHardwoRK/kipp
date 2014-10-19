package com.teamhardwork.kipp.fragments;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.parse.FindCallback;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.queries.BehaviorRetriever;
import com.teamhardwork.kipp.utilities.behavior_event.BehaviorEventListFilterer;

import java.text.MessageFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StatsFragment extends Fragment {
    private static final int GOOD_COLOR_ID = Color.parseColor("#33CC00");
    private static final int BAD_COLOR_ID = Color.parseColor("#FF3333");
    @InjectView(R.id.pgTest)
    PieGraph pg;
    @InjectView(R.id.llLegend)
    LinearLayout llLegend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rtnView = inflater.inflate(R.layout.fragment_stats, container, false);
        ButterKnife.inject(this, rtnView);

        setupGoodBadChart();

        return rtnView;
    }

    private void setupGoodBadChart() {
        try {
            SchoolClass testClass = SchoolClass.findById("d1RmWrazIm");
            BehaviorRetriever.findBySchoolClass(testClass, new FindCallback<BehaviorEvent>() {
                @Override
                public void done(List<BehaviorEvent> behaviorEvents, com.parse.ParseException e) {
                    List<BehaviorEvent> good = BehaviorEventListFilterer.keepGood(behaviorEvents);
                    List<BehaviorEvent> bad = BehaviorEventListFilterer.keepBad(behaviorEvents);
                    populateGoodBadChart(good, bad);
                }
            });
        } catch (com.parse.ParseException e) {
            Toast.makeText(getActivity(), "Error in test data", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateGoodBadChart(List<BehaviorEvent> good, List<BehaviorEvent> bad) {
        pg.setInnerCircleRatio(200);
        addSlice(GOOD_COLOR_ID, good.size(), "Good");
        addSlice(BAD_COLOR_ID, bad.size(), "Bad");
        setupLegend(good, bad);
    }

    private void setupLegend(List<BehaviorEvent> good, List<BehaviorEvent> bad) {
        int totalSize = good.size() + bad.size();
        String goodPercentage = MessageFormat.format("{0,number,#.##%}", ((double) good.size()) / totalSize);
        String badPercentage = MessageFormat.format("{0,number,#.##%}", ((double) bad.size()) / totalSize);
        addToLegend("Good: " + goodPercentage, GOOD_COLOR_ID);
        addToLegend("Bad: " + badPercentage, BAD_COLOR_ID);
    }

    private void addToLegend(String text, int colorId) {
        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView legendItem = new TextView(getActivity());
        legendItem.setLayoutParams(lparams);
        legendItem.setText(text);
        legendItem.setTextColor(colorId);
        llLegend.addView(legendItem);

    }

    private void addSlice(int colorId, int value, String title) {
        PieSlice slice = new PieSlice();
        slice.setColor(colorId);
        slice.setValue(value);
        slice.setTitle(title);
        pg.addSlice(slice);
    }

}
