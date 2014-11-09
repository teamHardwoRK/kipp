package com.teamhardwork.kipp.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.BehaviorAdapter;
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.enums.BehaviorCategory;
import com.teamhardwork.kipp.graphics.SadFaceAnimationSet;
import com.teamhardwork.kipp.graphics.SadFaceDrawable;
import com.teamhardwork.kipp.graphics.StarAnimationSet;
import com.teamhardwork.kipp.graphics.StarDrawable;
import com.teamhardwork.kipp.utilities.GraphicsUtils;
import com.teamhardwork.kipp.utilities.ReboundAnimator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BehaviorFragment extends Fragment {
    private static final String ARG_PARAM1 = "isPositive";
    private static final String BEHAVIOR_POSITION = "behaviorPosition";

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
                ReboundAnimator.startClickAnimation(view);
                // pass behavior back to listener
                Behavior behavior = behaviorsAdapter.getItem(position);
                if (behavior.getPoints() > 0) {
                    createAndAnimateStars(view);
                } else {
                    createAndAnimateSadFaces(view);
                }
                listener.saveBehavior(behavior);
            }
        });
        return v;
    }

    void createAndAnimateSadFaces(View view) {
        List<SadFaceDrawable> sadFaceList = SadFaceDrawable.createPopulation(getActivity(), 20);
        Point screenSize = new Point();
        ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(screenSize);

        for (int i = 0; i < sadFaceList.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(GraphicsUtils.dpToPx(200), GraphicsUtils.dpToPx(200));
            params.setMargins((new Random().nextInt(screenSize.x + GraphicsUtils.dpToPx(100))) - GraphicsUtils.dpToPx(100), -1 * GraphicsUtils.dpToPx(200), 0, 0);
            imageView.setLayoutParams(params);
            imageView.setBackground(sadFaceList.get(i));
            rlBehaviors.addView(imageView);

            SadFaceAnimationSet animationSet = new SadFaceAnimationSet(getActivity(),
                    (SadFaceAnimationSet.SadFaceAnimationSetListener) getActivity(), imageView);
            animationSet.setStartOffset(new Random().nextInt(2000));
            animationSet.setDuration(new Random().nextInt(1000) + 5000);
            animationSet.setTranslateAnimation();

            if (i == sadFaceList.size() - 1) {
                animationSet.setListener();
            }

            imageView.startAnimation(animationSet);
        }
    }

    void createAndAnimateStars(View view) {
        int[] directionList = {-1, 0, 1};
        int[] rotationDirection = {-1, 1};
        int startRotationRange = 540;
        int endRotationRange = 1080;

        for (StarDrawable star : StarDrawable.createUniverse(getActivity(), 60)) {
            ImageView starImageView = new ImageView(getActivity());
            starImageView.setBackground(star);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(GraphicsUtils.dpToPx(100), GraphicsUtils.dpToPx(100));

            int xOffset = view.getLeft();
            int yOffset = view.getTop();
            params.setMargins(xOffset, yOffset, 0, 0);

            starImageView.setLayoutParams(params);
            rlBehaviors.addView(starImageView);

            StarAnimationSet set = new StarAnimationSet(getActivity(), (StarAnimationSet.StarAnimationSetListener) getActivity(), starImageView);
            set.setStartOffset(new Random().nextInt(2000));
            set.setDuration(new Random().nextInt(1000) + 4000);
            set.setRotationAnimation(
                    rotationDirection[new Random().nextInt(rotationDirection.length)] * (startRotationRange + new Random().nextInt(endRotationRange - startRotationRange)));
            set.setScaleAnimation(new Random().nextInt(3) + 3.5f);
            set.setTranslateAnimation(directionList[new Random().nextInt(directionList.length)],
                    directionList[new Random().nextInt(directionList.length)]);
            starImageView.startAnimation(set);
        }
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
        public void saveBehavior(Behavior behavior);
    }
}
