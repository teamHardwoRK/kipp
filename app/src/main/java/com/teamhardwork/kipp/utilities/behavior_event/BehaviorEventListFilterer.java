package com.teamhardwork.kipp.utilities.behavior_event;

import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.models.BehaviorEvent;

import java.util.ArrayList;
import java.util.List;

public class BehaviorEventListFilterer {

    public static List<BehaviorEvent> keepGood(List<BehaviorEvent> list) {
        List<BehaviorEvent> kept = new ArrayList<BehaviorEvent>();
        for (BehaviorEvent event : list) {
            Behavior curBehavior = event.getBehavior();
            int points = curBehavior.getPoints();
            if (points > 0) {
                kept.add(event);
            }
        }
        return kept;
    }

    public static List<BehaviorEvent> keepBad(List<BehaviorEvent> list) {
        List<BehaviorEvent> kept = new ArrayList<BehaviorEvent>();
        for (BehaviorEvent event : list) {
            Behavior curBehavior = event.getBehavior();
            int points = curBehavior.getPoints();
            if (points < 0) {
                kept.add(event);
            }
        }
        return kept;
    }
}
