package com.teamhardwork.kipp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.BehaviorAdapter;
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.enums.BehaviorCategory;
import com.teamhardwork.kipp.graphics.StarDrawable;

import java.util.ArrayList;
import java.util.List;

public class BehaviorFragment extends Fragment {
    private static final String ARG_PARAM1 = "isPositive";

    private List<Behavior> behaviors;
    private boolean isPositive;
    private ArrayAdapter<Behavior> behaviorsAdapter;
    private GridView gvBehaviors;
    private BehaviorListener listener;
    private RelativeLayout rlBehaviors;


    public BehaviorFragment() {
        // Required empty public constructor
    }

    public static BehaviorFragment newInstance(boolean isPositive) {
        BehaviorFragment fragment = new BehaviorFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, isPositive);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isPositive = true;

        if (getArguments() != null) {
            isPositive = getArguments().getBoolean(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_behavior, container, false);

        gvBehaviors = (GridView) v.findViewById(R.id.gvBehaviors);
        rlBehaviors = (RelativeLayout) v.findViewById(R.id.rlBehaviors);

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

        behaviorsAdapter = new BehaviorAdapter(getActivity(), behaviors);

        gvBehaviors.setAdapter(behaviorsAdapter);
        gvBehaviors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long mylng) {
                // pass behavior back to listener
                createAndAnimateStars(view, position);
            }
        });

        return v;
    }

    void createAndAnimateStars(View view, final int position) {
        final ImageView starImageView = new ImageView(getActivity());
        StarDrawable starDrawable = new StarDrawable(50, 30);
        starDrawable.setPaintStyle(Paint.Style.STROKE);
        starDrawable.setStrokeColor(Color.RED);
        starDrawable.setStrokeWidth(5);
        starImageView.setBackground(starDrawable);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.setMargins(view.getLeft(), view.getTop(), 0, 0);
        starImageView.setLayoutParams(params);
        rlBehaviors.addView(starImageView);

        final ImageView starImageView2 = new ImageView(getActivity());
        StarDrawable starDrawable2 = new StarDrawable(50, 30);
        starDrawable2.setPaintStyle(Paint.Style.FILL_AND_STROKE);
        starDrawable2.setStrokeColor(Color.GREEN);
        starDrawable2.setFillColor(Color.YELLOW);
        starDrawable2.setStrokeWidth(3);
        starImageView2.setBackground(starDrawable2);

        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(100, 100);
        params2.setMargins(view.getLeft() + 100, view.getTop() + 50, 0, 0);
        starImageView2.setLayoutParams(params2);
        rlBehaviors.addView(starImageView2);

        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360);
        rotateAnimation.setDuration(3000);
        set.addAnimation(rotateAnimation);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 500, 0, 500);
        translateAnimation.setDuration(3000);
        set.addAnimation(translateAnimation);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                starImageView.setVisibility(View.GONE);
                starImageView2.setVisibility(View.GONE);
                listener.closeBehaviorPagerFragment(behaviors.get(position));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        starImageView.startAnimation(set);
        starImageView2.startAnimation(set);
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
        public void closeBehaviorPagerFragment(Behavior behavior);
    }
}
