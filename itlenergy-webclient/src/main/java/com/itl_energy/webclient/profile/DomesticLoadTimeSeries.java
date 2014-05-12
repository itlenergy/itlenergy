package com.itl_energy.webclient.profile;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Class to represent a sequence of load measurements from a given property.
 *
 * @version 13th December 2010
 * @author bstephen
 */
public class DomesticLoadTimeSeries implements LoadTimeSeries {

    /**
     * @uml.property name="propertyid"
     */
    protected long propertyid;
    /**
     * @uml.property name="label"
     */
    protected String label;
    /**
     * @uml.property name="times" multiplicity="(0 -1)" dimension="1"
     */
    protected String[] times;
    /**
     * @uml.property name="loads" multiplicity="(0 -1)" dimension="1"
     */
    protected double[] loads;
    /**
     * @uml.property name="length"
     */
    private int length;

    /**
     * Creates an empty load time series associated with the specified property.
     *
     * @param pid the property identifier.
     */
    public DomesticLoadTimeSeries(long pid) {
        this.propertyid = pid;
        this.label = "";
        this.times = new String[48];
        this.loads = new double[48];
        this.length = 0;
    }

    /**
     * Gets the identifier of the property this load time series was recorded
     * at.
     *
     * @return the identifier of the property where the meter that recorded this
     * time series was installed.
     */
    public long getPropertyID() {
        return this.propertyid;
    }

    /**
     * Adds a load observation on to the end of this time series.
     *
     * @param time the time of this observation
     * @param load the value of the load in kW
     */
    public void addObservation(String time, double load) {
        if (this.loads.length <= this.length) {
            String[] temptimes = new String[this.length + 480];
            double[] temploads = new double[this.length + 480];

            for (int i = 0; i < this.length; i++) {
                temptimes[i] = this.times[i];
                temploads[i] = this.loads[i];
            }

            this.times = temptimes;
            this.loads = temploads;
        }

        this.times[this.length] = time;
        this.loads[this.length] = load;

        this.length++;
    }

    /**
     * @return @uml.property name="length"
     */
    public int getLength() {
        return this.length;
    }

    public double getLoadAtSeriesIndex(int idx) {
        return this.loads[idx];
    }

    public String getTimeAtSeriesIndex(int idx) {
        return this.times[idx];
    }

    @Override
    public double getBeginLoad() {
        return this.loads[0];
    }

    @Override
    public double getEndLoad() {
        return this.loads[this.length - 1];
    }

    @Override
    public String getBeginTime() {
        return this.times[0];
    }

    @Override
    public String getEndTime() {
        return this.times[this.length - 1];
    }

    @Override
    public long getSeriesDurationInSeconds() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beg;
        Date nd;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        try {
            beg = sdf.parse(this.getBeginTime());
            nd = sdf.parse(this.getEndTime());

            cal1.setTime(beg);
            cal2.setTime(nd);

            long diff = Math.abs(cal2.getTimeInMillis() - cal1.getTimeInMillis());

            return diff / 1000;
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Return a subsequence of load measurements from this load measurement
     * series.
     *
     * @param start the time the subsequence is to begin.
     * @param finish the time the subsequence is to end.
     * @return A DomesticLoadTimeSeries instance which is a subsequence of this
     * instance captured between the bounding dates specified. Returns null if
     * either of the dates are out with the range of this time series.
     */
    public DomesticLoadTimeSeries getSubSeries(String start, String finish) {
        DomesticLoadTimeSeries subseq = null;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beg;
        Date nd;
        Date sta;
        Date fin;
        Date now;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        Calendar cal4 = Calendar.getInstance();
        Calendar cal5 = Calendar.getInstance();

        try {
            int pt1 = 0;
            int pt2 = 0;

            beg = sdf.parse(this.getBeginTime());
            nd = sdf.parse(this.getEndTime());
            sta = sdf.parse(start);
            fin = sdf.parse(finish);

            cal1.setTime(beg);
            cal2.setTime(nd);
            cal3.setTime(sta);
            cal4.setTime(fin);

            if ((cal3.getTimeInMillis() - cal1.getTimeInMillis()) < 0) {
                return null;
            }
            if ((cal4.getTimeInMillis() - cal2.getTimeInMillis()) > 0) {
                return null;
            }

            subseq = new DomesticLoadTimeSeries(this.getPropertyID());

            for (pt1 = 0; pt1 < this.getLength(); pt1++) {
                now = sdf.parse(this.getTimeAtSeriesIndex(pt1));
                cal5.setTime(now);

                if ((cal5.getTimeInMillis() - cal3.getTimeInMillis()) > 0) {
                    break;
                }
            }
            for (pt2 = pt1; pt2 < this.getLength(); pt2++) {
                now = sdf.parse(this.getTimeAtSeriesIndex(pt2));
                cal5.setTime(now);

                if ((cal5.getTimeInMillis() - cal4.getTimeInMillis()) > 0) {
                    break;
                }
            }

            for (int i = pt1; i < pt2; i++) {
                subseq.addObservation(this.getTimeAtSeriesIndex(i), this.getLoadAtSeriesIndex(i));
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return subseq;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public String toString() {
        String str = "Load from: ";

        str += this.getBeginTime();
        str += " to ";
        str += this.getEndTime();
        str += ".";

        return str;
    }
}
