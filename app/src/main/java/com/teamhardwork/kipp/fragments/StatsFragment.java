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
import com.parse.ParseException;
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

    private List<BehaviorEvent> currentBehaviorEvents;
    private FeedFragment.FeedType currentChartType = FeedFragment.FeedType.CLASS;
    private Student currentStudent;

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

        setupGoodBadChart();
        updateChartForClass();

        return rtnView;
    }

    public void updateChartForClass() {
        FeedQueries.getClassFeed(currentClass, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, com.parse.ParseException e) {
                currentBehaviorEvents = behaviorEvents;
                currentChartType = FeedFragment.FeedType.CLASS;
                updateChart(currentBehaviorEvents);
                tvLegendDescription.setText("Class behavior history");
            }
        });

    }

    public void updateChartForStudent(final Student currentStudent) {
        this.currentStudent = currentStudent;
        FeedQueries.getStudentFeed(currentStudent, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, com.parse.ParseException e) {
                currentBehaviorEvents = behaviorEvents;
                currentChartType = FeedFragment.FeedType.STUDENT;
                updateChart(currentBehaviorEvents);
                tvLegendDescription.setText("Student behavior history");
            }
        });
    }

    private void setupGoodBadChart() {
        pg.setInnerCircleRatio(200);

        addSlice(GOOD_COLOR_ID, 0, "Good");
        addSlice(BAD_COLOR_ID, 0, "Bad");

        tvGood.setTextColor(GOOD_COLOR_ID);
        tvBad.setTextColor(BAD_COLOR_ID);
    }

    private void updateChart(List<BehaviorEvent> behaviorEvents) {
        List<BehaviorEvent> good = BehaviorEventListFilterer.keepGood(behaviorEvents);
        List<BehaviorEvent> bad = BehaviorEventListFilterer.keepBad(behaviorEvents);

        updateLegend(good, bad);
        updateSlices(good, bad);
    }

    private void updateSlices(List<BehaviorEvent> good, List<BehaviorEvent> bad) {
        pg.getSlice(0).setGoalValue(good.size());
        pg.getSlice(1).setGoalValue(bad.size());
        pg.animateToGoalValues();
    }

    private void updateLegend(List<BehaviorEvent> good, List<BehaviorEvent> bad) {
        int totalSize = good.size() + bad.size();
        String goodPercentage = MessageFormat.format("{0,number,#.##%}", ((double) good.size()) / totalSize);
        String badPercentage = MessageFormat.format("{0,number,#.##%}", ((double) bad.size()) / totalSize);
        tvGood.setText("Good: " + goodPercentage);
        tvBad.setText("Bad: " + badPercentage);
    }

    private void addSlice(int colorId, int value, String title) {
        PieSlice slice = new PieSlice();
        slice.setColor(colorId);
        slice.setValue(value);
        slice.setTitle(title);
        pg.addSlice(slice);
    }

    public void updateData() {
        FindCallback<BehaviorEvent> callback = new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> eventList, ParseException e) {
                currentBehaviorEvents.addAll(eventList);
                updateChart(currentBehaviorEvents);
            }
        };

        switch (currentChartType) {
            case STUDENT:
                FeedQueries.getLatestStudentEvents(currentStudent, currentBehaviorEvents, callback);
                break;
            case CLASS:
                FeedQueries.getLatestClassEvents(currentClass, currentBehaviorEvents, callback);
                break;
        }
    }

}
