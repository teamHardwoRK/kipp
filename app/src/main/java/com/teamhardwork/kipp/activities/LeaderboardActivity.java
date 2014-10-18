package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.LeaderboardAdapter;
import com.teamhardwork.kipp.fragments.LeaderboardFragment;
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.BehaviorRetriever;
import com.teamhardwork.kipp.utilities.behavior_event.BehaviorEventListFilterer;
import com.teamhardwork.kipp.utilities.student.StudentListFilterer;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

// TODO: For testing purposes. Remove when done.
public class LeaderboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        getFragmentManager().beginTransaction()
                .replace(R.id.flLeaderboardContainer, new LeaderboardFragment())
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.leaderboard, menu);
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
}
