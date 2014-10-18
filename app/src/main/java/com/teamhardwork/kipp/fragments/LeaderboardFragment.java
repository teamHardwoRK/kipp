package com.teamhardwork.kipp.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.LeaderboardAdapter;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.utilities.student.StudentListFilterer;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LeaderboardFragment extends Fragment {
    @InjectView(R.id.lvLeaderboard)
    ListView lvLeaderboard;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rtnView = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        ButterKnife.inject(this, rtnView);

        setupLeaderboard();

        return rtnView;
    }

    private void setupLeaderboard() {
        try {
            SchoolClass testClass = SchoolClass.findById("d1RmWrazIm");
            List<Student> entireRoster = testClass.getRoster();
            List<Student> top5 = StudentListFilterer.keepTopNDescending(entireRoster, 5);
            LeaderboardAdapter leaderboardAdapter = new LeaderboardAdapter(getActivity(), top5);

            lvLeaderboard.setAdapter(leaderboardAdapter);

        } catch (com.parse.ParseException e) {
            Toast.makeText(getActivity(), "Error in test data", Toast.LENGTH_SHORT).show();
        }
    }


}
