package com.teamhardwork.kipp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.SchoolClass;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BaseKippFragment extends Fragment {
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
}
