package com.teamhardwork.kipp.fragments;

import android.support.v4.app.Fragment;
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
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public abstract class BaseKippFragment extends Fragment {
    protected SchoolClass currentClass;
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

    private float dpFromPx(float px) {
        return px / getActivity().getResources().getDisplayMetrics().density;
    }
}
