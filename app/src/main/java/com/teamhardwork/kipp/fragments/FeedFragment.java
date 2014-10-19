package com.teamhardwork.kipp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.BehaviorEventAdapter;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FeedFragment extends Fragment {
    FeedType feedType;
    SchoolClass schoolClass;
    Student student;

    @InjectView(R.id.lvBehaviorFeed)
    ListView lvBehaviorFeed;

    @InjectView(R.id.pbBehaviorFeed)
    ProgressBar pbBehaviorFeed;

    BehaviorEventAdapter adapter;
    FeedListener listener;

    public static FeedFragment getInstance() {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.listener = (FeedListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.inject(this, view);

        schoolClass = ((KippApplication) getActivity().getApplication()).getSchoolClass();

        FeedQueries.getClassFeed(schoolClass, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> events, ParseException e) {
                adapter = new BehaviorEventAdapter(getActivity(), events);
                lvBehaviorFeed.setAdapter(adapter);
                feedType = FeedType.CLASS;

                hideProgressBar();
                setupListeners();
            }
        });
        return view;
    }

    void setupListeners() {
        lvBehaviorFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BehaviorEvent event = (BehaviorEvent) lvBehaviorFeed.getItemAtPosition(position);
                listener.addAction(event);
            }
        });
    }

    void showProgressBar() {
        lvBehaviorFeed.setVisibility(View.GONE);
        pbBehaviorFeed.setVisibility(View.VISIBLE);
    }

    void hideProgressBar() {
        pbBehaviorFeed.setVisibility(View.GONE);
        lvBehaviorFeed.setVisibility(View.VISIBLE);
    }

    public void updateData() {
        FindCallback<BehaviorEvent> callback = new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> eventList, ParseException e) {
                for(BehaviorEvent event : eventList) {
                    adapter.insert(event, 0);
                }
            }
        };

        switch(feedType) {
           case STUDENT:
               FeedQueries.getLatestStudentEvents(student, adapter.getEventList(), callback);
               break;
           case CLASS:
               FeedQueries.getLatestClassEvents(schoolClass, adapter.getEventList(), callback);
               break;
       }
    }

    public void changeToStudentFeed(Student student) {
        showProgressBar();
        this.student = student;

        FeedQueries.getStudentFeed(student, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                adapter.clear();
                adapter.addAll(behaviorEvents);
                feedType = FeedType.STUDENT;
                hideProgressBar();
            }
        });
    }

    public void changeToClassFeed(SchoolClass schoolClass) {
        showProgressBar();
        this.schoolClass = schoolClass;

        FeedQueries.getClassFeed(schoolClass, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                adapter.clear();
                adapter.addAll(behaviorEvents);
                feedType = FeedType.CLASS;
                hideProgressBar();
            }
        });
    }

    public interface FeedListener {
        public void addAction(BehaviorEvent event);
    }

    public enum FeedType {
        STUDENT,
        CLASS;
    }
}
