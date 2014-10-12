package com.teamhardwork.kipp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.teamhardwork.kipp.enums.ActionType;
import com.teamhardwork.kipp.models.users.Student;

import java.util.Date;

@ParseClassName("Action")
public class Action extends ParseObject {
    static final String TYPE = "type";
    private static final String NOTES = "notes";
    private static final String OCCURRED_AT = "occurredAt";
    private static final String STUDENT = "student";

    public Action() {
        // Default constructor is required by ParseObject.
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

    public String getNote() {
        return getString(NOTES);
    }

    public void setNote(String note) {
        put(NOTES, note);
    }

    public void setStudent(Student student) {
        put(STUDENT, student);
    }

    public Student getStudent() {
        return (Student) getParseObject(STUDENT);
    }
}
