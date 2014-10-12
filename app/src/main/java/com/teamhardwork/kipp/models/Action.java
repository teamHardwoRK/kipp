package com.teamhardwork.kipp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.teamhardwork.kipp.enums.ActionType;
import com.teamhardwork.kipp.models.users.Student;

import java.util.Date;

@ParseClassName("Action")
public class Action extends ParseObject {
    static final String TYPE = "type";
    static final String NOTES = "notes";
    static final String OCCURRED_AT = "occurredAt";
    static final String STUDENT = "student";
    static final String BEHAVIOR_EVENT = "behaviorEvent";
    static final String SCHOOL_CLASS = "schoolClass";

    public SchoolClass getSchoolClass() {
        return (SchoolClass) getParseObject(SCHOOL_CLASS);
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        put(SCHOOL_CLASS, schoolClass);
    }

    public ActionType getType() {
        return ActionType.valueOf(getString(TYPE));
    }

    public void setType(ActionType type) {
        put(TYPE, type.toString());
    }

    public Date getOccurredAt() {
        return getDate(OCCURRED_AT);
    }

    public void setOccurredAt(Date occurredAt) {
        put(OCCURRED_AT, occurredAt);
    }

    public String getNotes() {
        return getString(NOTES);
    }

    public void setNotes(String notes) {
        put(NOTES, notes);
    }

    public void setStudent(Student student) {
        put(STUDENT, student);
    }

    public Student getStudent() {
        return (Student) getParseObject(STUDENT);
    }

    public BehaviorEvent getBehaviorEvent() {
        return (BehaviorEvent) getParseObject(BEHAVIOR_EVENT);
    }

    public void setBehaviorEvent(BehaviorEvent event) {
        if(event != null) {
            put(BEHAVIOR_EVENT, event);
        }
    }
}
