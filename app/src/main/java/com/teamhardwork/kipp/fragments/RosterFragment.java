package com.teamhardwork.kipp.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.StudentArrayAdapter;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;
import com.teamhardwork.kipp.utilities.Recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

public class RosterFragment extends BaseKippFragment implements Recommendation.RecListener {
    List<Student> students;
    StudentArrayAdapter aStudents;
    SwipeListView lvStudents;
    private RosterSwipeListener listener;
    private OnStudentSelectedListener onStudentSelectedListener;
    private ArrayList<Student> selectedStudents;
    private List<BehaviorEvent> classBehaviorEvents;
    private int frontViewId;
    private int backViewId;

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
        selectedStudents = new ArrayList<Student>();
        Recommendation.getInstance().setListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_roster, container, false);
        ButterKnife.inject(this, v);
        setupViews(v);

        if (aStudents.isEmpty()) {
            currentClass.getClassRosterAsync(new FindCallback<Student>() {
                @Override
                public void done(List<Student> foundStudents, ParseException e) {
                    students = foundStudents;
                    if (students == null) {
                        return;
                    }
                    Collections.sort(students);
                    aStudents.clear();
                    aStudents.addAll(students);
                    aStudents.setOriginalRoster(students);
                    progressBar.setVisibility(View.GONE);
                    if (!Recommendation.getInstance().isInit()) {
                        for (Student student : students) {
                            Recommendation.getInstance().addRecs(student);
                        }
                        aStudents.notifyDataSetChanged();
                    }
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
        }


        FeedQueries.getClassFeed(currentClass, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                classBehaviorEvents = behaviorEvents;
            }
        });

        return v;
    }

    private void setupViews(View v) {
        lvStudents = (SwipeListView) v.findViewById(R.id.lvStudents);
        lvStudents.setAdapter(aStudents);

        frontViewId = lvStudents.getTouchListener().getFrontViewId();
        backViewId = lvStudents.getTouchListener().getBackViewId();

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
                        selectedStudents.clear();
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
                    selectedStudents.clear();
                    selectedStudents.add(aStudents.getItem(position));
                    listener.showBehaviorPagerFragment(selectedStudents, currentClass, true);
                    lvStudents.closeOpenedItems();
                } else {
                    selectedStudents.clear();
                    selectedStudents.add(aStudents.getItem(position));
                    listener.showBehaviorPagerFragment(selectedStudents, currentClass, false);
                    lvStudents.closeOpenedItems();
                }
            }

            @Override
            public void onStartOpen(int position, int action, boolean toRight) {
                Resources resources = RosterFragment.this.getActivity().getResources();
                View v = lvStudents.getChildAt(position - lvStudents.getFirstVisiblePosition());
                View backView = null;
                if (v == null) return;

                backView = v.findViewById(backViewId);
                if (backView == null) return;

                if (toRight == true) {
                    backView.setBackgroundColor(resources.getColor(R.color.AlgaeGreen));
                } else {
                    backView.setBackgroundColor(resources.getColor(R.color.PaleVioletRed));
                }
            }

            @Override
            public void onClickFrontView(int position) {
                Student clicked = aStudents.getItem(position);
                onStudentSelectedListener.onStudentSelected(clicked);
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
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        lvStudents.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH);
        lvStudents.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
        lvStudents.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
        lvStudents.setOffsetLeft(convertDpToPixel(dpWidth / 4));
        lvStudents.setOffsetRight(convertDpToPixel(dpWidth / 4));
        lvStudents.setSwipeCloseAllItemsWhenMoveList(true);
        lvStudents.setAnimationTime(500);
        lvStudents.setSwipeOpenOnLongPress(false);
    }

    private int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    public void updateData() {
        final FindCallback<BehaviorEvent> callback = new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> eventList, ParseException e) {
                if (aStudents == null) return;
                for (BehaviorEvent event : eventList) {
                    classBehaviorEvents.add(0, event);
                    Student student = event.getStudent();
                    int pos = aStudents.getPosition(student);

                    if (pos != -1) {
                        View v = lvStudents.getChildAt(pos -
                                lvStudents.getFirstVisiblePosition());
                        updateStudentBehavior(v, student, event);
                    }
                }
            }
        };

        FeedQueries.getLatestClassEvents(currentClass, classBehaviorEvents, callback);
    }

    public void updateStudentBehavior(final View view, final Student student, final BehaviorEvent event) {
        if (view == null) return;
        final ImageView ivFirstBehavior = (ImageView) view.findViewById(R.id.ivFirstBehavior);
        final ImageView ivSecondBehavior = (ImageView) view.findViewById(R.id.ivSecondBehavior);
        final ImageView ivThirdBehavior = (ImageView) view.findViewById(R.id.ivThirdBehavior);
        final ImageView ivFourthBehavior = (ImageView) view.findViewById(R.id.ivFourthBehavior);
        final ImageView ivNewBehavior = (ImageView) view.findViewById(R.id.ivLastBehavior);
        final ImageView ivTips = (ImageView) view.findViewById(R.id.ivTips);

        ivTips.setImageResource(0);

        ivNewBehavior.setImageResource(event.getBehavior().getColorResource());

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivNewBehavior, "scaleX", 2.0f, 1.0f)
                .setDuration(1000);
        scaleX.setRepeatCount(5);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivNewBehavior, "scaleY", 2.0f, 1.0f)
                .setDuration(1000);
        scaleY.setRepeatCount(5);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setStartDelay(3000);
        set.start();

        // Recalculate Recommendation for this student
        if (Recommendation.getInstance().hasRecs(student)) {
            Recommendation.getInstance().dismissRecs(student);
        }
        Recommendation.getInstance().addRecs(student);

        FeedQueries.getStudentFeedMostRecent(student, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                if (behaviorEvents == null || behaviorEvents.isEmpty()) return;

                Collections.reverse(behaviorEvents);

                for (int i = behaviorEvents.size() - 1; i >= 0; i--) {
                    int eventResource = behaviorEvents.get(i).getBehavior().getColorResource();

                    if (i == behaviorEvents.size() - 2) {
                        ivFourthBehavior.setImageResource(eventResource);
                        ivFourthBehavior.setVisibility(View.VISIBLE);
                    } else if (i == behaviorEvents.size() - 3) {
                        ivThirdBehavior.setImageResource(eventResource);
                        ivThirdBehavior.setVisibility(View.VISIBLE);
                    } else if (i == behaviorEvents.size() - 4) {
                        ivSecondBehavior.setImageResource(eventResource);
                        ivSecondBehavior.setVisibility(View.VISIBLE);
                    } else if (i == behaviorEvents.size() - 5) {
                        ivFirstBehavior.setImageResource(eventResource);
                        ivFirstBehavior.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void onAddRec(Student student, Recommendation.RecommendationData rec) {
        if (aStudents == null) return;
        int pos = aStudents.getPosition(student);

        if (pos != -1) {
            View v = lvStudents.getChildAt(pos -
                    lvStudents.getFirstVisiblePosition());
            if (v == null) return;

            ImageView ivTips = (ImageView) v.findViewById(R.id.ivTips);
            updateStudentRec(ivTips, rec);
        }
    }

    @Override
    public void onDismissRec(Student student) {
        if (aStudents == null) return;
        int pos = aStudents.getPosition(student);

        if (pos != -1) {
            View v = lvStudents.getChildAt(pos -
                    lvStudents.getFirstVisiblePosition());
            if (v == null) return;

            ImageView ivTips = (ImageView) v.findViewById(R.id.ivTips);
            ivTips.setImageResource(0);
        }
    }

    public void updateStudentRec(final ImageView ivTips, final Recommendation.RecommendationData rec) {
        if (ivTips == null || rec == null) return;
        ivTips.setImageResource(R.drawable.ic_tips);
        int tipColor = (rec.getRecType() == Recommendation.RecommendationType.BAD) ?
                StudentArrayAdapter.warningColor : StudentArrayAdapter.infoColor;
        ivTips.setColorFilter(tipColor);
    }

    public interface OnStudentSelectedListener {
        public void onStudentSelected(Student student);
    }

    public interface RosterSwipeListener {
        public void showBehaviorPagerFragment(ArrayList<Student> students, SchoolClass schoolClass
                , boolean isPositive);
    }

    public StudentArrayAdapter getStudentsAdapter() {
        return aStudents;
    }
}
