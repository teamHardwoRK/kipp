package com.teamhardwork.kipp.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.SwipeListViewListener;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.StudentArrayAdapter;
import com.teamhardwork.kipp.enums.Gender;
import com.teamhardwork.kipp.models.users.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RosterFragment extends Fragment {
    private SwipeListView lvStudents;
    private List<Student> students;
    StudentArrayAdapter aStudents;


    public RosterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        students = new ArrayList<Student>();

        Student a = new Student();
        a.setFirstName("Bruce");
        a.setLastName("Wayne");
        a.setGender(Gender.MALE);
        students.add(a);

        Student b = new Student();
        b.setFirstName("Jackie");
        b.setLastName("Chan");
        b.setGender(Gender.MALE);
        students.add(b);

        Student c = new Student();
        c.setFirstName("Jennifer");
        c.setLastName("Lawrence");
        c.setGender(Gender.FEMALE);
        students.add(c);

        aStudents = new StudentArrayAdapter(getActivity(), R.layout.student_row, students);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_roster, container, false);
        setupViews(v);
        return v;
    }

    private void setupViews(View v) {
        lvStudents = (SwipeListView) v.findViewById(R.id.lvStudents);
        lvStudents.setAdapter(aStudents);

        lvStudents.setChoiceMode(ListView.CHOICE_MODE_NONE);
        lvStudents.setSwipeListViewListener(new SwipeListViewListener() {
            @Override
            public void onOpened(int i, boolean b) {
                Toast.makeText(getActivity(), "Swipe onOpened!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClosed(int i, boolean b) {

            }

            @Override
            public void onListChanged() {

            }

            @Override
            public void onMove(int i, float v) {

            }

            @Override
            public void onStartOpen(int i, int i2, boolean b) {

            }

            @Override
            public void onStartClose(int i, boolean b) {

            }

            @Override
            public void onClickFrontView(int i) {

            }

            @Override
            public void onClickBackView(int i) {

            }

            @Override
            public void onDismiss(int[] ints) {

            }

            @Override
            public int onChangeSwipeMode(int i) {
                return 0;
            }

            @Override
            public void onChoiceChanged(int i, boolean b) {

            }

            @Override
            public void onChoiceStarted() {

            }

            @Override
            public void onChoiceEnded() {

            }

            @Override
            public void onFirstListItem() {

            }

            @Override
            public void onLastListItem() {

            }
        });
    }

}
