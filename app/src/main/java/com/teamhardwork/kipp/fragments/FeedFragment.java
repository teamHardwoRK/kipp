package com.teamhardwork.kipp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.users.KippUser;

public class FeedFragment extends Fragment {
    KippUser user;

    public static FeedFragment getInstance(KippUser user) {
        FeedFragment fragment = new FeedFragment();
        fragment.user = user;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        return view;
    }
}
