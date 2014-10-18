package com.teamhardwork.kipp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.BehaviorEventAdapter;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.KippUser;
import com.teamhardwork.kipp.queries.FeedQueries;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FeedFragment extends Fragment {
    KippUser user;

    @InjectView(R.id.lvBehaviorFeed)
    ListView lvBehaviorFeed;

    @InjectView(R.id.pbBehaviorFeed)
    ProgressBar pbBehaviorFeed;

    public static FeedFragment getInstance(KippUser user) {
        FeedFragment fragment = new FeedFragment();
        fragment.user = user;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.inject(this, view);

        SchoolClass schoolClass = ((KippApplication) getActivity().getApplication()).getSchoolClass();

        try {
            FeedQueries.getClassFeed(schoolClass, new FindCallback<BehaviorEvent>() {
                @Override
                public void done(List<BehaviorEvent> events, ParseException e) {
                    BehaviorEventAdapter adapter = new BehaviorEventAdapter(getActivity(), events);
                    lvBehaviorFeed.setAdapter(adapter);

                    pbBehaviorFeed.setVisibility(View.GONE);
                    lvBehaviorFeed.setVisibility(View.VISIBLE);
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }
}
