package com.teamhardwork.kipp.models;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.teamhardwork.kipp.enums.Discipline;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("SchoolClass")
public class SchoolClass extends ParseObject {
    static final String DISCIPLINE = "discipline";
    static final String NAME = "name";
    static final String TEACHER = "teacher";
    static final String START_TIME = "startTime";
    static final String END_TIME = "endTime";
    public static final String ROSTER = "roster";

    public static SchoolClass findSchoolClass(SchoolClass schoolClass) {
        SchoolClass savedSchoolClass = null;

        ParseQuery<SchoolClass> query = ParseQuery.getQuery(SchoolClass.class);
        query
                .whereEqualTo(DISCIPLINE, schoolClass.getDiscipline().name())
                .whereEqualTo(NAME, schoolClass.getName());
        try {
            savedSchoolClass = query.getFirst();
        } catch (ParseException e) {
        }

        return savedSchoolClass;
    }

    public static SchoolClass findSchoolClassByTeacher(Teacher teacher) throws ParseException {
        // TODO: Add logic to get class by current time.
        ParseQuery<SchoolClass> query = ParseQuery.getQuery(SchoolClass.class);
        query.whereEqualTo(TEACHER, teacher);
        return query.getFirst();
    }

    public static void findSchoolClassByTeacherAsync(Teacher teacher, GetCallback<SchoolClass> callback) {
        ParseQuery<SchoolClass> query = ParseQuery.getQuery(SchoolClass.class);
        query.whereEqualTo(TEACHER, teacher);
        query.getFirstInBackground(callback);
    }

    public void addStudent(Student student) {
        getStudentRelation().add(student);
    }

    public void removeStudent(Student student) {
        getStudentRelation().remove(student);
    }

    public List<Student> getRoster() {
        List<Student> roster = new ArrayList<Student>();
        ParseQuery query = getStudentRelation().getQuery();

        try {
            roster = query.find();
        } catch (ParseException e) {
        }

        return roster;
    }

    public void getClassRosterAsync(FindCallback<Student> callback) {
        ParseQuery query = getStudentRelation().getQuery();
        query.findInBackground(callback);
    }

    public ParseRelation<Student> getStudentRelation() {
        return getRelation(ROSTER);
    }

    public void setClassRoster(List<Student> roster) {
        put(ROSTER, roster);
    }

    /**
     * Note on setting start and end times for classes. The only relevant fields are day and time.
     * Since year is irrelevant, use the epoch as the reference for Thursday.  All times are UTC.
     * <p/>
     * MON: 1970-01-05
     * TUES: 1970-01-06
     * WED: 1970-01-07
     * THURS: 1970-01-01
     * FRI: 1970-01-02
     * SAT: 1970-01-03
     * SUNDAY: 1970-01-04
     * <p/>
     * Format: "1970-01-01 10:45 -0000"
     */
    public void setStartTime(Date startTime) {
        put(START_TIME, startTime);
    }

    public Date getEndTime() {
        return getDate(END_TIME);
    }

    public void setEndTime(Date endTime) {
        put(END_TIME, endTime);
    }

    public Discipline getDiscipline() {
        return Discipline.valueOf(getString(DISCIPLINE));
    }

    public void setDiscipline(Discipline discipline) {
        put(DISCIPLINE, discipline.name());
    }

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        put(NAME, name);
    }

    public Teacher getTeacher() {
        return (Teacher) getParseObject(TEACHER);
    }

    public void setTeacher(Teacher teacher) {
        put(TEACHER, teacher);
    }

    public Date getStartTime() {
        return getDate(START_TIME);
    }

    public static SchoolClass findById(String id) throws ParseException {
        ParseQuery<SchoolClass> query = ParseQuery.getQuery(SchoolClass.class);
        return query.get(id);
    }
}
