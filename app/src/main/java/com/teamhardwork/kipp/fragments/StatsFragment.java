package com.teamhardwork.kipp.fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.queries.FeedQueries;
import com.teamhardwork.kipp.utilities.behavior_event.BehaviorEventListFilterer;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

// Stats for class
public class StatsFragment extends BaseKippFragment implements Updatable {
    private static final int GOOD_COLOR_ID = Color.parseColor("#22BD89");
    private static final int BAD_COLOR_ID = Color.parseColor("#FC6C85");
    private static final int EXTRA_COLOR_ONE = Color.parseColor("#1589FF");
    private static final int EXTRA_COLOR_TWO = Color.parseColor("#7A5DC7");
    private static final int EXTRA_COLOR_THREE = Color.parseColor("#FDD017");
    private static final int EXTRA_COLOR_FOUR = Color.parseColor("#E66C2C");

    private static final List<Behavior> badBehaviors = Arrays.asList(Behavior.DRESS_CODE_VIOLATION,
            Behavior.LACK_OF_INTEGRITY, Behavior.LATE, Behavior.TALKING, Behavior.HORSEPLAY,
            Behavior.FIGHTING);
    private static final List<Behavior> goodBehaviors = Arrays.asList(Behavior.CLEANING_UP,
            Behavior.ON_TASK, Behavior.RESPECTING_EVERYONE, Behavior.SHOWING_GRATITUDE,
            Behavior.SILENT_REMINDERS, Behavior.VOLUNTEERING);

    protected String statForString = "Class";

    @InjectView(R.id.tvBarLabel)
    TextView tvBarLabel;
    @InjectView(R.id.rlBarChartContainer)
    RelativeLayout rlBarChartContainer;
    @InjectView(R.id.barGraph)
    BarGraph barGraph;
    @InjectView(R.id.rlRecommendationContainer)
    RelativeLayout rlRecommendationContainer;
    @InjectView(R.id.tvRecommendation)
    TextView tvRecommendation;
    @InjectView(R.id.btnDismissRecommendation)
    Button btnDismissRecommendation;
    @InjectView(R.id.pieGraph)
    PieGraph pieGraph;
    @InjectView(R.id.tvLegendDescription)
    TextView tvLegendDescription;
    @InjectView(R.id.tvGood)
    TextView tvGood;
    @InjectView(R.id.tvBad)
    TextView tvBad;
    @InjectView(R.id.tvExtraOne)
    TextView tvExtraOne;
    @InjectView(R.id.tvExtraTwo)
    TextView tvExtraTwo;
    @InjectView(R.id.tvExtraThree)
    TextView tvExtraThree;
    @InjectView(R.id.tvExtraFour)
    TextView tvExtraFour;
    @InjectView(R.id.llLegend)
    RelativeLayout rlLegend;
    @InjectView(R.id.rlBackButton)
    RelativeLayout rlBackButton;
    @InjectView(R.id.ivBack)
    ImageView ivBack;
    @InjectView(R.id.tvBack)
    TextView tvBack;

