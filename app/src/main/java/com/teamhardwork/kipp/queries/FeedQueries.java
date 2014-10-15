package com.teamhardwork.kipp.queries;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.teamhardwork.kipp.models.Action;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.Student;

import java.util.List;

/**
 *  Queries that provide data for Behavior Feeds.
 */
public class FeedQueries {
    public static List<BehaviorEvent> getClassFeed(SchoolClass schoolClass) throws ParseException {
        ParseQuery<BehaviorEvent> query = ParseQuery.getQuery(BehaviorEvent.class);
        query.whereEqualTo(BehaviorEvent.SCHOOL_CLASS, schoolClass);
        query.include(BehaviorEvent.STUDENT);
        return query.find();
    }

    public static void getStudentFeed(Student student, FindCallback<BehaviorEvent> callback) {
        ParseQuery<BehaviorEvent> query = ParseQuery.getQuery(BehaviorEvent.class);
        query.whereEqualTo(BehaviorEvent.STUDENT, student);
        query.include(BehaviorEvent.STUDENT);
        query.findInBackground(callback);
    }

    public static List<Action> getStudentActionLog(Student student) {
        List<Action> actionList = null;

        return actionList;
    }
}
