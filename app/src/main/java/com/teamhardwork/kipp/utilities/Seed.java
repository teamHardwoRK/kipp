package com.teamhardwork.kipp.utilities;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamhardwork.kipp.enums.Gender;
import com.teamhardwork.kipp.models.users.KippUser;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;

import java.util.Date;

public class Seed {
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";

    public static void seedActions() {
//        Student student = new Student();
//        student.setUsername("Foo2");
//        student.setFirstName("foo");
//        student.setLastName("bar");
//        student.setPassword(KippUser.DEFAULT_PASSWORD);
//        try {
//            student.signUp();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.whereEqualTo("username", "Foo");
//        ParseUser user = null;
//        try {
//            user = query.getFirst();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Student student = (Student) user;
//
//        Action action1 = new Action();
//        action1.setNote("Hey");
//        action1.setType(ActionType.EMAIL);
//        action1.setOccurredAt(new Date());
//        action1.setStudent(student);
//
//        try {
//            action1.save();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
//        query1.whereEqualTo("username", "Foo");
//        ParseUser user = null;
//        try {
//            user = query1.getFirst();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Student student = (Student) user;
//
//
//        ParseQuery<Action> query = ParseQuery.getQuery("Action");
//        query.include("student");
//        Action action = null;
//        try {
//            action = query.getFirst();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Log.d("blah", action.toString());
    }

    public static void seedData() throws ParseException {
        createTeacher("billwalsh", "Bill", "Walsh", Gender.MALE, new Date(), null);
        createTeacher("donshula", "Don", "Shula", Gender.MALE, new Date(), null);

        createStudent("tombrady", "Tom", "Brady", Gender.MALE, new Date(), null);
        createStudent("patmanning", "Patricia", "Manning", Gender.FEMALE, new Date(), null);
        createStudent("joemontana", "Joe", "Montana", Gender.MALE, new Date(), null);
        createStudent("drewbrees", "Drew", "Breesymore", Gender.FEMALE, new Date(), null);
    }

    public static void createTeacher(String username, String firstName, String lastName, Gender gender, Date birthdate, String telephoneNumber) {
        ParseUser teacherUser = new ParseUser();
        teacherUser.setUsername(username);
        teacherUser.setPassword(KippUser.DEFAULT_PASSWORD);
        teacherUser = ParseUserUtils.signUp(teacherUser);

        Teacher teacher = new Teacher(teacherUser, firstName, lastName, gender, birthdate, telephoneNumber);
        if(!KippUser.doesExist(teacher, KippUser.TEACHER_CLASS_NAME)) {
            try {
                teacher.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createStudent(String username, String firstName, String lastName, Gender gender, Date birthdate, String telephoneNumber) {
        ParseUser studentUser = new ParseUser();
        studentUser.setUsername(username);
        studentUser.setPassword(KippUser.DEFAULT_PASSWORD);
        studentUser = ParseUserUtils.signUp(studentUser);

        Student student = new Student(studentUser, firstName, lastName, gender, birthdate, telephoneNumber);
        if(!KippUser.doesExist(student, KippUser.STUDENT_CLASS_NAME)) {
            try {
                student.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
