package com.teamhardwork.kipp.utilities;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;
import com.teamhardwork.kipp.utilities.behavior_event.BehaviorEventListFilterer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recommendation {
    private static final int RISK_LOW = 3;
    private static final int RISK_ELEVATED = 6;
    private static final int RISK_SEVERE = 10;
    private static final String REC_CLEANING_UP = " deserves a star for consistently cleaning up";
    private static final String REC_ON_TASK = " deserves a star for consistently staying on task";
    private static final String REC_RESPECTING_EVERYONE = " deserves a star for always showing respect to everyone";
    private static final String REC_SHOWING_GRATITUDE = " deserves a star for always showing gratitude to everyone";
    private static final String REC_SILENT_REMINDERS = " deserves a star";
    private static final String REC_VOLUNTEERING = " deserves a star for actively volunteering";

    private static final String REC_DRESS_CODE_VIOLATION = " needs to learn the proper dress code";
    private static final String REC_LACK_OF_INTEGRITY = " needs to learn integrity";
    private static final String REC_LATE = " is frequently late to class- contact the parents";
    private static final String REC_TALKING = " frequently talks in class- contact the parents";
    private static final String REC_HORSEPLAY = " must go to detention for horseplay";
    private static final String REC_FIGHTING = " must do to detention for fighting in school- and contact the parents";

    private static Recommendation instance = null;
    private HashMap<Student, String> studentRecs;
    private boolean init;

    protected Recommendation() {
        this.studentRecs = new HashMap<Student, String>();
        this.init = false;
    }

    public static Recommendation getInstance() {
        if (instance == null) {
            instance = new Recommendation();
        }
        return instance;
    }

    public String getRecPerBehavior(Behavior behavior) {
        switch (behavior) {
            case CLEANING_UP:
                return REC_CLEANING_UP;

            case ON_TASK:
                return REC_ON_TASK;

            case RESPECTING_EVERYONE:
                return REC_RESPECTING_EVERYONE;

            case SHOWING_GRATITUDE:
                return REC_SHOWING_GRATITUDE;

            case SILENT_REMINDERS:
                return REC_SILENT_REMINDERS;

            case VOLUNTEERING:
                return REC_VOLUNTEERING;

            case DRESS_CODE_VIOLATION:
                return REC_DRESS_CODE_VIOLATION;

            case LACK_OF_INTEGRITY:
                return REC_LACK_OF_INTEGRITY;

            case LATE:
                return REC_LATE;

            case TALKING:
                return REC_TALKING;

            case HORSEPLAY:
                return REC_HORSEPLAY;

            case FIGHTING:
                return REC_FIGHTING;

            default:
                return null;
        }
    }

    public boolean isInit() { return this.init; }

    public boolean hasRecs(Student student) {
        return studentRecs.containsKey(student);
    }

    public void addRecs(final Student student) {
        this.init = true;

        // this student already has a rec that has not been viewed, ignore add
        if (studentRecs.containsKey(student) == true) return;

        FeedQueries.getStudentFeed(student, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                if (behaviorEvents == null) return;
                Map<Behavior, Integer> behaviorCounts = BehaviorEventListFilterer.getGroupedCount(behaviorEvents);

                Behavior riskyBehavior = null;
                int maxCount = 0, count = 0;
                for(Behavior behavior : behaviorCounts.keySet()) {
                    count = behaviorCounts.get(behavior);
                    if (count >= RISK_ELEVATED && count > maxCount) {
                        maxCount = count;
                        riskyBehavior = behavior;
                    }
                }
                if (riskyBehavior != null) {
                    studentRecs.put(student, getRecPerBehavior(riskyBehavior));
                }
            }
        });

    }

    public void dismissRecs(Student student) {
        studentRecs.remove(student);
    }

    public String getRecs(Student student) {
        return studentRecs.get(student);
    }

}
