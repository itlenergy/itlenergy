package org.itl.display.graphics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateManipulator {

    protected String informat;
    protected String outformat;
    protected SimpleDateFormat indateformat;
    protected SimpleDateFormat outdateformat;

    public DateManipulator() {
        this.informat = "yyyy-MM-dd HH:mm:ss";
        this.outformat = "yyyy-MM-dd HH:mm:ss";
        this.indateformat = new SimpleDateFormat(this.informat);
        this.outdateformat = new SimpleDateFormat(this.outformat);

        this.indateformat.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.outdateformat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public void setInputDateFormat(String form) {
        this.informat = form;
        this.indateformat.applyPattern(this.informat);
    }

    public void setOutputDateFormat(String form) {
        this.outformat = form;
        this.outdateformat.applyPattern(this.outformat);
    }

    public float getHourOfDay(String timestamp) {
        Date beg;
        Calendar cal1 = Calendar.getInstance();

        try {
            beg = this.indateformat.parse(timestamp);
            cal1.setTime(beg);

            return (float) cal1.get(Calendar.HOUR_OF_DAY) + (float) cal1.get(Calendar.MINUTE) / 60.0F;
        }
        catch (java.text.ParseException e) {
            e.printStackTrace(System.err);
        }

        return -1;
    }

    public float getMinuteOfDay(String timestamp) {
        Date beg;
        Calendar cal1 = Calendar.getInstance();

        try {
            beg = this.indateformat.parse(timestamp);
            cal1.setTime(beg);

            return (float) cal1.get(Calendar.HOUR_OF_DAY) * 60.0F + (float) cal1.get(Calendar.MINUTE);
        }
        catch (java.text.ParseException e) {
            e.printStackTrace(System.err);
        }

        return -1;
    }

    public float getSecondOfDay(String timestamp) {
        Date beg;
        Calendar cal1 = Calendar.getInstance();

        try {
            beg = this.indateformat.parse(timestamp);
            cal1.setTime(beg);

            return (float) cal1.get(Calendar.HOUR_OF_DAY) * 3600.0F + (float) cal1.get(Calendar.MINUTE) * 60.0F + (float) cal1.get(Calendar.SECOND);
        }
        catch (java.text.ParseException e) {
            e.printStackTrace(System.err);
        }

        return -1;
    }

    public float getDayOfYear(String timestamp) {
        Date beg;
        Calendar cal1 = Calendar.getInstance();

        try {
            beg = this.indateformat.parse(timestamp);
            cal1.setTime(beg);

            return (float) cal1.get(Calendar.DAY_OF_YEAR);
        }
        catch (java.text.ParseException e) {
            e.printStackTrace(System.err);
        }

        return -1;
    }

    public long timeToUTC(String timestamp) {
        Date beg;
        Calendar cal1 = Calendar.getInstance();

        try {
            beg = this.indateformat.parse(timestamp);
            cal1.setTime(beg);

            return cal1.getTimeInMillis();
        }
        catch (java.text.ParseException e) {
            e.printStackTrace(System.err);
        }

        return -1;
    }

    public String utcToTime(long utc) {
        Date beg;
        Calendar cal1 = Calendar.getInstance();

        cal1.setTimeInMillis(utc);
        beg = cal1.getTime();

        return this.outdateformat.format(beg);
    }

    public static boolean containsDate(String date, String begin, String end) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beg;
        Date nd;
        Date dte;
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        try {
            dte = sdf.parse(date);
            beg = sdf.parse(begin);
            nd = sdf.parse(end);

            cal.setTime(dte);
            cal1.setTime(beg);
            cal2.setTime(nd);

            if (cal.compareTo(cal1) >= 0 && cal.compareTo(cal2) <= 0) {
                return true;
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static int getDuration(String begin, String end) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beg;
        Date nd;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        try {
            beg = sdf.parse(begin);
            nd = sdf.parse(end);

            cal1.setTime(beg);
            cal2.setTime(nd);

            long diff = Math.abs(cal2.getTimeInMillis() - cal1.getTimeInMillis());

            return (int) (diff / 86400000L);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
