package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.dialogfragments.AddActionDialogFragment;
import com.teamhardwork.kipp.fragments.BehaviorFragment;
import com.teamhardwork.kipp.fragments.BehaviorPagerFragment;
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.LeaderboardFragment;
import com.teamhardwork.kipp.fragments.RosterFragment;
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;

import java.util.ArrayList;

public class MainActivity extends Activity implements FeedFragment.FeedListener,
        RosterFragment.OnStudentSelectedListener,
        RosterFragment.RosterSwipeListener,
        BehaviorFragment.BehaviorListener {
    private final String FEED_FRAGMENT_TAG = "FeedFragment";
    private final String ROSTER_FRAGMENT_TAG = "RosterFragment";
    private final String STATS_FRAGMENT_TAG = "StatsFragment";
    private final String BEHAVIOR_PAGER_FRAGMENT_TAG = "BehaviorPagerFragment";
    Teacher teacher;
    private FeedFragment feedFragment;
    private RosterFragment rosterFragment;
    private StatsFragment statsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setHomeButtonEnabled(true);

        teacher = ((KippApplication) getApplication()).getTeacher();

        setupLeaderboard();
        setupStatsModule();
        createFragments();
    }

    private void createFragments() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        feedFragment = FeedFragment.getInstance(teacher, this);
        ft.add(R.id.flClassFeed, feedFragment, FEED_FRAGMENT_TAG);

        rosterFragment = RosterFragment.newInstance();
        ft.replace(R.id.flRoster, rosterFragment, ROSTER_FRAGMENT_TAG);

        ft.commit();
    }

    private void setupLeaderboard() {
        getFragmentManager().beginTransaction()
                .replace(R.id.flLeaderboardContainer, new LeaderboardFragment())
                .commit();
    }

    private void setupStatsModule() {
        statsFragment = new StatsFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.flStatsContainer, statsFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                ParseUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                this.finish();
                startActivity(intent);
                break;

            case android.R.id.home:
                onClassSelected();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addAction(BehaviorEvent event) {
        AddActionDialogFragment dialogFragment = AddActionDialogFragment.getInstance(event);

        dialogFragment.show(getFragmentManager(), "dialog_fragment_add_action");
    }
    
    @Override
    public void onStudentSelected(Student student) {
        getActionBar().setTitle("Detail view for " + student.getFullName());
        feedFragment.changeToStudentFeed(student);
        statsFragment.updateChartForStudent(student);
        Toast.makeText(this, student.getFirstName() + " selected", Toast.LENGTH_SHORT).show();
    }

    private void onClassSelected() {
        getActionBar().setTitle("Math 101 - Bob Loblaw"); // Hard coded for now
        statsFragment.updateChartForClass();
    }

    public void toggleBehaviorFragment(boolean open, ArrayList<String> studentIds, String schoolClassId, boolean isPositive) {
        if (open == true) {
            showBehaviorPagerFragment(studentIds, schoolClassId, isPositive);
        } else {
            closeBehaviorPagerFragment();
        }
    }

    public void showBehaviorPagerFragment(ArrayList<String> studentIds, String schoolClassId, boolean isPositive) {
        FragmentManager fm = getFragmentManager();
        Fragment feedFragment = fm.findFragmentByTag(FEED_FRAGMENT_TAG);
        Fragment pagerFragment = fm.findFragmentByTag(BEHAVIOR_PAGER_FRAGMENT_TAG);

        FragmentTransaction ft = fm.beginTransaction();

        // hide feed Fragment and show behavior Fragment
        if (feedFragment != null) {
            ft.hide(feedFragment);
        }

        if (pagerFragment == null) {
            pagerFragment = BehaviorPagerFragment.newInstance(studentIds, schoolClassId, isPositive);
            ft.add(R.id.flClassFeed, pagerFragment, BEHAVIOR_PAGER_FRAGMENT_TAG);
        } else {
            ft.detach(pagerFragment);

            ((BehaviorPagerFragment)pagerFragment).reset(studentIds, schoolClassId, isPositive);

            ft.attach(pagerFragment);
            ft.show(pagerFragment);
        }

        ft.commit();
    }

    public void closeBehaviorPagerFragment() {
        FragmentManager fm = getFragmentManager();
        Fragment feedFragment = fm.findFragmentByTag(FEED_FRAGMENT_TAG);
        Fragment rosterFragment = fm.findFragmentByTag(ROSTER_FRAGMENT_TAG);
        Fragment pagerFragment = fm.findFragmentByTag(BEHAVIOR_PAGER_FRAGMENT_TAG);

        // close behavior fragment and show feed Fragment
        FragmentTransaction ft = fm.beginTransaction();

        if (pagerFragment != null) {
            ft.detach(pagerFragment);
        }

        if (rosterFragment != null) {
            ((RosterFragment)rosterFragment).reset();
        }

        if (feedFragment != null) {
            ft.show(feedFragment);
        }

        ft.commit();
    }
}
