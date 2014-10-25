package com.teamhardwork.kipp.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.dialogfragments.AddActionDialogFragment;
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.LeaderboardFragment;
import com.teamhardwork.kipp.fragments.RosterFragment;
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.fragments.StudentStatsFragment;
import com.teamhardwork.kipp.listeners.FragmentTabListener;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class InfoActivity extends Activity implements  FeedFragment.FeedListener {
    private StatsFragment statsFragment;
    private Student selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        selected = getSelectedStudent();

        if (selected != null) {
            getActionBar().setTitle("Detail view for " + selected.getFullName());
        } else {
            getActionBar().setTitle("Info for class");
        }

        setupTabs();
    }

    private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        Bundle statBundle = new Bundle();
        Class statFragmentClass = StatsFragment.class;
        if (selected != null) {
            statBundle.putString(StudentStatsFragment.STUDENT_ID, selected.getObjectId());
            statFragmentClass = StudentStatsFragment.class;
        }

        ActionBar.Tab tab1 = actionBar
                .newTab()
                .setText("First")
                .setIcon(R.drawable.ic_kipp)
                .setTabListener(
                        new FragmentTabListener<StatsFragment>(R.id.flInfoFragmentContainer, this,
                                "first", statFragmentClass, statBundle));
        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);

        ActionBar.Tab tab2 = actionBar
                .newTab()
                .setText("Second")
                .setIcon(R.drawable.ic_kipp)
                .setTabListener(
                        new FragmentTabListener<FeedFragment>(R.id.flInfoFragmentContainer, this,
                                "second",
                                FeedFragment.class));
        actionBar.addTab(tab2);

        ActionBar.Tab tab3 = actionBar
                .newTab()
                .setText("Third")
                .setIcon(R.drawable.ic_kipp)
                .setTabListener(
                        new FragmentTabListener<LeaderboardFragment>(R.id.flInfoFragmentContainer,
                                this, "second", LeaderboardFragment.class));
        actionBar.addTab(tab3);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);
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

    private Student getSelectedStudent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return Student.getStudent(extras.getString("selected_student_id"));
        } else {
            return null;
        }
    }

    @Override
    public void addAction(BehaviorEvent event) {
        AddActionDialogFragment dialogFragment = AddActionDialogFragment.getInstance(event);

        dialogFragment.show(getFragmentManager(), "dialog_fragment_add_action");
    }
}
