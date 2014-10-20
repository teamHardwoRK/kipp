package com.teamhardwork.kipp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.LeaderboardAdapter;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.utilities.student.StudentListFilterer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LeaderboardFragment extends BaseKippFragment {
    @InjectView(R.id.lvLeaderboard)
    ListView lvLeaderboard;
    private LeaderboardAdapter leaderboardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rtnView = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        ButterKnife.inject(this, rtnView);

        setupLeaderboard();
        updateLeaderboard();

        return rtnView;
    }

    private void setupLeaderboard() {
        List<Student> leaderboard = new ArrayList<Student>();
        leaderboardAdapter = new LeaderboardAdapter(getActivity(), leaderboard);

        lvLeaderboard.setAdapter(leaderboardAdapter);
    }

    public void updateLeaderboard() {
        leaderboardAdapter.clear();
        List<Student> leaderboard = currentClass.getRoster();
        Collections.sort(leaderboard, StudentListFilterer.pointsComparator);
        Collections.reverse(leaderboard);
        leaderboardAdapter.addAll(leaderboard);
    }
}
