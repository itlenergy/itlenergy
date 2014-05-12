package com.itl_energy.webclient.profile.strat;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.itl_energy.webclient.profile.LoadTimeSeries;
import com.itl_energy.webclient.profile.TimeSeries;

/**
 * Interface to represent a sequence of measurements that have been converted
 * into stratum labels.
 *
 * @version 5th January 2011
 * @author bstephen
 */
public class StratumTimeSeries implements TimeSeries {

    /**
     * @uml.property name="times" multiplicity="(0 -1)" dimension="1"
     */
    protected String[] times;
    /**
     * @uml.property name="strata" multiplicity="(0 -1)" dimension="1"
     */
    protected int[] strata;
    /**
     * @uml.property name="allstrata" multiplicity="(0 -1)" dimension="1"
     */
    protected String[] allstrata;
    /**
     * @uml.property name="length"
     */
    private int length;

    /**
     * Creates an empty stratum time series.
     */
    public StratumTimeSeries() {
        this.times = new String[48];
        this.strata = new int[48];
        this.length = 0;
    }

    /**
     * Creates a stratum time series from an existing series of load
     * measurements and a stratification model.
     *
     * @param ms the stratification model to discretise the series
     * @param lts the time series to be stratified
     */
    public StratumTimeSeries(MeasurementStratifier ms, LoadTimeSeries lts) {
        this.times = new String[lts.getLength()];
        this.strata = new int[lts.getLength()];
        this.allstrata = ms.getStratum();
        this.length = 0;

        for (int i = 0; i < lts.getLength(); i++) {
            this.addObservation(lts.getTimeAtSeriesIndex(i), ms.stratify(new Double(lts.getLoadAtSeriesIndex(i))));
        }
    }

    /**
     * Adds an observation of a stratum label to this time series.
     *
     * @param time the time of the observation
     * @param strat the stratum label
     */
    public void addObservation(String time, int strat) {
        if (this.strata.length <= this.length) {
            String[] temptimes = new String[this.length + 480];
            int[] tempstrat = new int[this.length + 480];

            for (int i = 0; i < this.length; i++) {
                temptimes[i] = this.times[i];
                tempstrat[i] = this.strata[i];
            }

            this.times = temptimes;
            this.strata = tempstrat;
        }

        this.times[this.length] = time;
        this.strata[this.length] = strat;

        this.length++;
    }

    /**
     * Gets a list of strata labels that are in this series.
     *
     * @return strata labels as an array of integers
     */
    public String[] getStrataInSeries() {
        return allstrata;
    }

    /**
     * Gets the load strata at the beginning of the series.
     *
     * @return the label of the first strata in the load series
     */
    public int getBeginStrata() {
        return this.strata[0];
    }

    /**
     * Gets the load strata at the end of the series.
     *
     * @return the label of the latest strata in the load series
     */
    public int getEndStrata() {
        return this.strata[this.length - 1];
    }

    /**
     * Gets the value of the load (in kWh) at a given point
     *
     * @param idx the sample index to return
     * @return the value of the load (kWh) at the requested sample index
     */
    public int getStrataAtSeriesIndex(int idx) {
        return this.strata[idx];
    }

    @Override
    public String getBeginTime() {
        return this.times[0];
    }

    @Override
    public String getEndTime() {
        return this.times[this.length - 1];
    }

    /**
     * @return @uml.property name="length"
     */
    @Override
    public int getLength() {
        return this.length;
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public String getTimeAtSeriesIndex(int idx) {
        return this.times[idx];
    }
}
