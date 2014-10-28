package com.teamhardwork.kipp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;
import com.teamhardwork.kipp.utilities.Recommendation;

import java.util.List;

public class StudentStatsFragment extends StatsFragment {
    public static final String STUDENT_ID = "studentId";

    private Student student;

    public static StudentStatsFragment newInstance(String studentId) {
        Bundle args = new Bundle();
        args.putString(STUDENT_ID, studentId);
        StudentStatsFragment studentStatsFragment = new StudentStatsFragment();
        studentStatsFragment.setArguments(args);
        return studentStatsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String studentId = getArguments().getString(STUDENT_ID);
        student = Student.getStudent(studentId);
        statForString = student.getFullName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        rlRecommendationContainer.setVisibility(View.GONE);
        tvRecommendation.setVisibility(View.GONE);
        btnDismissRecommendation.setVisibility(View.GONE);
        btnDismissRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recommendation.getInstance().dismissRecs(student);
                rlRecommendationContainer.setVisibility(View.GONE);
                tvRecommendation.setVisibility(View.GONE);
                btnDismissRecommendation.setVisibility(View.GONE);
            }
        });

        return v;
    }

    @Override
    protected void fillChartWithOverallData() {
        FeedQueries.getStudentFeed(student, overallResponseCallback);
    }

    @Override
    protected void setRecommendation(List<BehaviorEvent> behaviorEvents) {
        if (Recommendation.getInstance().hasRecs(student)) {
            tvRecommendation.setText(student.getFirstName() + Recommendation.getInstance().getRecs(student));
            rlRecommendationContainer.setVisibility(View.VISIBLE);
            tvRecommendation.setVisibility(View.VISIBLE);
            btnDismissRecommendation.setVisibility(View.VISIBLE);
        }
    }
}
