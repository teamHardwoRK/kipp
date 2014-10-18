package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.parse.FindCallback;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.queries.BehaviorRetriever;
import com.teamhardwork.kipp.utilities.behavior_event.BehaviorEventListFilterer;

import java.text.MessageFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import android.view.ViewGroup.LayoutParams;

// TODO: For testing purposes. Remove when done.
public class StatsActivity extends Activity {
    @InjectView(R.id.flStatsContainer)
    FrameLayout flStatsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ButterKnife.inject(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.flStatsContainer, new StatsFragment())
                .commit();
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
