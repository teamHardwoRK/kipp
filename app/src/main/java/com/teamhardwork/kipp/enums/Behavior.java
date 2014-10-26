package com.teamhardwork.kipp.enums;

import com.teamhardwork.kipp.R;

public enum Behavior {
    CLEANING_UP("Cleaning Up", BehaviorCategory.INTEGRITY, 3, R.drawable.wh_crane),
    ON_TASK("On Task", BehaviorCategory.PERSISTENCE, 2, R.drawable.wh_fish),
    RESPECTING_EVERYONE("Respecting Everyone", BehaviorCategory.RELATIONSHIPS, 2, R.drawable.wh_two_fish),
    SHOWING_GRATITUDE("Showing Gratitude", BehaviorCategory.INTEGRITY, 2, R.drawable.wh_monster),
    SILENT_REMINDERS("Silent Reminders", BehaviorCategory.INTEGRITY, 2, R.drawable.wh_sheep),
    VOLUNTEERING("Volunteering", BehaviorCategory.INTEGRITY, 2, R.drawable.wh_rabbit_smile),

    DRESS_CODE_VIOLATION("Dress Code Violation", BehaviorCategory.SLIP, -1, R.drawable.wh_dinosaur),
    LACK_OF_INTEGRITY("Lack of Integrity", BehaviorCategory.FALL, -5, R.drawable.wh_fish_bones),
    LATE("Late to School or Class", BehaviorCategory.SLIP, -1, R.drawable.wh_barn_owl),
    TALKING("Talking", BehaviorCategory.FALL, -3, R.drawable.wh_cat),
    HORSEPLAY("Horseplay", BehaviorCategory.SLIP, -1, R.drawable.wh_rocking_horse),
    FIGHTING("Fighting", BehaviorCategory.FALL, -3, R.drawable.wh_teddybear);

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
