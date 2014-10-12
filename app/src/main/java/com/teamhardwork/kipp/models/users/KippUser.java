package com.teamhardwork.kipp.models.users;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamhardwork.kipp.enums.Gender;

import java.util.Date;

@ParseClassName("KippUser")
public class KippUser extends ParseObject {
    public static final String TEACHER_CLASS_NAME = "Teacher";
    public static final String STUDENT_CLASS_NAME = "Student";
    public static final String DEFAULT_PASSWORD = "aaa";

    static final String USER = "user";
    static final String DATE_OF_BIRTH = "dateOfbirth";
    static final String TELEPHONE_NUMBER = "telephoneNumber";
    static final String FIRST_NAME = "firstName";
    static final String LAST_NAME = "lastName";
    static final String GENDER = "gender";

    public KippUser() {
    }

    public KippUser(ParseUser user,
                    String firstName,
                    String lastName,
                    Gender gender,
                    Date dateOfBirth,
                    String telephoneNumber) {

        setUser(user);
        setFirstName(firstName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);

        if(telephoneNumber == null) {
            telephoneNumber = "1234567890";
        }
        setTelephoneNumber(telephoneNumber);

        setGender(gender);
    }

    public static <T extends KippUser> boolean doesExist(T user, String parseClassName) {
        boolean doesExist = false;

        ParseQuery<T> query = ParseQuery.getQuery(parseClassName);
        query
            .whereEqualTo(FIRST_NAME, user.getFirstName())
            .whereEqualTo(LAST_NAME, user.getLastName());

        try {
            if(query.find().size() > 0) {
                doesExist = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return doesExist;
    }

    // The following fields are already in ParseUser: email, username
    public Date getDateOfBirth() {
        return getDate(DATE_OF_BIRTH);
    }

    public void setDateOfBirth(Date dateOfBirth) {
        put(DATE_OF_BIRTH, dateOfBirth);
    }

    public String getTelephonNumber() {
        return getString(TELEPHONE_NUMBER);
    }

    public void setTelephoneNumber(String telephoneNumber) {
        put(TELEPHONE_NUMBER, telephoneNumber);
    }

    public String getLastName() {
        return getString(LAST_NAME);
    }

    public void setLastName(String lastName) {
        put(LAST_NAME, lastName);
    }

    public String getFirstName() {
        return getString(FIRST_NAME);
    }

    public void setFirstName(String firstName) {
        put(FIRST_NAME, firstName);
    }

    public Gender getGender() {
        return Gender.valueOf(getString(GENDER));
    }

    public void setGender(Gender gender) {
        put(GENDER, gender.name());
    }

    public ParseUser getUser(ParseUser user) {
        return getParseUser(USER);
    }

    public void setUser(ParseUser user) {
        put(USER, user);
    }
}
