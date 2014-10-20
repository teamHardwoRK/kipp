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
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;
import com.teamhardwork.kipp.utilities.behavior_event.BehaviorEventListFilterer;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

// TODO: Code is doing way too much need to segment out class vs student charts. And totals vs line items charts
public class StatsFragment extends BaseKippFragment {
    private static final int GOOD_COLOR_ID = Color.parseColor("#33CC00");
    private static final int BAD_COLOR_ID = Color.parseColor("#FF3333");

    private static final int GOOD_SLICE = 0;
    private static final int BAD_SLICE = 1;


    private List<BehaviorEvent> currentBehaviorEvents;
    private FeedFragment.FeedType currentChartType = FeedFragment.FeedType.CLASS;
    private Student currentStudent;

    @InjectView(R.id.pgGoodBad)
    PieGraph pgGoodBad;
    @InjectView(R.id.pgBadDetail)
    PieGraph pgBadDetail;
    @InjectView(R.id.tvLegendDescription)
    TextView tvLegendDescription;
    @InjectView(R.id.tvGood)
    TextView tvGood;
    @InjectView(R.id.tvBad)
    TextView tvBad;

    @InjectView(R.id.llGoodBadContainer)
    LinearLayout llGoodBadContainer;

    @InjectView(R.id.tvBadDetailDescription)
    TextView tvBadDetailDescription;
    @InjectView(R.id.llBadDetailContainer)
    LinearLayout llBadDetailContainer;
    @InjectView(R.id.tvDressCodeViolation)
    TextView tvDressCodeViolation;
    @InjectView(R.id.tvLackOfIntegrity)
    TextView tvLackOfIntegrity;
    @InjectView(R.id.tvLate)
    TextView tvLate;
    @InjectView(R.id.tvTalking)
    TextView tvTalking;
    @InjectView(R.id.tvTalkingBack)
    TextView tvTalkingBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rtnView = inflater.inflate(R.layout.fragment_stats, container, false);
        ButterKnife.inject(this, rtnView);

        setupGoodBadChart();
        updateChartForClass();

        setupBadDetailChart();
        updateBadDetailChartForClass();

        llBadDetailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llGoodBadContainer.setVisibility(View.VISIBLE);
                llBadDetailContainer.setVisibility(View.GONE);

