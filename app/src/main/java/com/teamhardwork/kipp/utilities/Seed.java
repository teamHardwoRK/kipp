package com.teamhardwork.kipp.utilities;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.teamhardwork.kipp.enums.Discipline;
import com.teamhardwork.kipp.enums.Gender;
import com.teamhardwork.kipp.models.SchoolClass;
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
        Teacher teacherOne = createTeacher("billwalsh", "Bill", "Walsh", Gender.MALE, new Date(), null);
        Teacher teacherTwo = createTeacher("donshula", "Don", "Shula", Gender.MALE, new Date(), null);

        Student studentOne = createStudent("tombrady", "Tom", "Brady", Gender.MALE, new Date(), null);
        Student studentTwo = createStudent("patmanning", "Patricia", "Manning", Gender.FEMALE, new Date(), null);
        Student studentThree = createStudent("joemontana", "Joe", "Montana", Gender.MALE, new Date(), null);
        Student studentFour = createStudent("drewbrees", "Drew", "Breesymore", Gender.FEMALE, new Date(), null);

        SchoolClass classOne = createClass("algebra", Discipline.MATH, teacherOne, "1970-01-07 12:00 -0000", "1970-01-07 13:00 -0000");
        SchoolClass classTwo = createClass("composition", Discipline.ENGLISH, teacherOne, "1970-01-02 10:00 -0000", "1970-01-02 11:00 -0000");
    }

    public static SchoolClass createClass(String name, Discipline discipline, Teacher teacher, String startTime, String endTime) throws ParseException {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setTeacher(teacher);
        schoolClass.setDiscipline(discipline);
        schoolClass.setName(name);
        schoolClass.setStartTime(DateUtilities.stringToDate(startTime));
        schoolClass.setEndTime(DateUtilities.stringToDate(endTime));

        SchoolClass savedSchoolClass = SchoolClass.findSchoolClass(schoolClass);

        if(savedSchoolClass == null) {
            schoolClass.save();
            savedSchoolClass = SchoolClass.findSchoolClass(schoolClass);
        }
        return savedSchoolClass;
    }

    public static Teacher createTeacher(String username, String firstName, String lastName, Gender gender, Date birthdate, String telephoneNumber) {
        ParseUser teacherUser = new ParseUser();
        teacherUser.setUsername(username);
        teacherUser.setPassword(KippUser.DEFAULT_PASSWORD);
        teacherUser = ParseUserUtils.signUp(teacherUser);

        Teacher teacher = new Teacher(teacherUser, firstName, lastName, gender, birthdate, telephoneNumber);
        Teacher savedTeacher = KippUser.findUser(teacher, KippUser.TEACHER_CLASS_NAME);

        if(savedTeacher == null) {
            try {
                teacher.save();
                savedTeacher = KippUser.findUser(teacher, KippUser.TEACHER_CLASS_NAME);
            } catch (ParseException e) {}
        }
        return savedTeacher;
    }

    public static Student createStudent(String username, String firstName, String lastName, Gender gender, Date birthdate, String telephoneNumber) {
        ParseUser studentUser = new ParseUser();
        studentUser.setUsername(username);
        studentUser.setPassword(KippUser.DEFAULT_PASSWORD);
        studentUser = ParseUserUtils.signUp(studentUser);

        Student student = new Student(studentUser, firstName, lastName, gender, birthdate, telephoneNumber);
        Student savedStudent = KippUser.findUser(student, KippUser.STUDENT_CLASS_NAME);

        if(savedStudent == null) {
            try {
                student.save();
                savedStudent = KippUser.findUser(student, KippUser.STUDENT_CLASS_NAME);
            } catch (ParseException e) {}
        }
        return savedStudent;
    }
}
