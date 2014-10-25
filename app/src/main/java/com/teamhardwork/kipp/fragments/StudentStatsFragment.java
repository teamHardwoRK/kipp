package com.teamhardwork.kipp.fragments;

import android.os.Bundle;

import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;

public class StudentStatsFragment extends StatsFragment {
    public static final String STUDENT_ID = "studentId";

    private Student student;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String studentId = getArguments().getString(STUDENT_ID);
        student = Student.getStudent(studentId);
        statForString = student.getFullName();
    }

    @Override
    protected void fillChartWithOverallData() {
        FeedQueries.getStudentFeed(student, overallResponseCallback);
    }
}
