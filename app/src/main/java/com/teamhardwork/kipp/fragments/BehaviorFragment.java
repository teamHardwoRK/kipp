package com.teamhardwork.kipp.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.enums.BehaviorCategory;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BehaviorFragment extends Fragment {
    private static final String ARG_PARAM1 = "student_ids";
    private static final String ARG_PARAM2 = "school_class";
    private static final String ARG_PARAM3 = "isPositive";

    private ArrayList<String> studentIds;
    private String schoolClassId;
    private List<Student> students;
    private SchoolClass schoolClass;
    private String notes;
    private String behavior;
    private String occurredAt;
    private List<Behavior> behaviors;
    private boolean isPositive;
    private ArrayAdapter<Behavior> behaviorsAdapter;
    private ListView lvBehaviors;
    private BehaviorListener listener;


    public BehaviorFragment() {
        // Required empty public constructor
    }

    public static BehaviorFragment newInstance(ArrayList<String> studentIds, String schoolClassId, boolean isPositive) {
        BehaviorFragment fragment = new BehaviorFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, studentIds);
        args.putString(ARG_PARAM2, schoolClassId);
        args.putBoolean(ARG_PARAM3, isPositive);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.studentIds = null;
        this.schoolClassId = null;
        this.isPositive = true;
        this.students = new ArrayList<Student>();

        if (getArguments() != null) {
            this.studentIds = getArguments().getStringArrayList(ARG_PARAM1);
            this.schoolClassId = getArguments().getString(ARG_PARAM2);
            isPositive = getArguments().getBoolean(ARG_PARAM3);
        }

        if (this.studentIds == null || this.schoolClassId == null) {
            Toast.makeText(getActivity(), "cannot get studentIds or schoolClassId", Toast.LENGTH_SHORT).show();
            return;
        }

        SchoolClass.getSchoolClassAsync(schoolClassId, new GetCallback<SchoolClass>() {
            @Override
            public void done(SchoolClass foundSchoolClass, ParseException e) {
                if (e == null && foundSchoolClass != null) {
                    schoolClass = foundSchoolClass;
                }
            }
        });

        for (int i = 0; i < studentIds.size(); i++) {
            Student.getStudentAsync(studentIds.get(i), new GetCallback<Student>() {
                @Override
                public void done(Student foundStudent, ParseException e) {
                    if (e == null && foundStudent != null) {
                        students.add(foundStudent);
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_behavior, container, false);

        lvBehaviors = (ListView) v.findViewById(R.id.lvBehaviors);

        behaviors = new ArrayList<Behavior>();
        for (Behavior b : Behavior.values()) {
            if (isPositive == true) {
                if (b.getCategory() != BehaviorCategory.FALL && b.getCategory() != BehaviorCategory.SLIP) {
                    behaviors.add(b);
                }
            } else {
                if (b.getCategory() == BehaviorCategory.FALL || b.getCategory() == BehaviorCategory.SLIP) {
                    behaviors.add(b);
                }
            }
        }

        behaviorsAdapter =
                new ArrayAdapter<Behavior>(getActivity(), android.R.layout.simple_list_item_1, behaviors);

        lvBehaviors.setAdapter(behaviorsAdapter);
        lvBehaviors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long mylng) {
                for (Student curStudent: students) {
                    BehaviorEvent behaviorEvent = new BehaviorEvent();
                    behaviorEvent.setBehavior(behaviors.get(position));
                    behaviorEvent.setSchoolClass(schoolClass);
                    behaviorEvent.setStudent(curStudent);
                    behaviorEvent.setOccurredAt(new Date());
                    behaviorEvent.setNotes("");
                    behaviorEvent.saveInBackground();
                    Toast.makeText(getActivity(), "behaviorEvent saved for " + curStudent.getFirstName(), Toast.LENGTH_SHORT).show();

                    int behaviorPoints = behaviorEvent.getBehavior().getPoints();
                    curStudent.addPoints(behaviorPoints);
                    curStudent.saveInBackground();
                    /*try {
                        behaviorEvent.save();
                        Toast.makeText(getActivity(), "behaviorEvent saved", Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*/
                }

                listener.closeBehaviorPagerFragment();
            }
        });

        return v;
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BehaviorListener) {
            listener = (BehaviorListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement BehaviorFragment.BehaviorListener");
        }
    }

    public interface BehaviorListener {
        public void closeBehaviorPagerFragment();
    }
}
