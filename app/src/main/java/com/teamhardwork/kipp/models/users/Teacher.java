package com.teamhardwork.kipp.models.users;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamhardwork.kipp.enums.Gender;
import com.teamhardwork.kipp.models.SchoolClass;

import java.util.Date;
import java.util.List;

@ParseClassName(KippUser.TEACHER_CLASS_NAME)
public class Teacher extends KippUser {
    List<SchoolClass> classList;

    public Teacher() {
    }

    public Teacher(ParseUser user, String firstName, String lastName, Gender gender, Date dateOfBirth, String telephoneNumber) {
        super(user, firstName, lastName, gender, dateOfBirth, telephoneNumber);
    }

    public static void findTeacher(ParseUser parseUser, GetCallback<Teacher> callback) {
        ParseQuery<Teacher> query = ParseQuery.getQuery(Teacher.class);
        query.whereEqualTo(USER, parseUser);
        query.getFirstInBackground(callback);
    }
}
