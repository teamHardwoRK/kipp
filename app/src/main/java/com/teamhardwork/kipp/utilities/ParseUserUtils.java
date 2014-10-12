package com.teamhardwork.kipp.utilities;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ParseUserUtils {

    static ParseUser signUp(ParseUser user) {
        try {
            user.signUp();
        } catch (ParseException e) {
        }
        finally {
            return findUserWithUsername(user.getUsername());
        }
    }
    public static ParseUser findUserWithUsername(String username) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        try {
            return query.getFirst();
        } catch (ParseException e) {
            return null;
        }
    }

}
