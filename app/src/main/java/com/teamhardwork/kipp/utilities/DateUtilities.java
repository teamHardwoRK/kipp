package com.teamhardwork.kipp.utilities;

import android.text.format.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.Period;

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

    public static String timestampAge (Date timestamp) {
        DateTime start = new DateTime(timestamp);
        DateTime end = new DateTime();

        Period period = new Period(start, end);

        StringBuilder sb = new StringBuilder();

        if(period.getYears() > 0) {
            sb.append(period.getYears()).append("y ");
        }
        if(period.getMonths() > 0) {
            sb.append(period.getMonths()).append("M ");
        }
        if(period.getWeeks() > 0) {
            sb.append(period.getWeeks()).append("w ");
        }
        if(period.getDays() > 0) {
            sb.append(period.getDays()).append("d ");
        }
        sb.append(period.getMinutes()).append("m ");
        sb.append(period.getSeconds()).append("s ");

        return sb.toString();
    }
}
