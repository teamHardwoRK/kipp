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
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.utilities.behavior_event.BehaviorEventListFilterer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hugh_sd on 11/8/14.
 */
public class BarStats {
    private Context context;
    private RelativeLayout rlBarChartContainer;
    private BarGraph barGraph;
    private TextView tvBarLabel;
    private List<BehaviorEvent> behaviorEvents = null;
    private Map<Behavior, Integer> behaviorCounts;

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

    public void setup(Behavior behavior, int behaviorColor) {
        if (behaviorEvents == null || behavior == null) {
            barChartExit(1000);
            return;
        }

        int behaviorCount = behaviorCounts.get(behavior);
        Log.e("BarGraph", "behavior " + behavior.getTitle() + ": " + behaviorCount);

        List<BehaviorEvent> filteredEvents = BehaviorEventListFilterer.filterByBehavior(behaviorEvents, behavior);
        BarData barData = new BarData();
        barData.configBarData(context, filteredEvents, tvBarLabel);
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
        for (Date date : barData.getBarKeySet()) {
            Bar bar = new Bar();
            bar.setColor(behaviorColor);
            bar.setName(barData.getBarLabel(date));
            bar.setValue(0);
            bar.setGoalValue(barData.getBarValue(date));
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
