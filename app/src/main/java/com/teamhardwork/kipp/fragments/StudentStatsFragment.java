package com.teamhardwork.kipp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.StudentArrayAdapter;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;
import com.teamhardwork.kipp.utilities.Recommendation;

import java.util.List;

public class StudentStatsFragment extends StatsFragment {
    public static final String TAG = "student_stats_fragment";
    public static final String STUDENT_ID_ARG_KEY = "student_id";
    public static final int btnDismissGoodColor = Color.parseColor("#3BB9FF");

    private Student student;

    public static StudentStatsFragment newInstance(String studentId) {
        Bundle args = new Bundle();
        args.putString(STUDENT_ID_ARG_KEY, studentId);
        StudentStatsFragment studentStatsFragment = new StudentStatsFragment();
        studentStatsFragment.setArguments(args);
        return studentStatsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String studentId = getArguments().getString(STUDENT_ID_ARG_KEY);
        student = Student.getStudent(studentId);
        statForString = student.getFullName();
    }

    @Override
    protected void fillChartWithOverallData() {
        FeedQueries.getStudentFeed(student, overallResponseCallback);
    }

    @Override
    protected void setRecommendation(List<BehaviorEvent> behaviorEvents) {
        boolean recommendationVisible = rlRecommendationContainer.getVisibility() == View.VISIBLE;
        if (Recommendation.getInstance().hasRecs(student) && !recommendationVisible) {
            btnDismissRecommendation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Recommendation.getInstance().dismissRecs(student);
                    hideRecommendation();
                }
            });

            Recommendation.RecommendationData rec = Recommendation.getInstance().getRecs(student);
            tvRecommendation.setText(student.getFirstName() + rec.toString());
            if (rec.getRecType() == Recommendation.RecommendationType.BAD) {
                rlRecommendationContainer.setBackgroundColor(StudentArrayAdapter.warningColor);
                btnDismissRecommendation.setBackgroundResource(R.drawable.btn_dismiss_warning_selector);
            } else {
                rlRecommendationContainer.setBackgroundColor(StudentArrayAdapter.infoColor);
                btnDismissRecommendation.setBackgroundResource(R.drawable.btn_dismiss_info_selector);
            }

            showRecommendation();
        }
    }

    private void showRecommendation() {
        Animation enter = AnimationUtils.loadAnimation(getActivity(), R.anim.top_in);
        rlRecommendationContainer.setVisibility(View.VISIBLE);
        enter.setStartOffset(1600);
        rlRecommendationContainer.setAnimation(enter);
    }

    private void hideRecommendation() {
        Animation exit = AnimationUtils.loadAnimation(getActivity(), R.anim.top_out);
        exit.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlRecommendationContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rlRecommendationContainer.startAnimation(exit);
    }
}
