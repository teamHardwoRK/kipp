package com.teamhardwork.kipp.fragments;

import android.os.Bundle;

import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;

public class StudentStatsFragment extends StatsFragment {

    private Student student;

    public static StudentStatsFragment newInstance(String studentId) {
        StudentStatsFragment studentStatsFragment = new StudentStatsFragment();
        Bundle args = new Bundle();
        args.putString("studentId", studentId);
        studentStatsFragment.setArguments(args);
        return studentStatsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String studentId = getArguments().getString("studentId");
        student = Student.getStudent(studentId);
        statForString = student.getFullName();
    }

    @Override
    protected void fillChartWithOverallData() {
        FeedQueries.getStudentFeed(student, overallResponseCallback);
    }
}
