package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.StudentArrayAdapter;
import com.teamhardwork.kipp.dialogfragments.AddActionDialogFragment;
import com.teamhardwork.kipp.fragments.BehaviorFragment;
import com.teamhardwork.kipp.fragments.BehaviorPagerFragment;
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.LeaderboardFragment;
import com.teamhardwork.kipp.fragments.RosterFragment;
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;
import com.teamhardwork.kipp.receivers.KippPushBroadcastReceiver;

import java.util.ArrayList;

public class MainActivity extends Activity implements
        BehaviorFragment.BehaviorListener,
        FeedFragment.FeedListener,
        RosterFragment.OnStudentSelectedListener,
        RosterFragment.RosterSwipeListener,
        StudentArrayAdapter.StudentAdapterListener {
    private final String FEED_FRAGMENT_TAG = "FeedFragment";
    private final String ROSTER_FRAGMENT_TAG = "RosterFragment";
    private final String STATS_FRAGMENT_TAG = "StatsFragment";
    private final String BEHAVIOR_PAGER_FRAGMENT_TAG = "BehaviorPagerFragment";

    private SchoolClass schoolClass;
    private Teacher teacher;
    private FeedFragment feedFragment;
    private RosterFragment rosterFragment;
    private StatsFragment statsFragment;
    private KippPushBroadcastReceiver pushReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KippApplication application = (KippApplication) getApplication();
        teacher = application.getTeacher();
        schoolClass = application.getSchoolClass();

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(schoolClass.getName());

        setupLeaderboard();
        setupStatsModule();
        createFragments();
    }

    private void createFragments() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        feedFragment = FeedFragment.getInstance();
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
    protected void onResume() {
        super.onResume();

        pushReceiver = new KippPushBroadcastReceiver(new KippPushBroadcastReceiver.Callback() {
            @Override
            public void onPushReceive() {
                feedFragment.updateData();
                statsFragment.updateData();
            }
        });
        IntentFilter receiveIntentFilter = new IntentFilter();
        receiveIntentFilter.addAction("com.parse.push.intent.RECEIVE");
        receiveIntentFilter.addAction("com.parse.push.intent.DELETE");
        receiveIntentFilter.addAction("com.parse.push.intent.OPEN");

        registerReceiver(pushReceiver, receiveIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (pushReceiver != null) {
            unregisterReceiver(pushReceiver);
        }
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
    public void addAction(Student student) {
        AddActionDialogFragment dialogFragment = AddActionDialogFragment.getInstance(student);

        dialogFragment.show(getFragmentManager(), "dialog_fragment_add_action");
    }

    @Override
    public void onStudentSelected(Student student) {
        getActionBar().setTitle("Detail view for " + student.getFullName());
        feedFragment.changeToStudentFeed(student);
        statsFragment.updateChartForStudent(student);
        Fragment rosterFragment = getFragmentManager().findFragmentByTag(ROSTER_FRAGMENT_TAG);

        // HACK: reset roster Fragment to get around swiping issue
        if (rosterFragment != null) {
            getFragmentManager().beginTransaction().detach(rosterFragment).attach(rosterFragment).commit();
        }
        Toast.makeText(this, student.getFirstName() + " selected", Toast.LENGTH_SHORT).show();
    }

    private void onClassSelected() {
        getActionBar().setTitle(schoolClass.getName());
        feedFragment.changeToClassFeed(schoolClass);
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

            ((BehaviorPagerFragment) pagerFragment).reset(studentIds, schoolClassId, isPositive);

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
            ((RosterFragment) rosterFragment).reset();
        }

        if (feedFragment != null) {
            ft.show(feedFragment);
        }

        ft.commit();
    }
}