    private ChartMode chartMode = ChartMode.OVERALL;
    private List<TextView> legendItems;
    private Map<Behavior, Integer> behaviorCounts;
    private List<BehaviorEvent> curBehaviorEvents;
    protected FindCallback<BehaviorEvent> overallResponseCallback =
            new FindCallback<BehaviorEvent>() {
                @Override
                public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                    curBehaviorEvents = behaviorEvents;
                    behaviorCounts = BehaviorEventListFilterer.getGroupedCount(behaviorEvents);
                    activateOverallChart(behaviorEvents);
                    setRecommendation(behaviorEvents);
                    progressBar.setVisibility(View.GONE);
                }
            };
    private Spring mScaleSpring;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rtnView = inflater.inflate(R.layout.fragment_stats, container, false);
        ButterKnife.inject(this, rtnView);
        setTypeface();

        legendItems = Arrays.asList(tvGood, tvBad, tvExtraOne, tvExtraTwo, tvExtraThree,
                tvExtraFour);

        setupChart();
        fillChartWithOverallData();

        mScaleSpring = SpringSystem.create().createSpring();

        mScaleSpring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(
                        spring.getCurrentValue(), 0, 1, 1, 0.5);
                pieGraph.setScaleX(mappedValue);
                pieGraph.setScaleY(mappedValue);
            }
        });

        rlLegend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (chartMode != ChartMode.OVERALL) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mScaleSpring.setEndValue(1);
                            break;
                        default:
                            mScaleSpring.setEndValue(0);
                    }
                }
                return false;
            }
        });

        rlBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chartMode != ChartMode.OVERALL) {
                    activateOverallChart(curBehaviorEvents);
                    chartMode = ChartMode.OVERALL;
                }
            }
        });

        pieGraph.setInnerCircleRatio(0);
        pieGraph.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener() {
            @Override
            public void onClick(int index) {
                if (chartMode == ChartMode.OVERALL) {
                    switch (index) {
                        case 0:
                            chartMode = ChartMode.GOOD_DETAIL;
                            activateChartForBehaviors(behaviorCounts, goodBehaviors);
                            break;
                        case 1: // Bad behaviors detail
                            chartMode = ChartMode.BAD_DETAIL;
                            activateChartForBehaviors(behaviorCounts, badBehaviors);
                    }
                } else {
                    setupBarGraph();
                    rlBackButton.setVisibility(View.GONE);
                }
            }
        });

        rlRecommendationContainer.setVisibility(View.GONE);
        tvRecommendation.setVisibility(View.GONE);
        btnDismissRecommendation.setVisibility(View.GONE);

        return rtnView;
    }

    private void setupBarGraph() {
        pieGraph.setVisibility(View.GONE);
        rlLegend.setVisibility(View.GONE);
        rlBarChartContainer.setVisibility(View.VISIBLE);

        if (barGraph.getBars().size() == 0) {
            tvBarLabel.setTypeface(KippApplication.getDefaultTypeFace(getActivity()));
            ArrayList<Bar> points = new ArrayList<Bar>();
            Bar d = new Bar();
            d.setColor(Color.parseColor("#99CC00"));
            d.setName("May");
            d.setValue(10);
            Bar d2 = new Bar();
            d2.setColor(Color.parseColor("#99CC00"));
            d2.setName("Jun");
            d2.setValue(2);
            Bar d3 = new Bar();
            d3.setColor(Color.parseColor("#99CC00"));
            d3.setName("Jul");
            d3.setValue(5);
            Bar d4 = new Bar();
            d4.setColor(Color.parseColor("#99CC00"));
            d4.setName("Aug");
            d4.setValue(2);
            Bar d5 = new Bar();
            d5.setColor(Color.parseColor("#99CC00"));
            d5.setName("Sep");
            d5.setValue(6);
            Bar d6 = new Bar();
            d6.setColor(Color.parseColor("#99CC00"));
            d6.setName("Oct");
            d6.setValue(8);
            points.add(d);
            points.add(d2);
            points.add(d3);
            points.add(d4);
            points.add(d5);
            points.add(d6);

            barGraph.setBars(points);
        };

        rlBarChartContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in));
    }

    void setTypeface() {
        Typeface typeface = KippApplication.getDefaultTypeFace(getActivity());
        tvRecommendation.setTypeface(typeface);
        tvLegendDescription.setTypeface(typeface);
        tvGood.setTypeface(typeface);
        tvBad.setTypeface(typeface);
        tvExtraFour.setTypeface(typeface);
        tvExtraThree.setTypeface(typeface);
        tvExtraTwo.setTypeface(typeface);
        tvExtraOne.setTypeface(typeface);
    }

    protected void fillChartWithOverallData() {
        FeedQueries.getClassFeed(currentClass, overallResponseCallback);
    }

    protected void setRecommendation(List<BehaviorEvent> behaviorEvents) {}

    private void activateOverallChart(List<BehaviorEvent> behaviorEvents) {
        unsetChartValues();
        List<BehaviorEvent> good = BehaviorEventListFilterer.keepGood(behaviorEvents);
        List<BehaviorEvent> bad = BehaviorEventListFilterer.keepBad(behaviorEvents);
        pieGraph.getSlice(OverallSlices.GOOD.ordinal()).setGoalValue(good.size());
        pieGraph.getSlice(OverallSlices.BAD.ordinal()).setGoalValue(bad.size());
        pieGraph.animateToGoalValues();
        activateOverallLegend(good.size(), bad.size());
    }

    private void activateChartForBehaviors(Map<Behavior, Integer> groupedCounts,
                                           List<Behavior> selectedBehaviors) {
        unsetChartValues();
        for (int i = 0; i < selectedBehaviors.size(); i++) {
            pieGraph.getSlice(i).setGoalValue(groupedCounts.get(selectedBehaviors.get(i)));
        }
        pieGraph.animateToGoalValues();

        activateLegendForBehaviors(behaviorCounts, selectedBehaviors);
    }

    private void unsetChartValues() {
        for (PieSlice slice : pieGraph.getSlices()) {
            slice.setGoalValue(0);
        }
    }

    @OnClick(R.id.ivBarGraphBack)
    void barToPie() {
        pieGraph.setVisibility(View.VISIBLE);
        rlLegend.setVisibility(View.VISIBLE);
        rlBarChartContainer.setVisibility(View.GONE);
        pieGraph.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in));
        rlLegend.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in));
        rlBackButton.setVisibility(View.VISIBLE);
    }

    private void setupChart() {
        pieGraph.setInnerCircleRatio(200);
        addSlice(pieGraph, GOOD_COLOR_ID, 0, "Good");
        addSlice(pieGraph, BAD_COLOR_ID, 0, "Bad");
        addSlice(pieGraph, EXTRA_COLOR_ONE, 0, "Extra One");
        addSlice(pieGraph, EXTRA_COLOR_TWO, 0, "Extra TWO");
        addSlice(pieGraph, EXTRA_COLOR_THREE, 0, "Extra THREE");
        addSlice(pieGraph, EXTRA_COLOR_FOUR, 0, "Extra FOUR");

        setupLegend();
    }

    private void setupLegend() {
        tvGood.setTextColor(GOOD_COLOR_ID);
        tvBad.setTextColor(BAD_COLOR_ID);
        tvExtraOne.setTextColor(EXTRA_COLOR_ONE);
        tvExtraTwo.setTextColor(EXTRA_COLOR_TWO);
        tvExtraThree.setTextColor(EXTRA_COLOR_THREE);
        tvExtraFour.setTextColor(EXTRA_COLOR_FOUR);
    }

    private void activateOverallLegend(int numGood, int numBad) {
        int total = numGood + numBad;
        tvLegendDescription.setText("Behavior for " + statForString);
        tvGood.setText("Good: " + getPercentString((double) numGood / total));
        tvBad.setText("Bad: " + getPercentString((double) numBad / total));
        tvGood.setVisibility(View.VISIBLE);
        tvBad.setVisibility(View.VISIBLE);
        turnOffExtraLegendItems();
    }

    private void activateLegendForBehaviors(Map<Behavior, Integer> groupedCounts,
                                            List<Behavior> behaviors) {
        String behaviorType = "Good Behaviors";
        if (chartMode.equals(ChartMode.BAD_DETAIL)) {
            behaviorType = "Bad Behaviors";
        }
        tvLegendDescription.setText(behaviorType + " for " + statForString);
        turnOffExtraLegendItems();
        int total = 0;
        for (Behavior behavior : behaviors) {
            total += groupedCounts.get(behavior);
        }

        for (int i = 0; i < behaviors.size(); i++) {
            Behavior behavior = behaviors.get(i);
            TextView legendItem = legendItems.get(i);
            String percentage = getPercentString((double) groupedCounts.get(behavior) / total);
            legendItem.setText(behavior.getTitle() + ": " + percentage);
            legendItem.setVisibility(View.VISIBLE);
        }
        rlBackButton.setVisibility(View.VISIBLE);
    }

    private void turnOffExtraLegendItems() {
        tvExtraOne.setVisibility(View.GONE);
        tvExtraTwo.setVisibility(View.GONE);
        tvExtraThree.setVisibility(View.GONE);
        tvExtraFour.setVisibility(View.GONE);
        rlBackButton.setVisibility(View.GONE);
    }

    private String getPercentString(double fraction) {
        return MessageFormat.format("{0,number,#.##%}", fraction);
    }

    private void addSlice(PieGraph pieGraph, int colorId, int value, String title) {
        PieSlice slice = new PieSlice();
        slice.setColor(colorId);
        slice.setValue(value);
        slice.setTitle(title);
        pieGraph.addSlice(slice);
    }

    @Override
    public void updateData() {
        fillChartWithOverallData();
    }

    @Override
    public String getTitle() {
        return "Stats";
    }

    public enum ChartMode {
        OVERALL, BAD_DETAIL, GOOD_DETAIL
    }

    private enum OverallSlices {
        GOOD, BAD
    }
}
