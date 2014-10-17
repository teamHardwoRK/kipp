package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Teacher;
import com.teamhardwork.kipp.queries.BehaviorRetriever;
import com.teamhardwork.kipp.utilities.behavior_event.BehaviorEventListFilterer;

import java.text.ParseException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StatsActivity extends Activity {
    @InjectView(R.id.pgTest) PieGraph pg;

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

    private void setupGoodBadChart(List<BehaviorEvent> good, List<BehaviorEvent> bad) {
        pg.setInnerCircleRatio(200);
        addSlice(Color.parseColor("#33CC00"), good.size(), "Good");
        addSlice(Color.parseColor("#FF3333"), bad.size(), "Bad");
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
