package com.teamhardwork.kipp.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.parse.FindCallback;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;
import com.teamhardwork.kipp.utilities.behavior_event.BehaviorEventListFilterer;

import java.text.MessageFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StatsFragment extends BaseKippFragment {
    private static final int GOOD_COLOR_ID = Color.parseColor("#33CC00");
    private static final int BAD_COLOR_ID = Color.parseColor("#FF3333");

    @InjectView(R.id.pgTest)
    PieGraph pg;
    @InjectView(R.id.llLegend)
    LinearLayout llLegend;
    @InjectView(R.id.tvLegendDescription)
    TextView tvLegendDescription;
    @InjectView(R.id.tvGood)
    TextView tvGood;
    @InjectView(R.id.tvBad)
    TextView tvBad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rtnView = inflater.inflate(R.layout.fragment_stats, container, false);
        ButterKnife.inject(this, rtnView);

        setupGoodBadChartForClass();

        return rtnView;
    }

    public void setupGoodBadChartForClass() {
        FeedQueries.getClassFeed(currentClass, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, com.parse.ParseException e) {
                List<BehaviorEvent> good = BehaviorEventListFilterer.keepGood(behaviorEvents);
                List<BehaviorEvent> bad = BehaviorEventListFilterer.keepBad(behaviorEvents);
                populateGoodBadChart(good, bad);
                tvLegendDescription.setText("Class behavior history");
            }
        });

    }

    public void setupGoodBadChartForStudent(final Student currentStudent) {
        FeedQueries.getStudentFeed(currentStudent, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, com.parse.ParseException e) {
                List<BehaviorEvent> good = BehaviorEventListFilterer.keepGood(behaviorEvents);
                List<BehaviorEvent> bad = BehaviorEventListFilterer.keepBad(behaviorEvents);
                populateGoodBadChart(good, bad);
                tvLegendDescription.setText("Student behavior history");
            }
        });
    }

    private void populateGoodBadChart(List<BehaviorEvent> good, List<BehaviorEvent> bad) {
        pg.setInnerCircleRatio(200);
        pg.removeSlices();
        addSlice(GOOD_COLOR_ID, good.size(), "Good");
        addSlice(BAD_COLOR_ID, bad.size(), "Bad");
        setupLegend(good, bad);
    }

    private void setupLegend(List<BehaviorEvent> good, List<BehaviorEvent> bad) {
        int totalSize = good.size() + bad.size();
        String goodPercentage = MessageFormat.format("{0,number,#.##%}", ((double) good.size()) / totalSize);
        String badPercentage = MessageFormat.format("{0,number,#.##%}", ((double) bad.size()) / totalSize);
        tvGood.setText("Good: " + goodPercentage);
        tvGood.setTextColor(GOOD_COLOR_ID);
        tvBad.setText("Bad: " + badPercentage);
        tvBad.setTextColor(BAD_COLOR_ID);
    }

    private void addSlice(int colorId, int value, String title) {
        PieSlice slice = new PieSlice();
        slice.setColor(colorId);
        slice.setValue(value);
        slice.setTitle(title);
        pg.addSlice(slice);
    }

}
