package com.teamhardwork.kipp.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.dialogfragments.AddActionDialogFragment;
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.fragments.BehaviorFragment;
import com.teamhardwork.kipp.fragments.BehaviorPagerFragment;
import com.teamhardwork.kipp.fragments.FeedFragment;
import com.teamhardwork.kipp.fragments.LeaderboardFragment;
import com.teamhardwork.kipp.fragments.RosterFragment;
import com.teamhardwork.kipp.fragments.StatsFragment;
import com.teamhardwork.kipp.graphics.SadFaceAnimationSet;
import com.teamhardwork.kipp.graphics.StarAnimationSet;
import com.teamhardwork.kipp.listeners.FragmentTabListener;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.utilities.ViewUtils;

import java.util.ArrayList;
import java.util.Date;

public class RosterActivity extends BaseKippActivity implements
        RosterFragment.OnStudentSelectedListener,
        RosterFragment.RosterSwipeListener,
        BehaviorFragment.BehaviorListener,
        SadFaceAnimationSet.SadFaceAnimationSetListener,
        FeedFragment.FeedListener,
        StarAnimationSet.StarAnimationSetListener,
        LeaderboardFragment.OnStudentSelectedListener {

    private final String ROSTER_FRAGMENT_TAG = "RosterFragment";
    private final String STATS_FRAGMENT_TAG = "StatsFragment";
    private final String LOG_FRAGMENT_TAG = "LogFragment";
    private final String LEADERBOARD_FRAGMENT_TAG = "LeaderboardFragment";
    private final String BEHAVIOR_PAGER_FRAGMENT_TAG = "BehaviorPagerFragment";
    private Fragment pagerFragment;
    private ArrayList<Student> selectedStudents;
    private SchoolClass schoolClass;
    private SearchView searchView;
    private DrawerLayout dlRoster;
    private ViewGroup drawerItemsListContainer;
    private LinearLayout llAccountInfo;
    private ActionBarDrawerToggle abDrawerToggle;

    //  list of all possible nav items.
    protected static final int NAVDRAWER_ITEM_CLASS = 0;
    protected static final int NAVDRAWER_ITEM_STATS = 1;
    protected static final int NAVDRAWER_ITEM_LOG = 2;
    protected static final int NAVDRAWER_ITEM_RANK = 3;

    // titles for navdrawer items (indices correspond to the above)
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.navdrawer_item_class,
            R.string.navdrawer_item_stats,
            R.string.navdrawer_item_logs,
            R.string.navdrawer_item_rank,
    };

    // icons for navdrawer items (indices must correspond to above array)
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[] {
            R.drawable.ic_drawer_class,  // My Schedule
            R.drawable.ic_drawer_stats,  // Explore
            R.drawable.ic_drawer_logs, // Map
            R.drawable.ic_drawer_rank, // Social
    };

    // list of navdrawer items, in order
    private ArrayList<Integer> navDrawerItems = new ArrayList<Integer>();

    // views that correspond to each navdrawer item, null if not yet created
    private View[] navDrawerItemViews = null;

    // Nav Drawer item selected, set to 0
    private int navDrawerItemIdSelected = NAVDRAWER_ITEM_CLASS;


    @Override
    public void addAction(BehaviorEvent event) {
        AddActionDialogFragment dialogFragment = AddActionDialogFragment.getInstance(event);
        dialogFragment.show(getFragmentManager(), "dialog_fragment_add_action");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);

        SchoolClass schoolClass = ((KippApplication) getApplication()).getSchoolClass();

        getSupportActionBar().setTitle(schoolClass.getName());
        setupNavDrawer();

        showFragment(RosterFragment.class);
    }

    /**
     * Sets up the navigation drawer
     */
    private void setupNavDrawer() {
        dlRoster = (DrawerLayout) findViewById(R.id.dlRoster);
        drawerItemsListContainer = (ViewGroup) findViewById(R.id.llNavDrawerItemList);

        // ActionBar navigation toogle
        abDrawerToggle = new ActionBarDrawerToggle(
                this, dlRoster, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // creates call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // creates call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
            }
        };

        dlRoster.setDrawerListener(abDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // navBarItems
        navDrawerItems.add(NAVDRAWER_ITEM_CLASS);
        navDrawerItems.add(NAVDRAWER_ITEM_STATS);
        navDrawerItems.add(NAVDRAWER_ITEM_LOG);
        navDrawerItems.add(NAVDRAWER_ITEM_RANK);

        // create nav bar items
        navDrawerItemViews = new View[navDrawerItems.size()];
        drawerItemsListContainer.removeAllViews();

        int i =0;
        for (int itemId : navDrawerItems) {
            navDrawerItemViews[i] = createNavDrawerItem(itemId, drawerItemsListContainer);
            drawerItemsListContainer.addView(navDrawerItemViews[i]);
            i++;
        }
    }

    private View createNavDrawerItem(final int itemId, ViewGroup container) {
        boolean selected = navDrawerItemIdSelected == itemId;

        View view = getLayoutInflater().inflate(R.layout.item_navdrawer, container, false);

        ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        ivIcon.setImageResource(NAVDRAWER_ICON_RES_ID[itemId]);
        tvTitle.setText(getString(NAVDRAWER_TITLE_RES_ID[itemId]));

        formatNavDrawerItem(view, selected);

        // set up click listener
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onNavDrawerItemSelected(itemId);
            }
        });

        return view;
    }

    private void formatNavDrawerItem(View view, boolean selected) {
        ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        // format item whether is selected or not
        tvTitle.setTextColor(selected ?
                getResources().getColor(R.color.navdrawer_text_color_selected) :
                getResources().getColor(R.color.navdrawer_text_color));
        ivIcon.setColorFilter(selected ?
                getResources().getColor(R.color.navdrawer_icon_tint_selected) :
                getResources().getColor(R.color.navdrawer_icon_tint));
    }

    private void onNavDrawerItemSelected(final int itemId) {
        if (itemId == navDrawerItemIdSelected) {
            dlRoster.closeDrawer(Gravity.START);
            return;
        }

        switch (itemId) {
            case NAVDRAWER_ITEM_CLASS:
                break;
            case NAVDRAWER_ITEM_STATS:
                break;
            case NAVDRAWER_ITEM_LOG:
                break;
            case NAVDRAWER_ITEM_RANK:
                break;
        }

        navDrawerItemIdSelected = itemId;

        // change the active item on the list so the user can see the item changed
        setSelectedNavDrawerItem(itemId);

        dlRoster.closeDrawer(Gravity.START);
    }

    /**
     * Sets the NavDrawer items appearance to the item selected
     *
     * @param itemId
     */
    private void setSelectedNavDrawerItem(int itemId) {
        if (navDrawerItemViews != null) {
            for (int i = 0; i < navDrawerItemViews.length; i++) {
                if (i < navDrawerItems.size()) {
                    int thisItemId = navDrawerItems.get(i);
                    formatNavDrawerItem(navDrawerItemViews[i], itemId == thisItemId);
                }
            }
        }
    }

    private RosterFragment getRosterFragment() {
        return (RosterFragment) getSupportFragmentManager().findFragmentByTag(RosterFragment.class.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.roster, menu);
        setupMenuSearch(menu);
        return true;
    }

    private void setupMenuSearch(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem == null) throw new IllegalArgumentException("menu item is null and that is very suspicious.");

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView == null) throw new IllegalArgumentException("search view is null and that is very suspicious.");

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);

        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.WHITE);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTypeface(getDefaultTypeface());

        searchView.setQueryHint("Enter First Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.matches("")) {
                    getRosterFragment().getStudentsAdapter().resetFilter();
                } else {
                    getRosterFragment().getStudentsAdapter().getFilter().filter(s);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (abDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_logout:
                ParseUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                exitActivity();
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Called when invalidateOptionsMenu() gets called */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = dlRoster.isDrawerOpen(Gravity.START);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        menu.findItem(R.id.action_logout).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        abDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        abDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onStudentSelected(Student student) {
        Fragment rosterFragment = getSupportFragmentManager().findFragmentByTag(ROSTER_FRAGMENT_TAG);

        // HACK: reset roster Fragment to get around swiping issue
        if (rosterFragment != null) {
            getSupportFragmentManager().beginTransaction().detach(rosterFragment).attach(rosterFragment)
                    .commit();
        }

        gotoInfoActivity(student);
    }

    private void gotoInfoActivity(Student student) {
        clearSearchView();

        Intent i = new Intent(this, InfoActivity.class);
        if (student != null) {
            i.putExtra("selected_student_id", student.getObjectId());
        }
        enterActivity(i);
    }

    private void clearSearchView() {
        if (searchView != null) {
            searchView.setQuery("", false);
            searchView.clearFocus();
        }
    }

    public void showBehaviorPagerFragment(ArrayList<Student> students, SchoolClass schoolClass, boolean isPositive) {
        this.selectedStudents = students;
        this.schoolClass = schoolClass;

        FragmentManager fm = getSupportFragmentManager();
        pagerFragment = fm.findFragmentByTag(BEHAVIOR_PAGER_FRAGMENT_TAG);

        // show behavior Fragment
        if (pagerFragment == null) {
            pagerFragment = BehaviorPagerFragment.newInstance(isPositive);
        }

        ((BehaviorPagerFragment) pagerFragment).show(fm, BEHAVIOR_PAGER_FRAGMENT_TAG);
    }

    @Override
    public void saveBehavior(Behavior behavior) {
        if (behavior == null) return;
        for (Student curStudent : selectedStudents) {
            BehaviorEvent behaviorEvent = new BehaviorEvent();
            behaviorEvent.setBehavior(behavior);
            behaviorEvent.setSchoolClass(schoolClass);
            behaviorEvent.setStudent(curStudent);
            behaviorEvent.setOccurredAt(new Date());
            behaviorEvent.setNotes("");
            behaviorEvent.saveInBackground();
            Toast.makeText(RosterActivity.this,
                    "behaviorEvent saved for " + curStudent.getFirstName(), Toast.LENGTH_SHORT)
                    .show();

            int behaviorPoints = behaviorEvent.getBehavior().getPoints();
            curStudent.addPoints(behaviorPoints);
            curStudent.saveInBackground();
        }
    }

    @Override
    public void onAnimationEnd() {
        FragmentManager fm = getSupportFragmentManager();
        pagerFragment = fm.findFragmentByTag(BEHAVIOR_PAGER_FRAGMENT_TAG);

        if (pagerFragment != null) {
            // this will by default remove the fragment instance
            ((BehaviorPagerFragment) pagerFragment).dismiss();
        }
    }

    @Override
    protected void updateFragments() {
        super.updateFragments();

        RosterFragment rosterFragment = getRosterFragment();
        if (rosterFragment != null) {
            rosterFragment.updateData();
        }

        StatsFragment statsFragment = (StatsFragment) getSupportFragmentManager().findFragmentByTag(STATS_FRAGMENT_TAG);
        if (statsFragment != null) {
            statsFragment.updateData();
        }

        FeedFragment feedFragment = (FeedFragment) getSupportFragmentManager().findFragmentByTag(LOG_FRAGMENT_TAG);
        if (feedFragment != null) {
            feedFragment.updateData();
        }

        LeaderboardFragment leaderboardFragment = (LeaderboardFragment) getSupportFragmentManager()
                .findFragmentByTag(LEADERBOARD_FRAGMENT_TAG);
        if (leaderboardFragment != null) {
            leaderboardFragment.updateData();
        }
    }

    @SuppressWarnings("rawtypes")
    private void showFragment(Class activeFragmentClass) {
        Class[] fragmentClasses = new Class[] {
                RosterFragment.class,
                StatsFragment.class,
                FeedFragment.class,
                LeaderboardFragment.class};
        FragmentManager mgr = getSupportFragmentManager();
        FragmentTransaction transaction = mgr.beginTransaction();
        try {
            for (Class klass : fragmentClasses) {
                Fragment fragment = mgr.findFragmentByTag(klass.getName());
                if (klass == activeFragmentClass) {
                    if (fragment != null) {
                        transaction.show(fragment);
                    } else {
                        transaction.add(R.id.flRoster, (Fragment) klass.newInstance(), klass.getName());
                    }
                } else {
                    if (fragment != null) {
                        transaction.hide(fragment);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        transaction.commit();
    }
}
