package com.teamhardwork.kipp.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.teamhardwork.kipp.stats.BarStats;
import com.teamhardwork.kipp.utilities.ReboundAnimator;
import com.teamhardwork.kipp.utilities.behavior_event.BehaviorEventListFilterer;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

// Stats for class
public class StatsFragment extends BaseKippFragment implements Updatable {
    private static final int GOOD_COLOR_ID = Color.parseColor("#22BD89");
    private static final int BAD_COLOR_ID = Color.parseColor("#FC6C85");
    private static final int EXTRA_COLOR_ONE = Color.parseColor("#1589FF");
    private static final int EXTRA_COLOR_TWO = Color.parseColor("#7A5DC7");
    private static final int EXTRA_COLOR_THREE = Color.parseColor("#FDD017");
    private static final int EXTRA_COLOR_FOUR = Color.parseColor("#E66C2C");

    private static final List<Integer> COLORS = Collections.unmodifiableList(Arrays.asList(
            GOOD_COLOR_ID, BAD_COLOR_ID, EXTRA_COLOR_ONE, EXTRA_COLOR_TWO, EXTRA_COLOR_THREE,
            EXTRA_COLOR_FOUR
    ));
    private static final List<Behavior> badBehaviors = Arrays.asList(Behavior.DRESS_CODE_VIOLATION,
            Behavior.LACK_OF_INTEGRITY, Behavior.LATE, Behavior.TALKING, Behavior.HORSEPLAY,
            Behavior.FIGHTING);
    private static final List<Behavior> goodBehaviors = Arrays.asList(Behavior.CLEANING_UP,
            Behavior.ON_TASK, Behavior.RESPECTING_EVERYONE, Behavior.SHOWING_GRATITUDE,
            Behavior.SILENT_REMINDERS, Behavior.VOLUNTEERING);

    protected String statForString = "Class";
    protected FindCallback<BehaviorEvent> overallResponseCallback;
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
    @InjectView(R.id.llLegend)
    RelativeLayout rlLegend;
    @InjectView(R.id.rlBackButton)
    RelativeLayout rlBackButton;
    @InjectViews({R.id.tvGood, R.id.tvBad, R.id.tvExtraOne, R.id.tvExtraTwo, R.id.tvExtraThree,
            R.id.tvExtraFour})
    List<TextView> legendItems;
    private ChartMode chartMode;
    private ChartMode previousMode;
    private Map<Behavior, Integer> behaviorCounts;
    private List<BehaviorEvent> curBehaviorEvents;
    private List<BehaviorEvent> curGood;
    private List<BehaviorEvent> curBad;
    private Spring mScaleSpring;
    private BarStats barStats;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rtnView = inflater.inflate(R.layout.fragment_stats, container, false);
        ButterKnife.inject(this, rtnView);
        barStats = new BarStats(getActivity(), rlBarChartContainer, barGraph, tvBarLabel);
        setTypeface();
        chartMode = ChartMode.OVERALL;
        previousMode = null;

