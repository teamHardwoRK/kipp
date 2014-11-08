package com.teamhardwork.kipp.stats;

import android.content.Context;
import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    private SimpleDateFormat dateFormatter = new SimpleDateFormat ("EEEE");

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

    public void setup () {
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

        tvBarLabel.setTypeface(KippApplication.getDefaultTypeFace(context));
        ArrayList<Bar> points = new ArrayList<Bar>();
        for (int i = 0; i < BAR_LABELS.size(); i++) {
            Bar bar = new Bar();
            bar.setValue(0);
            bar.setColor(COLORS.get(i));
            bar.setLabelColor(COLORS.get(i));
            bar.setName(BAR_LABELS.get(i));
            bar.setGoalValue(BAR_VALUES.get(i));
            bar.setValueColor(COLORS.get(i));
            points.add(bar);
        }
        barGraph.setBars(points);

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
