package com.teamhardwork.kipp.activities;

import android.app.Activity;
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
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.LeaderboardFragment;
import com.teamhardwork.kipp.fragments.RosterFragment;
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;

public class MainActivity extends Activity implements FeedFragment.FeedListener,
        RosterFragment.OnStudentSelectedListener {
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
        ft.add(R.id.flClassFeed, feedFragment);

        rosterFragment = new RosterFragment();
        ft.add(R.id.flRoster, rosterFragment);

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
        statsFragment.setupGoodBadChartForStudent(student);
        Toast.makeText(this, student.getFirstName() + " selected", Toast.LENGTH_SHORT).show();
    }

    private void onClassSelected() {
        getActionBar().setTitle("Math 101 - Bob Loblaw"); // Hard coded for now
        statsFragment.setupGoodBadChartForClass();
    }
}
