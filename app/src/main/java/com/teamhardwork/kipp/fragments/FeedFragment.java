package com.teamhardwork.kipp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.BehaviorEventAdapter;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.KippUser;
import com.teamhardwork.kipp.models.users.Teacher;
import com.teamhardwork.kipp.queries.FeedQueries;

import java.util.List;

public class FeedFragment extends Fragment {
    KippUser user;
    ListView lvBehaviorFeed;

    public static FeedFragment getInstance(KippUser user) {
        FeedFragment fragment = new FeedFragment();
        fragment.user = user;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        lvBehaviorFeed = (ListView) view.findViewById(R.id.lvBehaviorFeed);

        SchoolClass schoolClass = ((KippApplication) getActivity().getApplication()).getSchoolClass();
        List<BehaviorEvent> events = null;

        try {
            events = FeedQueries.getClassFeed(schoolClass);
        } catch (ParseException e) {
            Toast.makeText(getActivity(), "Could not retrieve feed.", Toast.LENGTH_SHORT).show();
        }

        BehaviorEventAdapter adapter = new BehaviorEventAdapter(getActivity(), events);
        lvBehaviorFeed.setAdapter(adapter);

        return view;
    }
}
