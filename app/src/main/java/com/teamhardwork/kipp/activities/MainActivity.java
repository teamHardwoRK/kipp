package com.teamhardwork.kipp.activities;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.RosterFragment;
import com.teamhardwork.kipp.models.users.Teacher;

import java.util.ArrayList;

public class MainActivity extends Activity {
    ParseUser currentUser;
    Teacher teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = ParseUser.getCurrentUser();

//        setupMockRoster();
//        setupMockLeaderboard();
//        setupMockStatsModule();
//        populateFeed();

        // Construct a fragment transaction
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        // Make changes to fragments
        ft.replace(R.id.flRoster, new RosterFragment());
        // Commit the fragment transaction
        ft.addToBackStack(null);
        ft.commit();
    }

    private void populateFeed() {
        Teacher.findTeacher(currentUser, new GetCallback<Teacher>() {
            @Override
            public void done(Teacher teacher, ParseException e) {
                MainActivity.this.teacher = teacher;
                FeedFragment fragment = FeedFragment.getInstance(teacher);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.flClassFeed, fragment);
                transaction.commit();
            }
        });
    }

    private void setupMockRoster() {
        ArrayList<String> roster = new ArrayList<String>();
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roster);
        itemsAdapter.add("Class Roster");
        itemsAdapter.add("Bruce Wayne");
        itemsAdapter.add("Clark Kent");
        itemsAdapter.add("John Doe");
        itemsAdapter.add("Jane Doe");
//        ListView lvRoster = (ListView) findViewById(R.id.lvLeaderBoard);
//        lvRoster.setAdapter(itemsAdapter);
    }

    private void setupMockLeaderboard() {
        ArrayList<String> leaderBoard = new ArrayList<String>();
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, leaderBoard);
        itemsAdapter.add("Top Students:");
        itemsAdapter.add("Bruce Wayne");
        itemsAdapter.add("John Doe");
        itemsAdapter.add("Worst Students:");
        itemsAdapter.add("Jane Doe");
//        ListView lvLeaderboard = (ListView) findViewById(R.id.lvLeaderBoard);
//        lvLeaderboard.setAdapter(itemsAdapter);
    }

    private void setupMockStatsModule(){
//        PieGraph pg = (PieGraph)findViewById(R.id.pgTest);
//        PieSlice slice = new PieSlice();
//        slice.setColor(Color.parseColor("#99CC00"));
//        slice.setValue(2);
//        slice.setTitle("I'm a good boy");
//        pg.addSlice(slice);
//        slice = new PieSlice();
//        slice.setColor(Color.parseColor("#FFBB33"));
//        slice.setValue(3);
//        slice.setTitle("I'm a bad boy");
//        pg.addSlice(slice);
//        slice = new PieSlice();
//        slice.setColor(Color.parseColor("#AA66CC"));
//        slice.setValue(8);
//        slice.setTitle("Awkward silence");
//        pg.addSlice(slice);
    }
}
