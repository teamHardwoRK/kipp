package com.teamhardwork.kipp.models.users;

import com.parse.ParseClassName;
import com.parse.ParseUser;
import com.teamhardwork.kipp.enums.Gender;
import com.teamhardwork.kipp.models.SchoolClass;

import java.util.Date;
import java.util.List;

@ParseClassName(KippUser.STUDENT_CLASS_NAME)
public class Student extends KippUser {
    List<Parent> parentList;
    List<SchoolClass> classList;

    public Student() {
    }

    public Student(ParseUser user, String firstName, String lastName, Gender gender, Date dateOfBirth, String telephoneNumber) {
        super(user, firstName, lastName, gender, dateOfBirth, telephoneNumber);
    }
}
