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
    private static final String REC_LACK_OF_INTEGRITY = " needs to have a talk about integrity";
    private static final String REC_LATE = " is frequently late to class- contact the parents";
    private static final String REC_TALKING = " frequently talks in class- contact the parents";
    private static final String REC_HORSEPLAY = " must go to detention for horseplay";
    private static final String REC_FIGHTING = " must do to detention for fighting in school- and contact the parents";

    private static Recommendation instance = null;
    private HashMap<Student, RecommendationData> studentRecs;
    private boolean init;

    public enum RecommendationType {
        GOOD(1),
        BAD(2);
        private int value;
        private RecommendationType(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    public class RecommendationData {
        String rec;
        RecommendationType recType;
        public RecommendationData(String rec, RecommendationType recType) {
            this.rec = rec;
            this.recType = recType;
        }
        public String toString() { return this.rec; }
        public RecommendationType getRecType() { return this.recType; }
    }

    protected Recommendation() {
        this.studentRecs = new HashMap<Student, RecommendationData>();
        this.init = false;
    }

    public static Recommendation getInstance() {
        if (instance == null) {
            instance = new Recommendation();
        }
        return instance;
    }

    public RecommendationData getRecPerBehavior(Behavior behavior) {
        switch (behavior) {
            case CLEANING_UP:
                return new RecommendationData(REC_CLEANING_UP, RecommendationType.GOOD);

            case ON_TASK:
                return new RecommendationData(REC_ON_TASK, RecommendationType.GOOD);

            case RESPECTING_EVERYONE:
                return new RecommendationData(REC_RESPECTING_EVERYONE, RecommendationType.GOOD);

            case SHOWING_GRATITUDE:
                return new RecommendationData(REC_SHOWING_GRATITUDE, RecommendationType.GOOD);

            case SILENT_REMINDERS:
                return new RecommendationData(REC_SILENT_REMINDERS, RecommendationType.GOOD);

            case VOLUNTEERING:
                return new RecommendationData(REC_VOLUNTEERING, RecommendationType.GOOD);

            case DRESS_CODE_VIOLATION:
                return new RecommendationData(REC_DRESS_CODE_VIOLATION, RecommendationType.BAD);

            case LACK_OF_INTEGRITY:
                return new RecommendationData(REC_LACK_OF_INTEGRITY, RecommendationType.BAD);

            case LATE:
                return new RecommendationData(REC_LATE, RecommendationType.BAD);

            case TALKING:
                return new RecommendationData(REC_TALKING, RecommendationType.BAD);

            case HORSEPLAY:
                return new RecommendationData(REC_HORSEPLAY, RecommendationType.BAD);

            case FIGHTING:
                return new RecommendationData(REC_FIGHTING, RecommendationType.BAD);

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

    public RecommendationData getRecs(Student student) {
        return studentRecs.get(student);
    }

}
