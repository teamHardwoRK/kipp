package com.teamhardwork.kipp.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.BehaviorPagerAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BehaviorPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BehaviorPagerFragment extends Fragment implements BehaviorFragment.BehaviorListener {
    private static final String ARG_PARAM1 = "student_ids";
    private static final String ARG_PARAM2 = "school_class";
    private static final String ARG_PARAM3 = "isPositive";

    private ArrayList<String> studentIds;
    private String schoolClassId;
//    private List<Student> students;
//    private SchoolClass schoolClass;
    private boolean isPositive;

    private BehaviorPagerAdapter behaviorPagerAdapter;
    private ViewPager vpPager;

    /**
     * @param studentIds    Student Parse ID.
     * @param schoolClassId School Class Parse ID.
     * @param isPositive    positive or negative list
     * @return A new instance of fragment BehaviorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BehaviorPagerFragment newInstance(ArrayList<String> studentIds, String schoolClassId, boolean isPositive) {
        BehaviorPagerFragment fragment = new BehaviorPagerFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, studentIds);
        args.putString(ARG_PARAM2, schoolClassId);
        args.putBoolean(ARG_PARAM3, isPositive);
        fragment.setArguments(args);
        return fragment;
    }
    public BehaviorPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.studentIds = null;
        this.schoolClassId = null;
        this.isPositive = true;

        if (getArguments() != null) {
            this.studentIds = getArguments().getStringArrayList(ARG_PARAM1);
            this.schoolClassId = getArguments().getString(ARG_PARAM2);
            this.isPositive = getArguments().getBoolean(ARG_PARAM3);
        }

        if (this.studentIds == null || this.studentIds.isEmpty() || this.schoolClassId == null) {
            Toast.makeText(getActivity(), "cannot get studentIds or schoolClassId", Toast.LENGTH_SHORT).show();
            return;
        }

//        String studentId = studentIds.get(0);
//        Student.getStudentAsync(studentId, new GetCallback<Student>() {
//            @Override
//            public void done(Student foundStudent, ParseException e) {
//                if (e == null && foundStudent != null) {
//                    students.add(foundStudent);
//                }
//            }
//        });
//
//        SchoolClass.getSchoolClassAsync(schoolClassId, new GetCallback<SchoolClass>() {
//            @Override
//            public void done(SchoolClass foundSchoolClass, ParseException e) {
//                if (e == null && foundSchoolClass != null) {
//                    schoolClass = foundSchoolClass;
//                }
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_behavior_pager, container, false);

        vpPager = (ViewPager) v.findViewById(R.id.vpPager);
        behaviorPagerAdapter = new BehaviorPagerAdapter(getActivity(), getFragmentManager(), this, this.studentIds, this.schoolClassId, this.isPositive);
        vpPager.setAdapter(behaviorPagerAdapter);
        if (this.isPositive) {
            vpPager.setCurrentItem(0);
        } else {
            vpPager.setCurrentItem(1);
        }

        return v;
    }


    public void closeBehaviorFragment() {
        getFragmentManager().popBackStack();
    }
}
