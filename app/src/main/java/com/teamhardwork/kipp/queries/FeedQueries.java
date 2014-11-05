package com.teamhardwork.kipp.queries;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.teamhardwork.kipp.models.Action;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Queries that provide data for Behavior Feeds.
 */
public class FeedQueries {

    private static final int QUERY_LIMIT = 200;
    public static final int MOST_RECENT_MAX = 5;

    public static void getLatestClassEvents(SchoolClass schoolClass, List<BehaviorEvent> eventList,
                                            FindCallback<BehaviorEvent> callback) {
        ParseQuery<BehaviorEvent> query = ParseQuery.getQuery(BehaviorEvent.class);
        if (eventList.size() > 0) {
            query.whereEqualTo(BehaviorEvent.SCHOOL_CLASS, schoolClass);
            query.whereGreaterThan(BehaviorEvent.OCCURRED_AT, eventList.get(0).getOccurredAt());
            query.include(BehaviorEvent.STUDENT);
            query.orderByAscending(BehaviorEvent.OCCURRED_AT).setLimit(QUERY_LIMIT);
            query.findInBackground(callback);
        }
    }

    public static void getLatestStudentEvents(Student student, List<BehaviorEvent> eventList,
                                              FindCallback<BehaviorEvent> callback) {
        if (eventList.size() > 0) {
            ParseQuery<BehaviorEvent> query = ParseQuery.getQuery(BehaviorEvent.class);
            query.whereEqualTo(BehaviorEvent.STUDENT, student);
            query.whereGreaterThan(BehaviorEvent.OCCURRED_AT, eventList.get(0).getOccurredAt());
            query.include(BehaviorEvent.STUDENT);
            query.orderByAscending(BehaviorEvent.OCCURRED_AT).setLimit(QUERY_LIMIT);
            query.findInBackground(callback);
        }
    }

    public static void getClassFeed(SchoolClass schoolClass, FindCallback<BehaviorEvent> callback) {
        ParseQuery<BehaviorEvent> query = ParseQuery.getQuery(BehaviorEvent.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.whereEqualTo(BehaviorEvent.SCHOOL_CLASS, schoolClass);
        query.include(BehaviorEvent.STUDENT);
        query.orderByDescending(BehaviorEvent.OCCURRED_AT).setLimit(QUERY_LIMIT);
        query.findInBackground(callback);
    }

    public static void getStudentFeed(Student student, FindCallback<BehaviorEvent> callback) {
        ParseQuery<BehaviorEvent> query = ParseQuery.getQuery(BehaviorEvent.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.whereEqualTo(BehaviorEvent.STUDENT, student);
        query.include(BehaviorEvent.STUDENT);
        query.orderByDescending(BehaviorEvent.OCCURRED_AT).setLimit(QUERY_LIMIT);
        query.findInBackground(callback);
    }

    public static void getStudentFeedMostRecent(Student student, FindCallback<BehaviorEvent> callback) {
        ParseQuery<BehaviorEvent> query = ParseQuery.getQuery(BehaviorEvent.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.whereEqualTo(BehaviorEvent.STUDENT, student);
        query.include(BehaviorEvent.STUDENT);
        query.orderByDescending(BehaviorEvent.OCCURRED_AT).setLimit(MOST_RECENT_MAX);
        query.findInBackground(callback);
    }

    public static void getStudentActionLog(Student student, FindCallback<Action> callback) {
        ParseQuery<Action> query = ParseQuery.getQuery(Action.class);
        query.whereEqualTo(Action.STUDENT, student);
        query.include(Action.STUDENT);
        query.orderByDescending(Action.OCCURRED_AT).setLimit(QUERY_LIMIT);
        query.findInBackground(callback);
    }

    public static void getLatestActionLog(Student student, List<Action> actionList, FindCallback<Action> callback) {
        ParseQuery<Action> query = ParseQuery.getQuery(Action.class);
        query.whereEqualTo(Action.STUDENT, student);
        query.whereGreaterThan(Action.OCCURRED_AT, actionList.get(0).getOccurredAt());
        query.include(Action.STUDENT);
        query.orderByAscending(Action.OCCURRED_AT).setLimit(QUERY_LIMIT);
        query.findInBackground(callback);
    }

    static List<String> extractObjectIds(List<? extends ParseObject> parseObjectsList) {
        List<String> objectIds = new ArrayList<String>();

        for (ParseObject parseObject : parseObjectsList) {
            objectIds.add(parseObject.getObjectId());
        }
        return objectIds;
    }
}
