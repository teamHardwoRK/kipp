package com.teamhardwork.kipp;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
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
    public static final String PARSE_REST_API_KEY = "5yTa9Zu5tc4pec64UP9k2CGn1U7bKQmBZsjlSWoi";
    public static final String TAG = "com.teamhardwork.kipp.kippApplication";

    Teacher teacher;
    SchoolClass schoolClass;

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

        for (Class subclass : classes) {
            ParseObject.registerSubclass(subclass);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        registerParseSubclasses();
        registerPushNotifications();
    }

    public void registerPushNotifications() {
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Successfully subcribed to broadcast channel.");
                } else {
                    Log.d(TAG, "Failed to subscribe to push notifications.");
                }
            }
        });
    }

    public void setTeacher() {
        if (teacher == null) {
            ParseUser user = ParseUser.getCurrentUser();

            if (user != null) {
                try {
                    teacher = Teacher.findTeacher(user);
                } catch (ParseException e) {
                    Toast.makeText(this, "User does not have a teacher account.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public Teacher getTeacher() {
        if (teacher == null) {
            setTeacher();
        }
        return teacher;
    }

    public void setSchoolClass() {
        if (schoolClass == null) {
            try {
                schoolClass = SchoolClass.findSchoolClassByTeacher(teacher);
            } catch (ParseException e) {
                Toast.makeText(this, "Teacher does not have a class.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public SchoolClass getSchoolClass() {
        if (schoolClass == null) {
            setSchoolClass();
        }
        return schoolClass;
    }

    public static Typeface getDefaultTypeFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/VarelaRound-Regular.otf");
    }
}
