package com.teamhardwork.kipp.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.StudentArrayAdapter;
import com.teamhardwork.kipp.enums.Gender;
import com.teamhardwork.kipp.models.users.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
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

        lvStudents.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
//        lvStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            lvStudents.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//
//                @Override
//                public void onItemCheckedStateChanged(ActionMode mode, int position,
//                                                      long id, boolean checked) {
//                    mode.setTitle("Selected (" + lvStudents.getCountSelected() + ")");
//                }
//
//                @Override
//                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                    switch (item.getItemId()) {
//                        default:
//                            return false;
//                    }
//                }
//
//                @Override
//                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
////                    MenuInflater inflater = mode.getMenuInflater();
////                    inflater.inflate(R.menu.menu_choice_items, menu);
//                    return true;
//                }
//
//                @Override
//                public void onDestroyActionMode(ActionMode mode) {
//                    lvStudents.unselectedChoiceStates();
//                }
//
//                @Override
//                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                    return false;
//                }
//            });
//        }

        lvStudents.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int i, boolean b) {

            }

            @Override
            public void onListChanged() {

            }

            @Override
            public void onMove(int position, float v) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean b) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(int position) {
            }

            @Override
            public void onClickBackView(int position) {
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    students.remove(position);
                }
                aStudents.notifyDataSetChanged();
            }

            @Override
            public int onChangeSwipeMode(int i) {
                //return 0; // Swipe won't work!
                return SwipeListView.SWIPE_MODE_DEFAULT;
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

        reloadListView();
    }

    private void reloadListView() {
        lvStudents.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH);
        lvStudents.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
        lvStudents.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
        lvStudents.setOffsetLeft(convertDpToPixel(0));
        lvStudents.setOffsetRight(convertDpToPixel(0));
        lvStudents.setSwipeCloseAllItemsWhenMoveList(true);
        lvStudents.setAnimationTime(0);
        lvStudents.setSwipeOpenOnLongPress(false);
    }

    private int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }
}
