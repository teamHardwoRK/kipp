package com.teamhardwork.kipp.activities;

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
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.fragments.StudentStatsFragment;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class InfoActivity extends Activity implements  FeedFragment.FeedListener {
    private StatsFragment statsFragment;
    private FeedFragment feedFragment;
    private LeaderboardFragment leaderboardFragment;
    private Student selected;

    @InjectView(R.id.btnStats)
    Button btnStats;
    @InjectView(R.id.btnFeed)
    Button btnFeed;
    @InjectView(R.id.btnLeaderboard)
    Button btnLeaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.inject(this);

        selected = getSelectedStudent();

        if (selected != null) {
            getActionBar().setTitle("Detail view for " + selected.getFullName());
        } else {
            getActionBar().setTitle("Info for class");
        }

        selectStatsModule();

        setupTabClickListeners();
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

    private void setupTabClickListeners() {
        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStatsModule();
            }
        });

        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFeed();
            }
        });

        btnLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLeaderboard();
            }
        });
    }

    private Student getSelectedStudent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return Student.getStudent(extras.getString("selected_student_id"));
        } else {
            return null;
        }
    }

    private void selectStatsModule() {
        if (statsFragment == null) {
            // TODO: Sorry for the hackiness this needs a detail Info activity subclass
            statsFragment = (selected != null) ?
                    StudentStatsFragment.newInstance(selected.getObjectId()) :
                    new StatsFragment();
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.flInfoFragmentContainer, statsFragment)
                .commit();
    }

    private void selectFeed() {
        if (feedFragment == null) {
            feedFragment = FeedFragment.getInstance();
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.flInfoFragmentContainer, feedFragment)
                .commit();

        if (selected != null) {
            //feedFragment.changeToStudentFeed(selected);
        }
    }

    private void selectLeaderboard() {
        if (leaderboardFragment == null) {
            leaderboardFragment = new LeaderboardFragment();
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.flInfoFragmentContainer, leaderboardFragment)
                .commit();
    }

    @Override
    public void addAction(BehaviorEvent event) {
        AddActionDialogFragment dialogFragment = AddActionDialogFragment.getInstance(event);

        dialogFragment.show(getFragmentManager(), "dialog_fragment_add_action");
    }
}
