package com.teamhardwork.kipp.stats;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.utilities.behavior_event.BehaviorEventListFilterer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by hugh_sd on 11/8/14.
 */
public class BarStats {
    private static final int GOOD_COLOR_ID = Color.parseColor("#22BD89");
    private static final int BAD_COLOR_ID = Color.parseColor("#FC6C85");
    private static final int EXTRA_COLOR_ONE = Color.parseColor("#1589FF");
    private static final int EXTRA_COLOR_TWO = Color.parseColor("#7A5DC7");
    private static final int EXTRA_COLOR_THREE = Color.parseColor("#FDD017");
    private static final int EXTRA_COLOR_FOUR = Color.parseColor("#E66C2C");

    private static final List<String> BAR_LABELS = Collections.unmodifiableList(
            Arrays.asList("Jun", "Jul", "Aug", "Sep", "Oct", "Nov")
    );
    private static final List<Integer> BAR_VALUES = Collections.unmodifiableList(
            Arrays.asList(3, 8, 5, 10, 6, 1)
    );
    private static final List<Integer> COLORS = Collections.unmodifiableList(Arrays.asList(
            GOOD_COLOR_ID, BAD_COLOR_ID, EXTRA_COLOR_ONE, EXTRA_COLOR_TWO, EXTRA_COLOR_THREE,
            EXTRA_COLOR_FOUR
    ));

    private Context context;
    private RelativeLayout rlBarChartContainer;
    private BarGraph barGraph;
    private TextView tvBarLabel;
    private List<BehaviorEvent> behaviorEvents = null;
    private Map<Behavior, Integer> behaviorCounts;
    private SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MMM");
    private SimpleDateFormat simpleDateFormatWeek = new SimpleDateFormat("W");

    public BarStats(Context context, RelativeLayout rlBarChartContainer, BarGraph barGraph, TextView tvBarLabel) {
        this.context = context;
        this.rlBarChartContainer = rlBarChartContainer;
        this.barGraph = barGraph;
        this.tvBarLabel = tvBarLabel;
    }

    public void activateBarStats(List<BehaviorEvent> behaviorEvents, Map<Behavior, Integer> behaviorCounts) {
        this.behaviorEvents = behaviorEvents;
        this.behaviorCounts = behaviorCounts;
    }

    public Map<Date, Integer> setupBarData(List<BehaviorEvent> events, TextView label) {
        Map<Date, Integer> barData = new TreeMap<Date, Integer>();
        Calendar prevCalendar = Calendar.getInstance();
        Calendar currCalendar = Calendar.getInstance();
        int currCount = 0, diffYears, diffWeeks;

        if (events == null || events.isEmpty()) return null;

        prevCalendar.setTime(events.get(events.size() - 1).getOccurredAt());
        currCount++;
        for (int i = events.size() - 2; i >= 0; i--) {
            BehaviorEvent event = events.get(i);
            currCalendar.setTime(event.getOccurredAt());
            diffYears = currCalendar.get(Calendar.YEAR) - prevCalendar.get(Calendar.YEAR);
            diffWeeks = currCalendar.get(Calendar.WEEK_OF_YEAR) - prevCalendar.get(Calendar.WEEK_OF_YEAR);

            Log.e("BarGraph", "prevEvent: " + prevCalendar.toString());
            Log.e("BarGraph", "currEvent: " + currCalendar.toString());

            if (diffYears == 0 && diffWeeks == 0) {
                currCount++;
                if (i == 0) {
                    barData.put(prevCalendar.getTime(), currCount);
                }
            } else {
                barData.put(prevCalendar.getTime(), currCount);
                currCount = 1;
            }

            prevCalendar.setTime(event.getOccurredAt());
        }

        label.setTypeface(KippApplication.getDefaultTypeFace(context));
        label.setText(events.get(0).getBehavior().getTitle() + " (last " + barData.size() + " weeks)");

        return barData;
    }

    public void setup(Behavior behavior, int behaviorColor) {
        if (behaviorEvents == null || behavior == null) {
            barChartExit(1000);
            return;
        }

        int behaviorCount = behaviorCounts.get(behavior);
        Log.e("BarGraph", "behavior " + behavior.getTitle() + ": " + behaviorCount);

        List<BehaviorEvent> filteredEvents = BehaviorEventListFilterer.filterByBehavior(behaviorEvents, behavior);
        Map<Date, Integer> barData = setupBarData(filteredEvents, tvBarLabel);
        Log.e("BarGraph", barData.toString());

        rlBarChartContainer.setVisibility(View.VISIBLE);
        barGraph.setDuration(1000);
        barGraph.setInterpolator(new AccelerateDecelerateInterpolator());
        barGraph.setValueStringPrecision(0);
        barGraph.setAxisColor(Color.parseColor("#58B488"));
        barGraph.setShowBarText(false);
        barGraph.setShowPopup(false);
        barGraph.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                barGraph.setShowBarText(true);
            }
        }, 1000);

        ArrayList<Bar> barsList = new ArrayList<Bar>();
        for (Date date : barData.keySet()) {
            Bar bar = new Bar();
            bar.setColor(behaviorColor);
            bar.setName(simpleDateFormatMonth.format(date) + ", wk " + simpleDateFormatWeek.format(date));
            bar.setValue(0);
            bar.setGoalValue(barData.get(date));
            bar.setLabelColor(behaviorColor);
            bar.setValueColor(behaviorColor);
            barsList.add(bar);
        }
        barGraph.setBars(barsList);

        Animation enter = AnimationUtils.loadAnimation(context, R.anim.left_in);
        enter.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                barGraph.animateToGoalValues();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rlBarChartContainer.startAnimation(enter);
    }

    public void barChartExit(int durationMillis) {
        List<Bar> bars = barGraph.getBars();
        for (Bar bar : bars) {
            bar.setGoalValue(0);
        }
        barGraph.setShowBarText(false);
        barGraph.setDuration(durationMillis);
        barGraph.animateToGoalValues();
        barGraph.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlBarChartContainer.setVisibility(View.GONE);
            }
        }, durationMillis);
    }
}
