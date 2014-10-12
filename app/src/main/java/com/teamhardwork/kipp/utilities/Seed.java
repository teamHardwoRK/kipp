package com.teamhardwork.kipp.utilities;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.teamhardwork.kipp.enums.Discipline;
import com.teamhardwork.kipp.enums.Gender;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.KippUser;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;

import java.util.Date;
import java.util.List;

public class Seed {
    static final String TAG = "Seed";

    public static void seedData() throws ParseException {
        Teacher teacherOne = createTeacher("billwalsh", "Bill", "Walsh", Gender.MALE, new Date(), null);
        Teacher teacherTwo = createTeacher("donshula", "Don", "Shula", Gender.MALE, new Date(), null);

        Student studentOne = createStudent("tombrady", "Tom", "Brady", Gender.MALE, new Date(), null);
        Student studentTwo = createStudent("patmanning", "Patricia", "Manning", Gender.FEMALE, new Date(), null);
        Student studentThree = createStudent("joemontana", "Joe", "Montana", Gender.MALE, new Date(), null);
        Student studentFour = createStudent("drewbrees", "Drew", "Breesymore", Gender.FEMALE, new Date(), null);

        SchoolClass classOne = createClass("algebra", Discipline.MATH, teacherOne, "1970-01-07 12:00 -0000", "1970-01-07 13:00 -0000");
        SchoolClass classTwo = createClass("composition", Discipline.ENGLISH, teacherTwo, "1970-01-02 10:00 -0000", "1970-01-02 11:00 -0000");

        classOne.addStudent(studentOne);
        classOne.addStudent(studentTwo);
        classOne.addStudent(studentThree);
        classOne.save();

        classTwo.addStudent(studentTwo);
        classTwo.addStudent(studentThree);
        classTwo.addStudent(studentFour);
        classTwo.save();

        classOne.removeStudent(studentThree);
        classOne.save();

        // Sample Queries - All synchronous.  Create new methods and use findInBackground() for Async calls.
        List<Student> classOneRoster = classOne.getRoster();
        List<SchoolClass> studentOneClasses = studentOne.getClassList();

        Log.d(TAG, "class size: " + classOneRoster.size());
        Log.d(TAG, "student class list size: " + studentOneClasses.size());
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
