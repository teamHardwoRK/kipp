package com.teamhardwork.kipp.enums;

import com.teamhardwork.kipp.R;

public enum Behavior {
    CLEANING_UP("Cleaning Up", BehaviorCategory.INTEGRITY, 3, R.drawable.wh_tiger_ic),
    ON_TASK("On Task", BehaviorCategory.PERSISTENCE, 2, R.drawable.wh_lion_ic),
    RESPECTING_EVERYONE("Respecting Everyone", BehaviorCategory.RELATIONSHIPS, 2, R.drawable.wh_hippo_ic),
    SHOWING_GRATITUDE("Showing Gratitude", BehaviorCategory.INTEGRITY, 2, R.drawable.wh_panda_ic), //
    SILENT_REMINDERS("Silent Reminders", BehaviorCategory.INTEGRITY, 2, R.drawable.wh_sheep_ic),
    VOLUNTEERING("Volunteering", BehaviorCategory.INTEGRITY, 2, R.drawable.wh_dog_ic), //

    DRESS_CODE_VIOLATION("Dress Code Violation", BehaviorCategory.SLIP, -1, R.drawable.wh_monkey_ic),
    LACK_OF_INTEGRITY("Lack of Integrity", BehaviorCategory.FALL, -5, R.drawable.wh_mouse_ic),
    LATE("Late", BehaviorCategory.SLIP, -1, R.drawable.wh_frog_ic),
    TALKING("Talking", BehaviorCategory.FALL, -3, R.drawable.wh_rabbit_ic),
    HORSEPLAY("Horseplay", BehaviorCategory.SLIP, -1, R.drawable.wh_reindeer_ic),
    FIGHTING("Fighting", BehaviorCategory.FALL, -3, R.drawable.wh_bull_ic);

    String title;
    BehaviorCategory category;
    int points;
    int resource;

    Behavior(String title, BehaviorCategory category, int points, int resource) {
        this.title = title;
        this.category = category;
        this.points = points;
        this.resource = resource;
    }

    public String getTitle() {
        return title;
    }

    public BehaviorCategory getCategory() {
        return category;
    }

    public int getPoints() {
        return points;
    }

    public boolean equalsName(String otherTitle) {
        return (otherTitle == null) ? false : title.equals(otherTitle);
    }

    public String toString() {
        return title;
    }

    public int getResource() {
        return resource;
    }
}