                pgBadDetail.setVisibility(View.GONE);
                pgGoodBad.setVisibility(View.VISIBLE);
            }
        });

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

    public void updateBadDetailChartForClass() {
        FeedQueries.getClassFeed(currentClass, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                currentBehaviorEvents = behaviorEvents;
                currentChartType = FeedFragment.FeedType.CLASS;
                Map<Behavior, Integer> groupedBadCounts = BehaviorEventListFilterer
                        .getGroupedCount(behaviorEvents);
                updateBadDetailChart(groupedBadCounts);
                tvBadDetailDescription.setText("Class bad behaviors");

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
        pgGoodBad.setInnerCircleRatio(200);

        addSlice(pgGoodBad,GOOD_COLOR_ID, 0, "Good");
        addSlice(pgGoodBad, BAD_COLOR_ID, 0, "Bad");

        tvGood.setTextColor(GOOD_COLOR_ID);
        tvBad.setTextColor(BAD_COLOR_ID);

        pgGoodBad.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener() {
            @Override
            public void onClick(int index) {
                llGoodBadContainer.setVisibility(View.GONE);
                switch (index) {
                    case GOOD_SLICE:
                        break;
                    case BAD_SLICE:
                        pgGoodBad.setVisibility(View.GONE);
                        pgBadDetail.setVisibility(View.VISIBLE);
                        llBadDetailContainer.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void setupBadDetailChart() {
        pgBadDetail.setInnerCircleRatio(200);

        addSlice(pgBadDetail, getResources().getColor(R.color.BlueDiamond), 0, Behavior.DRESS_CODE_VIOLATION.getTitle());
        addSlice(pgBadDetail, getResources().getColor(R.color.DollarBillGreen), 0, Behavior.LACK_OF_INTEGRITY.getTitle());
        addSlice(pgBadDetail, getResources().getColor(R.color.FlamingoPink), 0, Behavior.LATE.getTitle());
        addSlice(pgBadDetail, getResources().getColor(R.color.VampireGray), 0, Behavior.TALKING.getTitle());
        addSlice(pgBadDetail, getResources().getColor(R.color.Gold), 0, Behavior.TALKING_BACK.getTitle());

        tvDressCodeViolation.setTextColor(getResources().getColor(R.color.BlueDiamond));
        tvLackOfIntegrity.setTextColor(getResources().getColor(R.color.DollarBillGreen));
        tvLate.setTextColor(getResources().getColor(R.color.FlamingoPink));
        tvTalking.setTextColor(getResources().getColor(R.color.VampireGray));
        tvTalkingBack.setTextColor(getResources().getColor(R.color.Gold));


    }

    private void updateChart(List<BehaviorEvent> behaviorEvents) {
        List<BehaviorEvent> good = BehaviorEventListFilterer.keepGood(behaviorEvents);
        List<BehaviorEvent> bad = BehaviorEventListFilterer.keepBad(behaviorEvents);

        updateLegend(good, bad);
        updateSlices(good, bad);
    }

    private void updateSlices(List<BehaviorEvent> good, List<BehaviorEvent> bad) {
        pgGoodBad.getSlice(0).setGoalValue(good.size());
        pgGoodBad.getSlice(1).setGoalValue(bad.size());
        pgGoodBad.animateToGoalValues();
    }

    private void updateLegend(List<BehaviorEvent> good, List<BehaviorEvent> bad) {
        int totalSize = good.size() + bad.size();
        String goodPercentage = MessageFormat.format("{0,number,#.##%}", ((double) good.size()) / totalSize);
        String badPercentage = MessageFormat.format("{0,number,#.##%}", ((double) bad.size()) / totalSize);
        tvGood.setText("Good: " + goodPercentage);
        tvBad.setText("Bad: " + badPercentage);
    }

    private void updateBadDetailLegend(Map<Behavior, Integer> badGroupedCounts) {
        int totalSize = 0;
        for (Integer i : badGroupedCounts.values()) {
            totalSize += i;
        }
        double dressCodeSize = badGroupedCounts.get(Behavior.DRESS_CODE_VIOLATION);
        String dressCodePercentage = MessageFormat.format("{0,number,#.##%}", dressCodeSize / totalSize);
        tvDressCodeViolation.setText("DressCodeViolation: " + dressCodePercentage);

        double lackOfIntegritySize = badGroupedCounts.get(Behavior.LACK_OF_INTEGRITY);
        String lackOfIntegrityPercentage = MessageFormat.format("{0,number,#.##%}", lackOfIntegritySize / totalSize);
        tvLackOfIntegrity.setText("LackOfIntegrity: " + lackOfIntegrityPercentage);

        double lateSize = badGroupedCounts.get(Behavior.LATE);
        String latePercentage = MessageFormat.format("{0,number,#.##%}", lateSize / totalSize);
        tvLate.setText("Late: " + latePercentage);

        double talkingSize = badGroupedCounts.get(Behavior.TALKING);
        String talkingPercentage = MessageFormat.format("{0,number,#.##%}", talkingSize / totalSize);
        tvTalking.setText("Talking: " + talkingPercentage);

        double talkingBackSize = badGroupedCounts.get(Behavior.TALKING_BACK);
        String talkingBackPercentage = MessageFormat.format("{0,number,#.##%}", talkingBackSize / totalSize);
        tvTalkingBack.setText("TalkingBack: " + talkingBackPercentage);
    }

    private void updateBadDetailSlices(Map<Behavior, Integer> badGroupedCounts) {
        pgBadDetail.getSlice(0).setGoalValue(badGroupedCounts.get(Behavior.DRESS_CODE_VIOLATION));
        pgBadDetail.getSlice(1).setGoalValue(badGroupedCounts.get(Behavior.LACK_OF_INTEGRITY));
        pgBadDetail.getSlice(2).setGoalValue(badGroupedCounts.get(Behavior.LATE));
        pgBadDetail.getSlice(3).setGoalValue(badGroupedCounts.get(Behavior.TALKING));
        pgBadDetail.getSlice(4).setGoalValue(badGroupedCounts.get(Behavior.TALKING_BACK));
        pgBadDetail.animateToGoalValues();
    }

    private void updateBadDetailChart(Map<Behavior, Integer> badGroupedCounts) {
        updateBadDetailLegend(badGroupedCounts);
        updateBadDetailSlices(badGroupedCounts);
    }

    private void addSlice(PieGraph pieGraph, int colorId, int value, String title) {
        PieSlice slice = new PieSlice();
        slice.setColor(colorId);
        slice.setValue(value);
        slice.setTitle(title);
        pieGraph.addSlice(slice);
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
