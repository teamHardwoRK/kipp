package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.fragments.BehaviorFragment;
import com.teamhardwork.kipp.fragments.BehaviorPagerFragment;
import com.teamhardwork.kipp.fragments.RosterFragment;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;

import java.util.ArrayList;

public class RosterActivity extends Activity implements
        RosterFragment.OnStudentSelectedListener,
        RosterFragment.RosterSwipeListener,
        BehaviorFragment.BehaviorListener {

    private final String ROSTER_FRAGMENT_TAG = "RosterFragment";
    private final String BEHAVIOR_PAGER_FRAGMENT_TAG = "BehaviorPagerFragment";

    private RosterFragment rosterFragment;
    private Fragment pagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);

        SchoolClass schoolClass = ((KippApplication) getApplication()).getSchoolClass();

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(schoolClass.getName());

        setupRoster();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.roster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, InfoActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRoster() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        rosterFragment = RosterFragment.newInstance();
        ft.replace(R.id.flRoster, rosterFragment, ROSTER_FRAGMENT_TAG);
        ft.commit();
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
        Toast.makeText(this, student.getFirstName() + " selected", Toast.LENGTH_SHORT).show();
    }

    private void gotoInfoActivity(Student student) {
        Intent i = new Intent(this, InfoActivity.class);
        i.putExtra("selected_student_id", student.getObjectId());
        startActivity(i);
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
        pagerFragment = fm.findFragmentByTag(BEHAVIOR_PAGER_FRAGMENT_TAG);

        // show behavior Fragment
        if (pagerFragment == null) {
            pagerFragment = BehaviorPagerFragment.newInstance(studentIds, schoolClassId, isPositive);
        } else {
            FragmentTransaction ft = fm.beginTransaction();
            ft.detach(pagerFragment);

            ((BehaviorPagerFragment) pagerFragment).reset(studentIds, schoolClassId, isPositive);

            ft.attach(pagerFragment);
            ft.commit();
        }

        ((BehaviorPagerFragment) pagerFragment).show(fm, BEHAVIOR_PAGER_FRAGMENT_TAG);
    }

    public void closeBehaviorPagerFragment() {
        FragmentManager fm = getFragmentManager();
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

        ft.commit();
    }
}
