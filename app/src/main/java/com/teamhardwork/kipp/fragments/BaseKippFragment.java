package com.teamhardwork.kipp.fragments;

import android.app.Fragment;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.receivers.KippPushBroadcastReceiver;

import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class BaseKippFragment extends Fragment {
    protected SchoolClass currentClass;
    private KippPushBroadcastReceiver pushReceiver;

    @InjectView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentClass = ((KippApplication) getActivity().getApplication()).getSchoolClass();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rtnView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rtnView);
        return rtnView;
    }

    public String getTitle() {
        return "Default";
    }

    protected void updateData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        registerPushReceiver();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (pushReceiver != null) {
            getActivity().unregisterReceiver(pushReceiver);
        }
    }

    private void registerPushReceiver() {
        pushReceiver = new KippPushBroadcastReceiver(new KippPushBroadcastReceiver.Callback() {
            @Override
            public void onPushReceive() {
                updateData();
            }
        });

        IntentFilter receiveIntentFilter = new IntentFilter();
        receiveIntentFilter.addAction("com.parse.push.intent.RECEIVE");
        receiveIntentFilter.addAction("com.parse.push.intent.DELETE");
        receiveIntentFilter.addAction("com.parse.push.intent.OPEN");

        getActivity().registerReceiver(pushReceiver, receiveIntentFilter);
    }
}
