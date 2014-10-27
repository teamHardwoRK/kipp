package com.teamhardwork.kipp.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.InfoPagerAdapter;
import com.teamhardwork.kipp.dialogfragments.AddActionDialogFragment;
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends BaseKippActivity implements FeedFragment.FeedListener {
    @InjectView(R.id.vpPager)
    ViewPager vpPager;
    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    private StatsFragment statsFragment;
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

        FragmentPagerAdapter pagerAdapter = new InfoPagerAdapter(getFragmentManager(), selectedId);
        vpPager.setAdapter(pagerAdapter);
        tabs.setViewPager(vpPager);
    }

    private void setActionBarTitle() {
        selected = getSelectedStudent();
        if (selected != null) {
            setCustomStudentActionBar();
        } else {
            getActionBar().setTitle(((KippApplication)getApplication()).getSchoolClass().getName());
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
        ivProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ivProfilePic.setImageResource(R.drawable.ic_profile_placeholder);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(80, 80);
        ivProfilePic.setLayoutParams(layoutParams);
        ivProfilePic.setBorderColor(Color.parseColor("#FFFFFFFF"));
        ivProfilePic.setBorderWidth(2);
        llActionBar.addView(ivProfilePic);
        Picasso.with(this)
                .load("http://thecatapi.com/api/images/get?format=src&type=jpg")
                .resize(80, 80)
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
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
}
