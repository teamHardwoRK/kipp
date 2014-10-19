package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseUser;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.dialogfragments.AddActionDialogFragment;
import com.teamhardwork.kipp.fragments.BehaviorPagerFragment;
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.RosterFragment;
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements FeedFragment.FeedListener, RosterFragment.RosterSwipeListener {
    Teacher teacher;
    private FeedFragment feedFragment;
    private RosterFragment rosterFragment;
    private BehaviorPagerFragment pagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        teacher = ((KippApplication) getApplication()).getTeacher();

        setupMockLeaderboard();
        setupStatsModule();
        createFragments();
    }

    private void createFragments() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        feedFragment = FeedFragment.getInstance(teacher, this);
        ft.add(R.id.flClassFeed, feedFragment);

        rosterFragment = RosterFragment.newInstance(this);
        ft.replace(R.id.flRoster, rosterFragment);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_logout:
                ParseUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                this.finish();
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addAction(BehaviorEvent event) {
        AddActionDialogFragment dialogFragment = AddActionDialogFragment.getInstance(event);

        dialogFragment.show(getFragmentManager(), "dialog_fragment_add_action");
    }

    public void showBehaviorFragment(List<Student> students, SchoolClass schoolClass, boolean isPositive) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ArrayList<String> studentIds = new ArrayList<String>();
        for (int i = 0; i < students.size(); i++) {
            studentIds.add(students.get(i).getObjectId());
        }
        pagerFragment = BehaviorPagerFragment.newInstance(studentIds, schoolClass.getObjectId(), isPositive);
        ft.replace(R.id.flClassFeed, pagerFragment);
        ft.addToBackStack(null);

        ft.commit();
    }
}
