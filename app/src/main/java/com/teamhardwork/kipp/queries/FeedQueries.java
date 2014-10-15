package com.teamhardwork.kipp.queries;

import com.teamhardwork.kipp.models.Action;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;

import java.util.List;

/**
 *  Queries that provide data for Behavior Feeds.
 */
public class FeedQueries {
    List<BehaviorEvent> getClassFeed(Teacher teacher) {
        List<BehaviorEvent> eventList = null;

        return eventList;
    }

    List<BehaviorEvent> getStudentFeed(Student student) {
        List<BehaviorEvent> eventList = null;

        return eventList;
    }

    List<Action> getStudentActionLog(Student student) {
        List<Action> actionList = null;

        return actionList;
    }
}
