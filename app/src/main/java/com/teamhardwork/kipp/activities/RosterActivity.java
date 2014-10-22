package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.fragments.RosterFragment;
import com.teamhardwork.kipp.models.users.Student;

import java.util.ArrayList;

public class RosterActivity extends Activity implements
        RosterFragment.OnStudentSelectedListener,
        RosterFragment.RosterSwipeListener {

    private final String ROSTER_FRAGMENT_TAG = "RosterFragment";

    private RosterFragment rosterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
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

    @Override
    public void toggleBehaviorFragment(boolean open, ArrayList<String> studentIds,
                                       String schoolClassId, boolean isPositive) {

    }
}
