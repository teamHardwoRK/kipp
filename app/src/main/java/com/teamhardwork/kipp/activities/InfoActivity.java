package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.InfoPagerAdapter;
import com.teamhardwork.kipp.dialogfragments.AddActionDialogFragment;
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class InfoActivity extends Activity implements FeedFragment.FeedListener {
    @InjectView(R.id.vpPager)
    ViewPager vpPager;
    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    private StatsFragment statsFragment;
    private Student selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.inject(this);
        setActionBarTitle();

        String selectedId = (selected != null) ? selected.getObjectId() : null;

        FragmentPagerAdapter pagerAdapter = new InfoPagerAdapter(getFragmentManager(), selectedId);
        vpPager.setAdapter(pagerAdapter);
        tabs.setViewPager(vpPager);
    }

    private void setActionBarTitle() {
        selected = getSelectedStudent();
        if (selected != null) {
            getActionBar().setTitle("Detail view for " + selected.getFullName());
        } else {
            getActionBar().setTitle("Info for class");
        }
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
