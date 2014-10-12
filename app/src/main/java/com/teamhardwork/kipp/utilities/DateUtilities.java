package com.teamhardwork.kipp.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtilities {
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm Z";

    public static Date stringToDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }
}
