package com.teamhardwork.kipp.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.StudentArrayAdapter;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

public class RosterFragment extends BaseKippFragment {
    private static final int GOOD_COLOR_ID = Color.parseColor("#C7F464");
    private static final int BAD_COLOR_ID = Color.parseColor("#FF6B6B");

    List<Student> students;
    StudentArrayAdapter aStudents;
    SwipeListView lvStudents;
    private RosterSwipeListener listener;
    private OnStudentSelectedListener onStudentSelectedListener;

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
        setHasOptionsMenu(true);

        aStudents = new StudentArrayAdapter(getActivity(), R.layout.item_student_row,
                new ArrayList<Student>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_roster, container, false);
        ButterKnife.inject(this, v);
        setupViews(v);

        currentClass.getClassRosterAsync(new FindCallback<Student>() {
            @Override
            public void done(List<Student> foundStudents, ParseException e) {
                students = foundStudents;
                Collections.sort(students);
                aStudents.addAll(students);
                progressBar.setVisibility(View.GONE);
            }
        });
        return v;
    }

    private void setupViews(View v) {
        lvStudents = (SwipeListView) v.findViewById(R.id.lvStudents);
        lvStudents.setAdapter(aStudents);

        lvStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvStudents.setFastScrollEnabled(true);
        lvStudents.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                mode.setTitle("Selected (" + lvStudents.getCountSelected() + " students)");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_eval:
                        // go show behavior Fragment
                        List<Integer> positions = lvStudents.getPositionsSelected();
                        ArrayList<Student> selectedStudents = new ArrayList<Student>();
                        for (int i = 0; i < positions.size(); i++) {
                            selectedStudents.add(aStudents.getItem(positions.get(i)));
                        }
                        listener.showBehaviorPagerFragment(selectedStudents, currentClass, true);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.multiple_students, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                lvStudents.unselectedChoiceStates();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
        });

        lvStudents.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
                if (toRight == true) {
                    ArrayList<Student> selectedStudents = new ArrayList<Student>();
                    selectedStudents.add(aStudents.getItem(position));
                    listener.showBehaviorPagerFragment(selectedStudents, currentClass, true);
                    lvStudents.closeOpenedItems();
                } else {
                    ArrayList<Student> selectedStudents = new ArrayList<Student>();
                    selectedStudents.add(aStudents.getItem(position));
                    listener.showBehaviorPagerFragment(selectedStudents, currentClass, false);
                    lvStudents.closeOpenedItems();
                }
            }

            @Override
            public void onStartOpen(int position, int action, boolean toRight) {
                View v = lvStudents.getChildAt(position);
                View backView = null;
                if (v == null) return;

                backView = v.findViewById(lvStudents.getTouchListener().getBackViewId());
                if (backView == null) return;

                if (toRight == true) {
                    backView.setBackgroundColor(GOOD_COLOR_ID);
                } else {
                    backView.setBackgroundColor(BAD_COLOR_ID);
                }
            }

            @Override
            public void onClickFrontView(int position) {
                Student clicked = aStudents.getItem(position);
                onStudentSelectedListener.onStudentSelected(clicked);
                Toast.makeText(getActivity(), clicked.getFirstName() + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public int onChangeSwipeMode(int i) {
                //return 0; // Swipe won't work!
                return SwipeListView.SWIPE_MODE_DEFAULT;
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

    public interface OnStudentSelectedListener {
        public void onStudentSelected(Student student);
    }

    public interface RosterSwipeListener {
        public void showBehaviorPagerFragment(ArrayList<Student> students, SchoolClass schoolClass
                , boolean isPositive);
    }
}
