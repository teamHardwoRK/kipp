package com.teamhardwork.kipp.fragments;

import android.app.Activity;
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
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.ActionEventAdapter;
import com.teamhardwork.kipp.adapters.BehaviorEventAdapter;
import com.teamhardwork.kipp.models.Action;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FeedFragment extends BaseKippFragment {
    private static final String CURRENT_STUDENT_ID_KEY = "student_id";

    FeedType feedType;
    Student student;

    @InjectView(R.id.lvBehaviorFeed)
    ListView lvBehaviorFeed;
    @InjectView(R.id.progressBar)
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

    public static FeedFragment newInstance(String currentStudentId) {
        Bundle args = new Bundle();
        args.putString(CURRENT_STUDENT_ID_KEY, currentStudentId);
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

        if (getArguments() != null && getArguments().containsKey(CURRENT_STUDENT_ID_KEY)) {
            String currentStudentId = getArguments().getString(CURRENT_STUDENT_ID_KEY);
            student = Student.getStudent(currentStudentId);
        }

        behaviorAdapter = new BehaviorEventAdapter(getActivity(), new ArrayList<BehaviorEvent>());
        lvBehaviorFeed.setAdapter(behaviorAdapter);
        actionAdapter = new ActionEventAdapter(getActivity(), new ArrayList<Action>());

        lvActionFeed.setAdapter(actionAdapter);
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
                for (BehaviorEvent event : eventList) {
                    behaviorAdapter.insert(event, 0);
                }
            }
        };

        FindCallback<Action> actionLogCallback = new FindCallback<Action>() {
            @Override
            public void done(List<Action> actionList, ParseException e) {
                for (Action action : actionList) {
                    actionAdapter.insert(action, 0);
                }
            }
        };

        switch (feedType) {
            case STUDENT:
                FeedQueries.getLatestStudentEvents(student, behaviorAdapter.getEventList(), callback);
                FeedQueries.getLatestActionLog(student, actionAdapter.getEventList(), actionLogCallback);
                break;
            case CLASS:
                FeedQueries.getLatestClassEvents(currentClass, behaviorAdapter.getEventList(), callback);
                break;
        }
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

    public void changeToClassFeed() {
        showProgressBar();

        lvActionFeed.setVisibility(View.GONE);

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
    public String getTitle() {
        return "Feed";
    }

    public enum FeedType {
        STUDENT,
        CLASS;
    }

    public interface FeedListener {
        public void addAction(BehaviorEvent event);
    }
}
