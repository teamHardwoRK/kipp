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
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.ActionEventAdapter;
import com.teamhardwork.kipp.adapters.BehaviorEventAdapter;
import com.teamhardwork.kipp.models.Action;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;

import java.util.ArrayList;
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

    @InjectView(R.id.lvActionFeed)
    ListView lvActionFeed;

    @InjectView(R.id.tvBehaviorFeedTitle)
    TextView tvBehaviorFeedTitle;

    @InjectView(R.id.tvActionFeedTitle)
    TextView tvActionFeedTitle;

    ActionEventAdapter actionAdapter;
    BehaviorEventAdapter behaviorAdapter;
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
                behaviorAdapter = new BehaviorEventAdapter(getActivity(), events);
                lvBehaviorFeed.setAdapter(behaviorAdapter);
                feedType = FeedType.CLASS;

                showBehaviorFeed();
                setupListeners();
            }
        });

        actionAdapter = new ActionEventAdapter(getActivity(), new ArrayList<Action>());
        lvActionFeed.setAdapter(actionAdapter);

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
        tvBehaviorFeedTitle.setVisibility(View.GONE);
        lvActionFeed.setVisibility(View.GONE);
        tvActionFeedTitle.setVisibility(View.GONE);
        pbBehaviorFeed.setVisibility(View.VISIBLE);
    }

    void showBehaviorFeed() {
        pbBehaviorFeed.setVisibility(View.GONE);
        tvBehaviorFeedTitle.setVisibility(View.VISIBLE);
        lvBehaviorFeed.setVisibility(View.VISIBLE);
    }

    void showActionFeed() {
        tvActionFeedTitle.setVisibility(View.VISIBLE);
        lvActionFeed.setVisibility(View.VISIBLE);
        pbBehaviorFeed.setVisibility(View.GONE);
    }

    public void updateData() {
        FindCallback<BehaviorEvent> callback = new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> eventList, ParseException e) {
                for(BehaviorEvent event : eventList) {
                    behaviorAdapter.insert(event, 0);
                }
            }
        };

        FindCallback<Action> actionLogCallback = new FindCallback<Action>() {
            @Override
            public void done(List<Action> actionList, ParseException e) {
                for(Action action : actionList) {
                    actionAdapter.insert(action, 0);
                }
            }
        };

        switch(feedType) {
           case STUDENT:
               FeedQueries.getLatestStudentEvents(student, behaviorAdapter.getEventList(), callback);
               FeedQueries.getLatestActionLog(student, actionAdapter.getEventList(), actionLogCallback);
               break;
           case CLASS:
               FeedQueries.getLatestClassEvents(schoolClass, behaviorAdapter.getEventList(), callback);
               break;
       }
    }

    public void changeToStudentFeed(Student student) {
        showProgressBar();
        this.student = student;

        FeedQueries.getStudentFeed(student, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                behaviorAdapter.clear();
                behaviorAdapter.addAll(behaviorEvents);
                feedType = FeedType.STUDENT;
                showBehaviorFeed();
            }
        });

        FeedQueries.getStudentActionLog(student, new FindCallback<Action>() {
            @Override
            public void done(List<Action> actions, ParseException e) {
                actionAdapter.clear();
                actionAdapter.addAll(actions);
                showBehaviorFeed();
                showActionFeed();
            }
        });
    }

    public void changeToClassFeed(SchoolClass schoolClass) {
        showProgressBar();
        this.schoolClass = schoolClass;

        lvActionFeed.setVisibility(View.GONE);

        FeedQueries.getClassFeed(schoolClass, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                behaviorAdapter.clear();
                behaviorAdapter.addAll(behaviorEvents);
                feedType = FeedType.CLASS;
                showBehaviorFeed();
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
