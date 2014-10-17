package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import android.view.ViewGroup.LayoutParams;

public class StatsActivity extends Activity {
    @InjectView(R.id.pgTest)
    PieGraph pg;

    @InjectView(R.id.llLegend)
    LinearLayout llLegend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ButterKnife.inject(this);

        try {
            SchoolClass testClass = SchoolClass.findById("kNUWSLcaQZ");
            BehaviorRetriever.findBySchoolClass(testClass, new FindCallback<BehaviorEvent>() {
                @Override
                public void done(List<BehaviorEvent> behaviorEvents, com.parse.ParseException e) {
                    List<BehaviorEvent> good = BehaviorEventListFilterer.keepGood(behaviorEvents);
                    List<BehaviorEvent> bad = BehaviorEventListFilterer.keepBad(behaviorEvents);
                    setupGoodBadChart(good, bad);
                }
            });
        } catch (com.parse.ParseException e) {
            Toast.makeText(this, "Error in test data", Toast.LENGTH_SHORT).show();
        }
    }

    private static final int GOOD_COLOR_ID = Color.parseColor("#33CC00");
    private static final int BAD_COLOR_ID = Color.parseColor("#FF3333");

    private void setupGoodBadChart(List<BehaviorEvent> good, List<BehaviorEvent> bad) {
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
        LayoutParams lparams = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        TextView legendItem = new TextView(this);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
