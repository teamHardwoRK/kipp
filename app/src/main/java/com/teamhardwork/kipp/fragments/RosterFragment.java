package com.teamhardwork.kipp.fragments;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.StudentArrayAdapter;
import com.teamhardwork.kipp.listeners.RosterTabListener;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RosterFragment extends Fragment {
    private static final int GOOD_COLOR_ID = Color.parseColor("#C7F464");
    private static final int BAD_COLOR_ID = Color.parseColor("#FF6B6B");
    private static int maxNumTabs = 4;
    private static int minStudentRange = 5;

    SchoolClass schoolClass;
    List<Student> students;
    StudentArrayAdapter aStudents;
    SwipeListView lvStudents;
    private RosterSwipeListener listener;
    private OnStudentSelectedListener onStudentSelectedListener;
    private ArrayList<TabRange> tabsRanges;

    private class TabRange
    {
        private Character beginChar;
        private Integer jumpPosition;

        public TabRange(Character beginChar, Integer jumpPosition)
        {
            this.beginChar   = beginChar;
            this.jumpPosition = jumpPosition;
        }

        public Character getBeginChar()   { return beginChar; }
        public Integer getJumpPosition() { return jumpPosition; }
    }

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
        setHasOptionsMenu(true);

        aStudents = new StudentArrayAdapter(getActivity(), R.layout.student_row, new ArrayList<Student>());

        schoolClass = ((KippApplication) getActivity().getApplication()).getSchoolClass();

        tabsRanges = new ArrayList<TabRange>();
        if (schoolClass != null) {
            schoolClass.getClassRosterAsync(new FindCallback<Student>() {
                @Override
                public void done(List<Student> foundStudents, ParseException e) {
                    students = foundStudents;
                    Collections.sort(students);
                    aStudents.addAll(students);

                    // calculate tabs' ranges based on names on the roster
                    if (!students.isEmpty() && students.size() > minStudentRange) {
                        int jumpPos = 0;
                        char beginChar = 'a';
                        tabsRanges.add(new TabRange(beginChar, jumpPos));

                        int rangeSize = (int) ((students.size() / maxNumTabs) + 0.5);
                        if (rangeSize < minStudentRange) rangeSize = minStudentRange;

                        while (jumpPos < (students.size() - rangeSize)) {
                            jumpPos = jumpPos + rangeSize;
                            beginChar = Character.toLowerCase(students.get(jumpPos).getFirstName().charAt(0));
                            tabsRanges.add(new TabRange(beginChar, jumpPos));
                        }

                        setupTabs();
                    }
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
        setupTabs();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getActionBar().removeAllTabs();
    }

    private void setupTabs() {
        ActionBar actionBar = getActivity().getActionBar();

        if (actionBar.getTabCount() == 0) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.setDisplayShowTitleEnabled(true);

            char beginChar;
            char endChar;

            for (int i = 0; i < tabsRanges.size(); i++) {
                beginChar = tabsRanges.get(i).getBeginChar();
                if (i == (tabsRanges.size() - 1)) {
                    endChar = 'z';
                } else {
                    endChar = (char) (tabsRanges.get(i + 1).getBeginChar() - 1);
                }
                ActionBar.Tab tab = actionBar
                        .newTab()
                        .setText(beginChar + "-" + endChar)
                        .setTabListener(new RosterTabListener(R.id.flRoster,
                                "", lvStudents, tabsRanges.get(i).getJumpPosition()));
                actionBar.addTab(tab);
            }

            if (actionBar.getTabCount() > 0) {
                actionBar.selectTab(actionBar.getTabAt(0));
            }
        }
    }

    private void setupViews(View v) {
        lvStudents = (SwipeListView) v.findViewById(R.id.lvStudents);
        lvStudents.setAdapter(aStudents);

        lvStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
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
                        listener.showBehaviorPagerFragment(selectedStudents, schoolClass, true);
                        mode.finish();
                        return true;
//                    case R.id.action_note:
//                        // TODO: go show note dialog
//                        mode.finish();
//                        return true;
//                    case R.id.action_filter:
//                        // TODO: go do filter
//                        mode.finish();
//                        return true;
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
                    listener.showBehaviorPagerFragment(selectedStudents, schoolClass, true);
                    lvStudents.closeOpenedItems();
                } else {
                    ArrayList<Student> selectedStudents = new ArrayList<Student>();
                    selectedStudents.add(aStudents.getItem(position));
                    listener.showBehaviorPagerFragment(selectedStudents, schoolClass, false);
                    lvStudents.closeOpenedItems();
                }
            }

            @Override
            public void onClosed(int position, boolean toRight) {
                // TODO
            }

            @Override
            public void onListChanged() {

            }

            @Override
            public void onMove(int position, float v) {
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
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(int position) {
                Student clicked = aStudents.getItem(position);
                onStudentSelectedListener.onStudentSelected(clicked);
                Toast.makeText(getActivity(), clicked.getFirstName() + " selected", Toast.LENGTH_SHORT).show();
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
        public void onStudentSelected(Student student);
    }

    public interface RosterSwipeListener {
        public void showBehaviorPagerFragment(ArrayList<Student> students, SchoolClass schoolClass, boolean isPositive);
    }
}
