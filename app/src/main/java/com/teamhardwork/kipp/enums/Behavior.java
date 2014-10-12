package com.teamhardwork.kipp.enums;

public enum Behavior {
    CLEANING_UP("Cleaning Up", BehaviorCategory.INTEGRITY, 3),
    DRESS_CODE_VIOLATION("Dress Code Violation", BehaviorCategory.SLIP, -1),
    LACK_OF_INTEGRITY("Lack of Integrity", BehaviorCategory.FALL, -5),
    LATE("Late to School or Class", BehaviorCategory.SLIP, -1),
    ON_TASK("On Task", BehaviorCategory.PERSISTENCE, 2),
    RESPECTING_EVERYONE("Respecting Everyone", BehaviorCategory.RELATIONSHIPS, 2),
    SHOWING_GRATITUDE("Showing Gratitude", BehaviorCategory.INTEGRITY, 2),
    TALKING("Talking", BehaviorCategory.FALL, -3),
    TALKING_BACK("Talking Back", BehaviorCategory.SLIP, -1);

    String title;
    BehaviorCategory category;
    int points;

    Behavior(String title, BehaviorCategory category, int points) {
        this.title = title;
        this.category = category;
        this.points = points;
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
}
