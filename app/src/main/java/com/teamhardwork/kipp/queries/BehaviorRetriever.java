package com.teamhardwork.kipp.queries;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;

/**
 * General behavior queries
 */
public class BehaviorRetriever {
    public static void findBySchoolClass(SchoolClass schoolClass,
                                         FindCallback<BehaviorEvent> callback)
            throws ParseException {
        ParseQuery<BehaviorEvent> query = ParseQuery.getQuery(BehaviorEvent.class);
        query.whereEqualTo(BehaviorEvent.SCHOOL_CLASS, schoolClass);
        query.findInBackground(callback);
    }
}
