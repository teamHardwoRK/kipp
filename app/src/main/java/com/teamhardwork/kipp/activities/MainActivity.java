package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.RosterFragment;
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.models.users.Teacher;

import java.util.ArrayList;

public class MainActivity extends Activity {
    Teacher teacher;
    private FeedFragment feedFragment;
    private RosterFragment rosterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        teacher = ((KippApplication) getApplication()).getTeacher();

        setupMockLeaderboard();
        setupStatsModule();
        populateFeed();
    }

    private void populateFeed() {
        feedFragment = FeedFragment.getInstance(teacher);
        rosterFragment = new RosterFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.flClassFeed, feedFragment);
        ft.add(R.id.flRoster, rosterFragment);
        ft.commit();
    }

    private void setupMockLeaderboard() {
        ArrayList<String> leaderBoard = new ArrayList<String>();
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, leaderBoard);
        itemsAdapter.add("Top Students:");
        itemsAdapter.add("Bruce Wayne");
        itemsAdapter.add("John Doe");
        itemsAdapter.add("Worst Students:");
        itemsAdapter.add("Jane Doe");
        ListView lvLeaderboard = (ListView) findViewById(R.id.lvLeaderBoard);
        lvLeaderboard.setAdapter(itemsAdapter);
    }

    private void setupStatsModule(){
        getFragmentManager().beginTransaction()
                .replace(R.id.flStatsContainer, new StatsFragment())
                .commit();
    }
}
