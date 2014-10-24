package com.teamhardwork.kipp.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.enums.BehaviorCategory;

import java.util.ArrayList;
import java.util.List;

public class BehaviorFragment extends Fragment {
    private static final String ARG_PARAM1 = "isPositive";

    private List<Behavior> behaviors;
    private boolean isPositive;
    private ArrayAdapter<Behavior> behaviorsAdapter;
    private ListView lvBehaviors;
    private BehaviorListener listener;


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

        lvBehaviors = (ListView) v.findViewById(R.id.lvBehaviors);

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

        behaviorsAdapter =
                new ArrayAdapter<Behavior>(getActivity(), android.R.layout.simple_list_item_1, behaviors);

        lvBehaviors.setAdapter(behaviorsAdapter);
        lvBehaviors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long mylng) {
                // pass behavior back to listener
                listener.closeBehaviorPagerFragment(behaviors.get(position));
            }
        });

        return v;
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
