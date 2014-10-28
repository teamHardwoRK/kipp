package com.teamhardwork.kipp.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.dialogfragments.AddActionDialogFragment;
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.fragments.BehaviorFragment;
import com.teamhardwork.kipp.fragments.BehaviorPagerFragment;
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.LeaderboardFragment;
import com.teamhardwork.kipp.fragments.RosterFragment;
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.graphics.SadFaceAnimationSet;
import com.teamhardwork.kipp.graphics.StarAnimationSet;
import com.teamhardwork.kipp.listeners.FragmentTabListener;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;

import java.util.ArrayList;
import java.util.Date;

public class RosterActivity extends BaseKippActivity implements
        RosterFragment.OnStudentSelectedListener,
        RosterFragment.RosterSwipeListener,
        BehaviorFragment.BehaviorListener,
        SadFaceAnimationSet.SadFaceAnimationSetListener,
        FeedFragment.FeedListener,
        StarAnimationSet.StarAnimationSetListener {

    private final String ROSTER_FRAGMENT_TAG = "RosterFragment";
    private final String BEHAVIOR_PAGER_FRAGMENT_TAG = "BehaviorPagerFragment";
    private Fragment pagerFragment;
    private ArrayList<Student> selectedStudents;
    private SchoolClass schoolClass;

    @Override
    public void addAction(BehaviorEvent event) {
        AddActionDialogFragment dialogFragment = AddActionDialogFragment.getInstance(event);
        dialogFragment.show(getFragmentManager(), "dialog_fragment_add_action");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);

        SchoolClass schoolClass = ((KippApplication) getApplication()).getSchoolClass();

        getActionBar().setTitle(schoolClass.getName());
        setupTabs();
    }

    private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        ActionBar.Tab tab1 = actionBar
                .newTab()
                .setText(R.string.roster_tab_title)
                .setTabListener(
                        new FragmentTabListener<RosterFragment>(R.id.flRoster, this, "roster",
                                RosterFragment.class));

        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);

        ActionBar.Tab tab2 = actionBar
                .newTab()
                .setText("Stats")
                .setTabListener(
                        new FragmentTabListener<StatsFragment>(R.id.flRoster, this, "stats",
                                StatsFragment.class));
        actionBar.addTab(tab2);

        ActionBar.Tab tab3 = actionBar
                .newTab()
                .setText("Log")
                .setTabListener(
                        new FragmentTabListener<FeedFragment>(R.id.flRoster, this, "Log",
                                FeedFragment.class));
        actionBar.addTab(tab3);

        ActionBar.Tab tab4 = actionBar
                .newTab()
                .setText("Rankings")
                .setTabListener(new FragmentTabListener<LeaderboardFragment>(R.id.flRoster, this,
                        "leaderboard",
                        LeaderboardFragment.class));
        actionBar.addTab(tab4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.roster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                ParseUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                exitActivity();
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStudentSelected(Student student) {
        Fragment rosterFragment = getFragmentManager().findFragmentByTag(ROSTER_FRAGMENT_TAG);

        // HACK: reset roster Fragment to get around swiping issue
        if (rosterFragment != null) {
            getFragmentManager().beginTransaction().detach(rosterFragment).attach(rosterFragment)
                    .commit();
        }

        gotoInfoActivity(student);
    }

    private void gotoInfoActivity(Student student) {
        Intent i = new Intent(this, InfoActivity.class);
        if (student != null) {
            i.putExtra("selected_student_id", student.getObjectId());
        }
        enterActivity(i);
    }

    public void showBehaviorPagerFragment(ArrayList<Student> students, SchoolClass schoolClass, boolean isPositive) {
        this.selectedStudents = students;
        this.schoolClass = schoolClass;

        FragmentManager fm = getFragmentManager();
        pagerFragment = fm.findFragmentByTag(BEHAVIOR_PAGER_FRAGMENT_TAG);

        // show behavior Fragment
        if (pagerFragment == null) {
            pagerFragment = BehaviorPagerFragment.newInstance(isPositive);
        }

        ((BehaviorPagerFragment) pagerFragment).show(fm, BEHAVIOR_PAGER_FRAGMENT_TAG);
    }

    @Override
    public void saveBehavior(Behavior behavior) {
        if (behavior == null) return;
        for (Student curStudent : selectedStudents) {
            BehaviorEvent behaviorEvent = new BehaviorEvent();
            behaviorEvent.setBehavior(behavior);
            behaviorEvent.setSchoolClass(schoolClass);
            behaviorEvent.setStudent(curStudent);
            behaviorEvent.setOccurredAt(new Date());
            behaviorEvent.setNotes("");
            behaviorEvent.saveInBackground();
            Toast.makeText(RosterActivity.this,
                    "behaviorEvent saved for " + curStudent.getFirstName(), Toast.LENGTH_SHORT)
                    .show();

            int behaviorPoints = behaviorEvent.getBehavior().getPoints();
            curStudent.addPoints(behaviorPoints);
            curStudent.saveInBackground();
        }
    }

    @Override
    public void onAnimationEnd() {
        FragmentManager fm = getFragmentManager();
        pagerFragment = fm.findFragmentByTag(BEHAVIOR_PAGER_FRAGMENT_TAG);

        if (pagerFragment != null) {
            // this will by default remove the fragment instance
            ((BehaviorPagerFragment) pagerFragment).dismiss();
        }
    }
}
