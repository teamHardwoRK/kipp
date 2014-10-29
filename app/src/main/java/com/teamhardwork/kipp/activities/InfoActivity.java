package com.teamhardwork.kipp.activities;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.dialogfragments.AddActionDialogFragment;
import com.teamhardwork.kipp.fragments.ActionLogFragment;
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.StudentStatsFragment;
import com.teamhardwork.kipp.listeners.FragmentTabListener;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends BaseKippActivity implements FeedFragment.FeedListener {
    @InjectView(R.id.flInfoContainer)
    FrameLayout flInfoContainer;

    private Student selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.inject(this);
        setActionBarTitle();

        // Back action bar navigation
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String selectedId = (selected != null) ? selected.getObjectId() : null;
        setupTabs(selectedId);
    }

    private void setActionBarTitle() {
        selected = getSelectedStudent();
        if (selected != null) {
            setCustomStudentActionBar();
        } else {
            getActionBar().setTitle(((KippApplication) getApplication()).getSchoolClass().getName());
        }
    }

    // Hackily construct a new action bar because we want a circular image view near the title
    private void setCustomStudentActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        LinearLayout llActionBar = new LinearLayout(actionBar.getThemedContext());
        llActionBar.setOrientation(LinearLayout.HORIZONTAL);
        llActionBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        CircleImageView ivProfilePic = new CircleImageView(this);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(110, 110);
        ivProfilePic.setLayoutParams(layoutParams);
        ivProfilePic.setBorderColor(Color.parseColor("#FFFFFFFF"));
        ivProfilePic.setBorderWidth(2);
        llActionBar.addView(ivProfilePic);
        Picasso.with(this)
                .load("http://thecatapi.com/api/images/get?format=src&type=jpg")
                .placeholder(R.drawable.ic_profile_placeholder)
                .resize(110, 110)
                .centerCrop()
                .into(ivProfilePic);

        TextView tvStudentName = new TextView(this);
        tvStudentName.setText(selected.getFullName());
        LinearLayout.LayoutParams studentNameParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_VERTICAL);
        studentNameParams.setMargins(15, 15, 0, 0);
        tvStudentName.setLayoutParams(studentNameParams);
        tvStudentName.setTextColor(Color.parseColor("#FFFFFF"));
        tvStudentName.setTextSize(17);
        llActionBar.addView(tvStudentName);

        actionBar.setCustomView(llActionBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);

        if (selected != null) {
            MenuItem item = menu.findItem(R.id.icContactStudent);
            item.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            exitActivity();
        } else if (id == R.id.icContactStudent) {
            if (selected != null) {
                AddActionDialogFragment dialogFragment = AddActionDialogFragment.getInstance(selected);

                dialogFragment.show(getFragmentManager(), "dialog_fragment_add_action");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private Student getSelectedStudent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return Student.getStudent(extras.getString("selected_student_id"));
        } else {
            return null;
        }
    }

    @Override
    public void addAction(BehaviorEvent event) {
        AddActionDialogFragment dialogFragment = AddActionDialogFragment.getInstance(event);

        dialogFragment.show(getFragmentManager(), "dialog_fragment_add_action");
    }

    private void setupTabs(String selectedStudentId) {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Bundle args = new Bundle();
        args.putString(StudentStatsFragment.STUDENT_ID_ARG_KEY, selectedStudentId);

        ActionBar.Tab tab1 = actionBar
                .newTab()
                .setText("Stats")
                .setTabListener(
                        new FragmentTabListener<StudentStatsFragment>(R.id.flInfoContainer, this,
                                StudentStatsFragment.TAG, StudentStatsFragment.class, args));
        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);

        ActionBar.Tab tab2 = actionBar
                .newTab()
                .setText("Behaviors")
                .setTabListener(
                        new FragmentTabListener<FeedFragment>(R.id.flInfoContainer, this,
                                FeedFragment.TAG, FeedFragment.class, args));
        actionBar.addTab(tab2);

        ActionBar.Tab tab3 = actionBar
                .newTab()
                .setText("Log")
                .setTabListener(
                        new FragmentTabListener<ActionLogFragment>(R.id.flInfoContainer, this,
                                ActionLogFragment.TAG, ActionLogFragment.class, args));
        actionBar.addTab(tab3);
    }

    @Override
    protected void updateFragments() {
        super.updateFragments();
        StudentStatsFragment studentStatsFragment = (StudentStatsFragment) getFragmentManager()
                .findFragmentByTag(StudentStatsFragment.STUDENT_ID_ARG_KEY);
        if (studentStatsFragment != null) {
            studentStatsFragment.updateData();
        }

        FeedFragment feedFragment = (FeedFragment) getFragmentManager()
                .findFragmentByTag(FeedFragment.TAG);
        if (feedFragment != null) {
            feedFragment.updateData();
        }

        ActionLogFragment actionLogFragment = (ActionLogFragment) getFragmentManager()
                .findFragmentByTag(ActionLogFragment.TAG);
        if (actionLogFragment != null) {
            actionLogFragment.updateData();
        }
    }
}
