package com.teamhardwork.kipp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.LeaderboardAdapter;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.utilities.student.StudentListFilterer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LeaderboardFragment extends BaseKippFragment implements Updatable {
    @InjectView(R.id.lvLeaderboard)
    ListView lvLeaderboard;
    private LeaderboardAdapter leaderboardAdapter;

    private AnimationAdapter mAnimAdapter;

    private OnStudentSelectedListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnStudentSelectedListener) {
            listener = (OnStudentSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement LeaderboardFragment.OnStudentSelectedListener");
        }
    }

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
        leaderboardAdapter = new LeaderboardAdapter(getActivity(), new ArrayList<Student>());
        mAnimAdapter = new AlphaInAnimationAdapter(leaderboardAdapter);
        mAnimAdapter.setAbsListView(lvLeaderboard);

        lvLeaderboard.setAdapter(mAnimAdapter);
        lvLeaderboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Student clicked = leaderboardAdapter.getItem(position);
                listener.onStudentSelected(clicked);
            }
        });
    }

    public void updateLeaderboard() {
        leaderboardAdapter.clear();
        currentClass.getClassRosterAsync(new FindCallback<Student>() {
            @Override
            public void done(List<Student> students, ParseException e) {
                Collections.sort(students, StudentListFilterer.pointsComparator);
                Collections.reverse(students);
                leaderboardAdapter.clear();
                leaderboardAdapter.addAll(students);
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void updateData() {
        updateLeaderboard();
    }

    @Override
    public String getTitle() {
        return "Leaderboard";
    }

    public interface OnStudentSelectedListener {
        public void onStudentSelected(Student student);
    }
}