        overallResponseCallback = new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                if (chartMode == ChartMode.OVERALL) {
                    curBehaviorEvents = behaviorEvents;
                    behaviorCounts = BehaviorEventListFilterer.getGroupedCount(behaviorEvents);
                    activateOverallChart(behaviorEvents);
                    setRecommendation(behaviorEvents);

                    if (rlLegend.getVisibility() != View.VISIBLE) {
                        rlLegend.setVisibility(View.VISIBLE);
                        Animation enter = AnimationUtils.loadAnimation(getActivity(), R.anim.top_in);
                        enter.setStartOffset(1000);
                        rlLegend.setAnimation(enter);
                    }
                }
            }
        };

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
                barGraph.setScaleX(mappedValue);
                barGraph.setScaleY(mappedValue);
            }
        });

        rlBackButton.setOnTouchListener(new View.OnTouchListener() {
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

        pieGraph.setInnerCircleRatio(0);
        pieGraph.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener() {
            @Override
            public void onClick(int index) {
                ReboundAnimator.startClickAnimation(pieGraph);
                if (chartMode == ChartMode.OVERALL) {
                    previousMode = chartMode;
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
                }
            }
        });

        return rtnView;
    }

    private void setupBarGraph() {
        previousMode = chartMode;
        chartMode = ChartMode.BAR;
        pieGraph.setVisibility(View.GONE);
        rlLegend.setVisibility(View.GONE);
        barStats.setup();
    }

    void setTypeface() {
        Typeface typeface = KippApplication.getDefaultTypeFace(getActivity());
        tvRecommendation.setTypeface(typeface);
        tvLegendDescription.setTypeface(typeface);

        for (TextView tvLegendItem : legendItems) {
            tvLegendItem.setTypeface(typeface);
        }
    }

    protected void fillChartWithOverallData() {
        FeedQueries.getClassFeed(currentClass, overallResponseCallback);
    }

    protected void setRecommendation(List<BehaviorEvent> behaviorEvents) {

    }

    private void activateOverallChart(List<BehaviorEvent> behaviorEvents) {
        unsetChartValues();
        curGood = BehaviorEventListFilterer.keepGood(behaviorEvents);
        curBad = BehaviorEventListFilterer.keepBad(behaviorEvents);
        pieGraph.getSlice(OverallSlices.GOOD.ordinal()).setGoalValue(curGood.size());
        pieGraph.getSlice(OverallSlices.BAD.ordinal()).setGoalValue(curBad.size());
        pieGraph.animateToGoalValues();
        activateOverallLegend(curGood.size(), curBad.size());
        barStats.activateBarStats(behaviorEvents, curGood, curBad);
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

    @OnClick(R.id.rlBackButton)
    void gotoPreviousChart() {
        if (chartMode == ChartMode.BAR) {
            // Shrink bar chart, grow pie chart
            Animation enterRight = AnimationUtils.loadAnimation(getActivity(), R.anim.right_in);
            enterRight.setStartOffset(1000);
            enterRight.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    prepareBarToPieAnimation();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    executeBarToPieAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            barStats.barChartExit(1000);
            pieGraph.startAnimation(enterRight);

            chartMode = previousMode;
            previousMode = ChartMode.BAR;
        } else if (chartMode != ChartMode.OVERALL) {
            activateOverallChart(curBehaviorEvents);
            previousMode = chartMode;
            chartMode = ChartMode.OVERALL;
        }
    }

    private void prepareBarToPieAnimation() {
        pieGraph.setVisibility(View.VISIBLE);
        List<PieSlice> slices = pieGraph.getSlices();
        for (PieSlice slice : slices) {
            slice.setGoalValue(slice.getValue());
            slice.setValue(0);
        }
        slices.get(0).setValue(1);
    }

    private void executeBarToPieAnimation() {
        rlBarChartContainer.setVisibility(View.GONE);
        pieGraph.animateToGoalValues();
        Animation enter = AnimationUtils.loadAnimation(getActivity(), R.anim.top_in);
        enter.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rlLegend.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rlLegend.setAnimation(enter);

    }



    private void setupChart() {
        pieGraph.setInnerCircleRatio(200);
        pieGraph.setDuration(1000);
        addSlice(pieGraph, GOOD_COLOR_ID, 100, "Good");
        addSlice(pieGraph, BAD_COLOR_ID, 0, "Bad");
        addSlice(pieGraph, EXTRA_COLOR_ONE, 0, "Extra One");
        addSlice(pieGraph, EXTRA_COLOR_TWO, 0, "Extra TWO");
        addSlice(pieGraph, EXTRA_COLOR_THREE, 0, "Extra THREE");
        addSlice(pieGraph, EXTRA_COLOR_FOUR, 0, "Extra FOUR");

        setupLegend();
    }

    private void setupLegend() {
        for (int i = 0; i < legendItems.size(); i++) {
            TextView legendItem = legendItems.get(i);
            legendItem.setTextColor(COLORS.get(i));
        }
    }

    private void activateOverallLegend(int numGood, int numBad) {
        int total = numGood + numBad;
        tvLegendDescription.setText("Behavior for " + statForString +
                "\n\t\t\t(total " + (numBad + numGood) + ")");
        TextView tvGood = legendItems.get(0);
        TextView tvBad = legendItems.get(1);
        tvGood.setText("Good: " + getPercentString((double) numGood / total));
        tvBad.setText("Bad: " + getPercentString((double) numBad / total));
        tvGood.setVisibility(View.VISIBLE);
        tvBad.setVisibility(View.VISIBLE);
        turnOffExtraLegendItems();
        animateLegendUpdate();
    }

    private void animateLegendUpdate() {
        if (previousMode != null) { // Don't call when pie graph is initializing
            Animation exit = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_out);
            exit.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Animation enter = AnimationUtils.loadAnimation(getActivity(), R.anim.top_in);
                    rlLegend.setAnimation(enter);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            rlLegend.setAnimation(exit);
        }
    }

    private void activateLegendForBehaviors(Map<Behavior, Integer> groupedCounts,
                                            List<Behavior> behaviors) {
        String behaviorType = "Good Behaviors";
        if (chartMode.equals(ChartMode.BAD_DETAIL)) {
            behaviorType = "Bad Behaviors";
        }
        int total = 0;
        for (Behavior behavior : behaviors) {
            total += groupedCounts.get(behavior);
        }
        tvLegendDescription.setText(behaviorType + " for " + statForString + "\n\t\t\t(total " + total + ")");
        turnOffExtraLegendItems();

        for (int i = 0; i < behaviors.size(); i++) {
            Behavior behavior = behaviors.get(i);
            TextView legendItem = legendItems.get(i);
            String percentage = getPercentString((double) groupedCounts.get(behavior) / total);
            legendItem.setText(behavior.getTitle() + ": " + percentage);
            legendItem.setVisibility(View.VISIBLE);
        }
        rlBackButton.setVisibility(View.VISIBLE);
        animateLegendUpdate();
    }

    private void turnOffExtraLegendItems() {
        for (int i = 2; i < legendItems.size(); i++) {
            legendItems.get(i).setVisibility(View.GONE);
        }
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
        OVERALL, BAD_DETAIL, GOOD_DETAIL, BAR
    }

    private enum OverallSlices {
        GOOD, BAD
    }
}