package com.teamhardwork.kipp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.ActionEventAdapter;
import com.teamhardwork.kipp.models.Action;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ActionLogFragment extends BaseKippFragment implements Updatable {

    private static final String CURRENT_STUDENT_ID_KEY = "student_id";

    ActionEventAdapter actionAdapter;
    Student student;

    @InjectView(R.id.lvActionFeed)
    ListView lvActionFeed;

    public static ActionLogFragment newInstance(String currentStudentId) {
        Bundle args = new Bundle();
        args.putString(CURRENT_STUDENT_ID_KEY, currentStudentId);
        ActionLogFragment fragment = new ActionLogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_log, container, false);
        ButterKnife.inject(this, view);

        if (getArguments() != null && getArguments().containsKey(CURRENT_STUDENT_ID_KEY)) {
            String currentStudentId = getArguments().getString(CURRENT_STUDENT_ID_KEY);
            student = Student.getStudent(currentStudentId);
        }

        actionAdapter = new ActionEventAdapter(getActivity(), new ArrayList<Action>());
        lvActionFeed.setAdapter(actionAdapter);
        fillActionLog();
        return view;
    }

    private void fillActionLog() {
        FeedQueries.getStudentActionLog(student, new FindCallback<Action>() {
            @Override
            public void done(List<Action> actions, ParseException e) {
                actionAdapter.clear();
                actionAdapter.addAll(actions);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void updateData() {
        FindCallback<Action> actionLogCallback = new FindCallback<Action>() {
            @Override
            public void done(List<Action> actionList, ParseException e) {
                for (Action action : actionList) {
                    actionAdapter.insert(action, 0);
                }
            }
        };

        if (student != null) { // Check whether on create was called yet
            FeedQueries.getLatestActionLog(student, actionAdapter.getEventList(), actionLogCallback);
        }
    }

    @Override
    public String getTitle() {
        return "Actions";
    }
}
