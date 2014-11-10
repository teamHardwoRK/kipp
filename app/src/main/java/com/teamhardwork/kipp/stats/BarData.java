package com.teamhardwork.kipp.stats;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.models.BehaviorEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by hugh_sd on 11/9/14.
 */
public class BarData {
    Map<Date, Integer> barData;
    private boolean isDailyFormat;

    private SimpleDateFormat weeklyFormatMonth = new SimpleDateFormat("MMM");
    private SimpleDateFormat weeklyFormatWeek = new SimpleDateFormat("W");
    private SimpleDateFormat dailyFormat = new SimpleDateFormat("E");

    public BarData() {
        this.barData = new TreeMap<Date, Integer>();
        this.isDailyFormat = false;
    }

    public String getBarLabel(Date date) {
        if (barData == null) {
            return "";
        }
        return isDailyFormat ? dailyFormat.format(date) : weeklyFormatMonth.format(date) + ", wk " + weeklyFormatWeek.format(date);
    }

    public Integer getBarValue(Date date) {
        if (barData == null) {
            return null;
        }
        return barData.get(date);
    }

    public Set<Date> getBarKeySet() {
        if (barData == null) {
            return null;
        }
        return barData.keySet();
    }

    public String toString() {
        if (barData == null) {
            return "";
        }
        return barData.toString();
    }

    public void configBarData(Context context, List<BehaviorEvent> events, TextView label) {
        isDailyFormat = false;
        configBarDataWeeklyFormat(context, events, label);

        if (barData.size() < 3) {
            isDailyFormat = true;
            configBarDataDailyFormat(context, events, label);
        }
    }

    public void configBarDataDailyFormat(Context context, List<BehaviorEvent> events, TextView label) {
        Calendar prevCalendar = Calendar.getInstance();
        Calendar currCalendar = Calendar.getInstance();
        int currCount = 0, diffYears, diffDays;

        if (events == null || events.isEmpty()) return;

        prevCalendar.setTime(events.get(events.size() - 1).getOccurredAt());
        currCount++;
        for (int i = events.size() - 2; i >= 0; i--) {
            BehaviorEvent event = events.get(i);
            currCalendar.setTime(event.getOccurredAt());
            diffYears = currCalendar.get(Calendar.YEAR) - prevCalendar.get(Calendar.YEAR);
            diffDays = currCalendar.get(Calendar.DAY_OF_YEAR) - prevCalendar.get(Calendar.DAY_OF_YEAR);

            Log.e("BarGraph", "prevEvent: " + prevCalendar.toString());
            Log.e("BarGraph", "currEvent: " + currCalendar.toString());

            if (diffYears == 0 && diffDays == 0) {
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
        label.setText(events.get(0).getBehavior().getTitle() + " (last " + barData.size() + " days)");
    }

    public void configBarDataWeeklyFormat(Context context, List<BehaviorEvent> events, TextView label) {
        Calendar prevCalendar = Calendar.getInstance();
        Calendar currCalendar = Calendar.getInstance();
        int currCount = 0, diffYears, diffWeeks;

        if (events == null || events.isEmpty()) return;

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
    }
}
