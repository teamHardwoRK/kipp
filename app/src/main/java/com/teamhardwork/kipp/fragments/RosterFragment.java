package com.teamhardwork.kipp.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.StudentArrayAdapter;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;

import java.util.ArrayList;
import java.util.List;

public class RosterFragment extends Fragment {
    SchoolClass schoolClass;
    List<Student> students;
    StudentArrayAdapter aStudents;
    SwipeListView lvStudents;
    private RosterSwipeListener listener;
    private OnStudentSelectedListener onStudentSelectedListener;

    public static RosterFragment newInstance() {
        RosterFragment fragment = new RosterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnStudentSelectedListener) {
            onStudentSelectedListener = (OnStudentSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement RosterFragment.OnStudentSelectedListener");
        }
        if (activity instanceof RosterSwipeListener) {
            listener = (RosterSwipeListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement RosterFragment.RosterSwipeListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aStudents = new StudentArrayAdapter(getActivity(), R.layout.student_row, new ArrayList<Student>());

        schoolClass = ((KippApplication) getActivity().getApplication()).getSchoolClass();
        if (schoolClass != null) {
            schoolClass.getClassRosterAsync(new FindCallback<Student>() {
                @Override
                public void done(List<Student> foundStudents, ParseException e) {
                    students = foundStudents;
                    aStudents.addAll(students);
                }
            });
        }
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
                if (toRight == true) {
                    Toast.makeText(getActivity(), "toRight of " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                    ArrayList<String> studentIds = new ArrayList<String>();
                    studentIds.add(aStudents.getItem(position).getObjectId());
                    listener.toggleBehaviorFragment(true, studentIds, schoolClass.getObjectId(), true);
                } else {
                    Toast.makeText(getActivity(), "toLeft of " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                    ArrayList<String> studentIds = new ArrayList<String>();
                    studentIds.add(aStudents.getItem(position).getObjectId());
                    listener.toggleBehaviorFragment(true, studentIds, schoolClass.getObjectId(), false);
                }
            }

            @Override
            public void onClosed(int position, boolean toRight) {
                listener.toggleBehaviorFragment(false, null, null, true);
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
                    aStudents.remove(aStudents.getItem(position));
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

        lvStudents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Student clicked = aStudents.getItem(position);

//                Toast.makeText(getActivity(), "longClicked on " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                onStudentSelectedListener.onStudentSelected(clicked);

                return true;
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

    public void reset() {
        lvStudents.closeOpenedItems();
    }

    public interface OnStudentSelectedListener {
        void onStudentSelected(Student student);
    }

    public interface RosterSwipeListener {
        public void toggleBehaviorFragment(boolean open, ArrayList<String> studentIds, String schoolClassId, boolean positive);
    }
}
