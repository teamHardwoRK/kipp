package com.teamhardwork.kipp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.BehaviorEventAdapter;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class FeedFragment extends BaseKippFragment implements Updatable {
    public static final String TAG = "behavior_log_fragment";
    public static final String STUDENT_ID_ARG_KEY = "student_id";

    FeedType feedType;
    Student student;

    @InjectView(R.id.lvBehaviorFeed)
    StickyListHeadersListView lvBehaviorFeed;

    BehaviorEventAdapter behaviorAdapter;
    FeedListener listener;

    public static FeedFragment newInstance(String currentStudentId) {
        Bundle args = new Bundle();
        args.putString(STUDENT_ID_ARG_KEY, currentStudentId);
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(args);
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

        boolean classMode = true;
        if (getArguments() != null && getArguments().containsKey(STUDENT_ID_ARG_KEY)) {
            String currentStudentId = getArguments().getString(STUDENT_ID_ARG_KEY);
            student = Student.getStudent(currentStudentId);
            classMode = false;
        }

        behaviorAdapter = new BehaviorEventAdapter(getActivity(), new ArrayList<BehaviorEvent>(),
                classMode);
        lvBehaviorFeed.setAdapter(behaviorAdapter);

        setupListeners();

        if (student != null) {
            changeToStudentFeed();
        } else {
            changeToClassFeed();
        }

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
        progressBar.setVisibility(View.VISIBLE);
    }

    void showBehaviorFeed() {
        progressBar.setVisibility(View.GONE);
        lvBehaviorFeed.setVisibility(View.VISIBLE);
    }

    public void changeToStudentFeed() {
        showProgressBar();

        FeedQueries.getStudentFeed(student, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                behaviorAdapter.clear();
                behaviorAdapter.addAll(behaviorEvents);
                feedType = FeedType.STUDENT;
                showBehaviorFeed();
            }
        });
    }

    public void changeToClassFeed() {
        showProgressBar();

        FeedQueries.getClassFeed(currentClass, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                behaviorAdapter.clear();
                behaviorAdapter.addAll(behaviorEvents);
                feedType = FeedType.CLASS;
                showBehaviorFeed();
            }
        });
    }

    @Override
    public void updateData() {
        FindCallback<BehaviorEvent> callback = new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> eventList, ParseException e) {
                for (BehaviorEvent event : eventList) {
                    behaviorAdapter.insert(event, 0);
                }
            }
        };

        switch (feedType) {
            case STUDENT:
                FeedQueries.getLatestStudentEvents(student, behaviorAdapter.getEventList(), callback);
                break;
            case CLASS:
                FeedQueries.getLatestClassEvents(currentClass, behaviorAdapter.getEventList(), callback);
                break;
        }
    }

    @Override
    public String getTitle() {
        return "Behaviors";
    }

    public enum FeedType {
        STUDENT,
        CLASS;
    }

    public interface FeedListener {
        public void addAction(BehaviorEvent event);
    }
}
