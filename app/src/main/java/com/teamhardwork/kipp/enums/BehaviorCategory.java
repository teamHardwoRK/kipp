package com.teamhardwork.kipp.enums;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public enum BehaviorCategory {
    PERSISTENCE,
    RELATIONSHIPS,
    INTEGRITY,
    ZEST,
    EXCELLENCE,
    SLIP,
    FALL;

    public List<Behavior> getBehaviors() {
        List<Behavior> behaviors = Arrays.asList(Behavior.values());

        Iterator<Behavior> iterator = behaviors.iterator();

        while (iterator.hasNext()) {
            Behavior behavior = iterator.next();

            if (behavior.getCategory() != this) {
                iterator.remove();
            }
        }

        return behaviors;
    }
}
