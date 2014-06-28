package com.itl_energy.webclient.itl.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility functions for accessing the ITL metering web service.
 *
 * @author bstephen
 * @date 10th July 2013
 */
public class ITLClientUtilities {
    public static long dateStringToSeconds(String timestamp) throws ParseException {
        return dateStringToMilliseconds(timestamp) / 1000;
    }

    public static long dateStringToMilliseconds(String timestamp) throws ParseException {
        return ITLClientUtilities.dateStringToMilliseconds(timestamp, "yyyy-MM-dd HH:mm");
    }

    public static long dateStringToMilliseconds(String timestamp, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date beg = sdf.parse(timestamp);
        
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(beg);
        return cal1.getTimeInMillis();
    }

    public static long nowToMilliseconds() {
        Calendar cal1 = Calendar.getInstance();

        return cal1.getTimeInMillis();
    }

    public static String millisecondsToDateString(long utc) {
        return ITLClientUtilities.millisecondsToDateString(utc, "yyyy-MM-dd HH:mm:SS");
    }

    public static String millisecondsToDateString(long utc, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date beg;
        Calendar cal1 = Calendar.getInstance();

        cal1.setTimeInMillis(utc);
        beg = cal1.getTime();

        return sdf.format(beg);
    }
}
