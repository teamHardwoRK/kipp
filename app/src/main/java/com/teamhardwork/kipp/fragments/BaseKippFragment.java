package com.teamhardwork.kipp.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.models.SchoolClass;

public class BaseKippFragment extends Fragment {
    protected SchoolClass currentClass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentClass = ((KippApplication) getActivity().getApplication()).getSchoolClass();
    }
}
