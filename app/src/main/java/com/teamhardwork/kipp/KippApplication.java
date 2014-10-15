package com.teamhardwork.kipp;

import android.app.Application;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.teamhardwork.kipp.models.Action;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.SchoolClass;
import com.teamhardwork.kipp.models.users.KippUser;
import com.teamhardwork.kipp.models.users.Parent;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;

import java.util.ArrayList;
import java.util.List;

public class KippApplication extends Application {
    public static final String PARSE_APPLICATION_ID = "6fb5KhXW73bUQKwdAb807wiIt9tROQ2HtHAYmKOq";
    public static final String PARSE_CLIENT_KEY = "y703WGXSG0rY4qvuP1dhM3vn1Qo4efXMMXoxtj12";
    public static final String TAG = "kippApplication";
    public static final String APP_PACKAGE = "com.teamhardwork.kipp";

    Teacher teacher;

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        registerParseSubclasses();
    }

    // All models saved to Parse must be registered.
    static void registerParseSubclasses() {
        List<Class> classes = new ArrayList<Class>();
        classes.add(KippUser.class);
        classes.add(Teacher.class);
        classes.add(Student.class);
        classes.add(Parent.class);
        classes.add(SchoolClass.class);
        classes.add(Action.class);
        classes.add(BehaviorEvent.class);

        for(Class subclass: classes) {
            ParseObject.registerSubclass(subclass);
        }
    }

    public void setTeacher() {
        if(teacher == null) {
            ParseUser user = ParseUser.getCurrentUser();

            if(user != null) {
                try {
                    teacher = Teacher.findTeacher(user);
                } catch (ParseException e) {
                    Toast.makeText(this, "User does not have a teacher account.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public Teacher getTeacher() {
        if(teacher == null) {
            setTeacher();
        }
        return teacher;
    }
}
