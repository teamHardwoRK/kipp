package com.teamhardwork.kipp.utilities.behavior_event;

import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.models.BehaviorEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BehaviorEventListFilterer {

    public static List<BehaviorEvent> filterByBehavior(List<BehaviorEvent> list, Behavior behavior) {
        List<BehaviorEvent> filtered = new ArrayList<BehaviorEvent>();
        for (BehaviorEvent event : list) {
            Behavior curBehavior = event.getBehavior();
            if (curBehavior.equals(behavior)) {
                filtered.add(event);
            }
        }
        return filtered;
    }

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

    public static Map<Behavior, Integer> getGroupedCount(List<BehaviorEvent> behaviorEvents) {
        HashMap<Behavior, Integer> groupedCount = new HashMap<Behavior, Integer>();
        for (Behavior behavior : Behavior.values()) {
            groupedCount.put(behavior, 0);
        }


        for (BehaviorEvent behaviorEvent : behaviorEvents) {
            Behavior behavior = behaviorEvent.getBehavior();
            Integer prevCount = groupedCount.get(behavior);
            groupedCount.put(behavior, prevCount + 1);
        }

        return groupedCount;
    }
}
