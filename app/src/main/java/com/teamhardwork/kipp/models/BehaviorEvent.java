package com.teamhardwork.kipp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.models.users.Student;

import java.util.Date;

@ParseClassName("BehaviorEvent")
public class BehaviorEvent extends ParseObject {
    static final String STUDENT = "student";
    static final String SCHOOL_CLASS = "schoolClass";
    static final String BEHAVIOR = "behavior";
    static final String OCCURRED_AT = "occurredAt";
    static final String NOTES = "notes";

    public Student getStudent() {
        return (Student) getParseObject(STUDENT);
    }

    public void setStudent(Student student) {
        put(STUDENT, student);
    }

    public SchoolClass getSchoolClass() {
        return (SchoolClass) getParseObject(SCHOOL_CLASS);
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        put(SCHOOL_CLASS, schoolClass);
    }

    public Behavior getBehavior() {
        return Behavior.valueOf(getString(BEHAVIOR));
    }

    public void setBehavior(Behavior behavior) {
        put(BEHAVIOR, behavior.name());
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
        if(notes == null) {
            notes = "";
        }

        put(NOTES, notes);
    }
}
