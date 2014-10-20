package com.teamhardwork.kipp.models.users;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamhardwork.kipp.enums.Gender;
import com.teamhardwork.kipp.models.SchoolClass;

import java.util.Date;
import java.util.List;

@ParseClassName(KippUser.STUDENT_CLASS_NAME)
public class Student extends KippUser {
    private static final String POINTS = "points";
    List<Parent> parentList;
    List<SchoolClass> classList;

    public Student() {
    }

    public Student(ParseUser user, String email, String firstName, String lastName, Gender gender, Date dateOfBirth, String telephoneNumber) {
        super(user, email, firstName, lastName, gender, dateOfBirth, telephoneNumber);
    }

    public static void findStudent(ParseUser parseUser, FindCallback<Student> callback) {
        ParseQuery<Student> query = ParseQuery.getQuery(Student.class);
        query.whereEqualTo(USER, parseUser);
        query.findInBackground(callback);
    }

    public static void getStudentAsync(String studentId, GetCallback<Student> callback) {
        ParseQuery<Student> query = ParseQuery.getQuery(Student.class);
        // First try to find from the cache and only then go to network
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        // Execute the query to find the object with ID
        query.getInBackground(studentId, callback);
    }

    public List<SchoolClass> getClassList() {
        ParseQuery<SchoolClass> query = ParseQuery.getQuery(SchoolClass.class);
        query.whereEqualTo(SchoolClass.ROSTER, this);
        List<SchoolClass> classes = null;

        try {
            classes = query.find();
        } catch (ParseException e) {
        }

        return classes;
    }

    public int getPoints() {
        return getInt(POINTS);
    }

    public void addPoints(int newPoints) {
        put(POINTS, getPoints() + newPoints);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (!getObjectId().equals(student.getObjectId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getObjectId().hashCode();
    }
}
