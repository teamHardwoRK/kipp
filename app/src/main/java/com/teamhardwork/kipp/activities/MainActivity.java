package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.app.Fragment;
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
import com.teamhardwork.kipp.dialogfragments.AddActionDialogFragment;
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.LeaderboardFragment;
import com.teamhardwork.kipp.fragments.RosterFragment;
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.receivers.KippPushBroadcastReceiver;

// TODO: currently being deconstructed. Will be killed soon. There might be useful code to copy from here in the mean time
public class MainActivity extends Activity implements
        FeedFragment.FeedListener {
    private final String FEED_FRAGMENT_TAG = "FeedFragment";
    private final String ROSTER_FRAGMENT_TAG = "RosterFragment";

    private SchoolClass schoolClass;
    private RosterFragment rosterFragment;
    private StatsFragment statsFragment;
    private LeaderboardFragment leaderboardFragment;
    private KippPushBroadcastReceiver pushReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KippApplication application = (KippApplication) getApplication();
        schoolClass = application.getSchoolClass();

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(schoolClass.getName());

        setupLeaderboard();
        setupStatsModule();
        createFragments();
    }

    private void createFragments() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();


        rosterFragment = RosterFragment.newInstance();
        ft.replace(R.id.flRoster, rosterFragment, ROSTER_FRAGMENT_TAG);

        ft.commit();
    }

    private void setupLeaderboard() {
        leaderboardFragment = new LeaderboardFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.flLeaderboardContainer, leaderboardFragment)
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
                statsFragment.updateData();
                leaderboardFragment.updateLeaderboard();

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

    public void addAction(Student student) {
        AddActionDialogFragment dialogFragment = AddActionDialogFragment.getInstance(student);

        dialogFragment.show(getFragmentManager(), "dialog_fragment_add_action");
    }

    public void onStudentSelected(Student student) {
        getActionBar().setTitle("Detail view for " + student.getFullName());
        Fragment rosterFragment = getFragmentManager().findFragmentByTag(ROSTER_FRAGMENT_TAG);

        // HACK: reset roster Fragment to get around swiping issue
        if (rosterFragment != null) {
            getFragmentManager().beginTransaction().detach(rosterFragment).attach(rosterFragment).commit();
        }
        Toast.makeText(this, student.getFirstName() + " selected", Toast.LENGTH_SHORT).show();
    }

    private void onClassSelected() {
        getActionBar().setTitle(schoolClass.getName());
        //statsFragment.updateChartForClass();
        leaderboardFragment.resetSelectedRow();
    }

}
