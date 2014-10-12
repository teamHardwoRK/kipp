package com.teamhardwork.kipp;

import android.app.Application;

import com.parse.ParseObject;
import com.teamhardwork.kipp.models.Action;
import com.teamhardwork.kipp.models.users.KippUser;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;

import java.util.ArrayList;
import java.util.List;

public class KippApplication extends Application {
    public static final String PARSE_APPLICATION_ID = "6fb5KhXW73bUQKwdAb807wiIt9tROQ2HtHAYmKOq";
    public static final String PARSE_CLIENT_KEY = "y703WGXSG0rY4qvuP1dhM3vn1Qo4efXMMXoxtj12";

    @Override
    public void onCreate() {
        super.onCreate();
        registerParseSubclasses();
    }

    static void registerParseSubclasses() {
        List<Class> classes = new ArrayList<Class>();
        classes.add(Action.class);
        classes.add(KippUser.class);
        classes.add(Teacher.class);
        classes.add(Student.class);

        for(Class subclass: classes) {
            ParseObject.registerSubclass(subclass);
        }
    }
}
