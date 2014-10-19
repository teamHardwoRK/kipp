package com.teamhardwork.kipp.utilities;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.teamhardwork.kipp.enums.ActionType;
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.enums.Discipline;
import com.teamhardwork.kipp.enums.Gender;
import com.teamhardwork.kipp.models.Action;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.KippUser;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;

import java.util.Date;
import java.util.List;

public class Seed {
    static final String TAG = "Seed";

    public static void seedHugoData() {
    }

    public static void seedRaymondData() {
    }

    public static void seedKevinData() throws ParseException {
        /** IDEMPOTENT INSTRUCTIONS **/
        Teacher teacherOne = createTeacher("billwalsh", "bill@49ers.com", "Bill", "Walsh", Gender.MALE, new Date(), null);
        Teacher teacherTwo = createTeacher("donshula", "don@dolphins.com", "Don", "Shula", Gender.MALE, new Date(), null);

        Student studentOne = createStudent("tombrady", "tom@patriots.com", "Tom", "Brady", Gender.MALE, new Date(), null);
        Student studentTwo = createStudent("patmanning", "pat@broncos.com", "Patricia", "Manning", Gender.FEMALE, new Date(), null);
        Student studentThree = createStudent("joemontana", "joe@49ers.com", "Joe", "Montana", Gender.MALE, new Date(), null);
        Student studentFour = createStudent("drewbrees", "drew@saints.com", "Drew", "Breesymore", Gender.FEMALE, new Date(), null);

        SchoolClass classOne = createClass("algebra", Discipline.MATH, teacherOne, "1970-01-07 12:00 -0000", "1970-01-07 13:00 -0000");
        SchoolClass classTwo = createClass("composition", Discipline.ENGLISH, teacherTwo, "1970-01-02 10:00 -0000", "1970-01-02 11:00 -0000");

        classOne.addStudent(studentOne);
        classOne.addStudent(studentTwo);
        classOne.addStudent(studentThree);
        classOne.addStudent(studentFour);
        classOne.save();

        classTwo.addStudent(studentOne);
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

        /** NON-IDEMPOTENT INSTRUCTIONS - DUPLICATE WARNING **/

//        // Behavior Events
//        BehaviorEvent eventOne = createBehaviorEvent(studentOne, classOne, Behavior.CLEANING_UP, new Date(), "Good Job!");
//        eventOne.save();
//        BehaviorEvent eventTwo = createBehaviorEvent(studentOne, classOne, Behavior.DRESS_CODE_VIOLATION, new Date(), null);
//        eventTwo.save();
//        BehaviorEvent eventThree = createBehaviorEvent(studentTwo, classTwo, Behavior.DRESS_CODE_VIOLATION, new Date(), "Are you blind?!");
//        eventThree.save();
//        BehaviorEvent eventFour = createBehaviorEvent(studentThree, classOne, Behavior.LACK_OF_INTEGRITY, new Date(), "Despicable!");
//        eventFour.save();
//        BehaviorEvent eventFive = createBehaviorEvent(studentOne, classOne, Behavior.ON_TASK, new Date(), "That's what I'm talking 'bout!");
//        eventFive.save();
//        BehaviorEvent eventSix = createBehaviorEvent(studentTwo, classOne, Behavior.LATE, new Date(), null);
//        eventSix.save();
//        BehaviorEvent eventSeven = createBehaviorEvent(studentOne, classOne, Behavior.TALKING, new Date(), "Shut it!");
//        eventSeven.save();

//        // Actions
//        Action actionOne = createAction(studentOne, classOne, ActionType.EMAIL, new Date(), null, "Gotta show some love.");
//        actionOne.save();
//        Action actionTwo = createAction(studentOne, classOne, ActionType.PARENT_CALL, new Date(), null, "Damn brat.");
//        actionTwo.save();
//        Action actionThree = createAction(studentOne, classTwo, ActionType.NOTE, new Date(), null, "What is he thinking?");
//        actionThree.save();
//        Action actionFour = createAction(studentFour, classOne, ActionType.PARENT_CALL, new Date(), null, "She doesn't pay attention.");
//        actionFour.save();
    }

    public static Action createAction(Student student, SchoolClass schoolClass, ActionType type, Date occurredAt, BehaviorEvent event, String notes) {
        Action action = new Action();
        action.setStudent(student);
        action.setSchoolClass(schoolClass);
        action.setType(type);
        action.setOccurredAt(occurredAt);
        action.setBehaviorEvent(event);
        action.setNotes(notes);

        return action;
    }

    public static BehaviorEvent createBehaviorEvent(Student student, SchoolClass schoolClass, Behavior behavior, Date occurredAt, String notes) {
        BehaviorEvent event = new BehaviorEvent();
        event.setStudent(student);
        event.setSchoolClass(schoolClass);
        event.setBehavior(behavior);
        event.setOccurredAt(occurredAt);
        event.setNotes(notes);

        return event;
    }

    public static SchoolClass createClass(String name, Discipline discipline, Teacher teacher, String startTime, String endTime) throws ParseException {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setTeacher(teacher);
        schoolClass.setDiscipline(discipline);
        schoolClass.setName(name);
        schoolClass.setStartTime(DateUtilities.stringToDate(startTime));
        schoolClass.setEndTime(DateUtilities.stringToDate(endTime));

        SchoolClass savedSchoolClass = SchoolClass.findSchoolClass(schoolClass);

        if (savedSchoolClass == null) {
            schoolClass.save();
            savedSchoolClass = SchoolClass.findSchoolClass(schoolClass);
        }
        return savedSchoolClass;
    }

    public static Teacher createTeacher(String username, String email, String firstName, String lastName, Gender gender, Date birthdate, String telephoneNumber) {
        ParseUser teacherUser = new ParseUser();
        teacherUser.setUsername(username);
        teacherUser.setPassword(KippUser.DEFAULT_PASSWORD);
        teacherUser = ParseUserUtils.signUp(teacherUser);

        Teacher teacher = new Teacher(teacherUser, email, firstName, lastName, gender, birthdate, telephoneNumber);
        Teacher savedTeacher = KippUser.findUser(teacher, KippUser.TEACHER_CLASS_NAME);

        if (savedTeacher == null) {
            try {
                teacher.save();
                savedTeacher = KippUser.findUser(teacher, KippUser.TEACHER_CLASS_NAME);
            } catch (ParseException e) {
            }
        }
        return savedTeacher;
    }

    public static Student createStudent(String username, String email, String firstName, String lastName, Gender gender, Date birthdate, String telephoneNumber) {
        ParseUser studentUser = new ParseUser();
        studentUser.setUsername(username);
        studentUser.setPassword(KippUser.DEFAULT_PASSWORD);
        studentUser = ParseUserUtils.signUp(studentUser);

        Student student = new Student(studentUser, email, firstName, lastName, gender, birthdate, telephoneNumber);
        Student savedStudent = KippUser.findUser(student, KippUser.STUDENT_CLASS_NAME);

        if (savedStudent == null) {
            try {
                student.save();
                savedStudent = KippUser.findUser(student, KippUser.STUDENT_CLASS_NAME);
            } catch (ParseException e) {
            }
        }
        return savedStudent;
    }
}
