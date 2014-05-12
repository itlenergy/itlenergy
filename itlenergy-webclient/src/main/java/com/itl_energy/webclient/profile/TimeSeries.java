package com.itl_energy.webclient.profile;

/**
 * Interface to represent a sequence of measurements - data sources will
 * specialise this according to the type of data they generate.
 *
 * @version 5th January 2011
 * @author bstephen
 */
public interface TimeSeries {

    /**
     * Gets the time the series begins.
     *
     * @return the first timestamp in the load series
     * @uml.property name="beginTime"
     */

    public String getBeginTime();

    /**
     * Gets the time the series ends.
     *
     * @return the latest timestamp in the load series
     * @uml.property name="endTime"
     */
    public String getEndTime();

    /**
     * Gets the length of the series.
     *
     * @return the number of samples in the series
     */
    public int getLength();

    /**
     * Gets the number of seconds the series lasts for.
     *
     * @return the time duration in seconds of the series
     */
    public long getSeriesDurationInSeconds();

    /**
     * Gets the timestamp at a given point in the series
     *
     * @param idx the sample index to return
     * @return the timestamp at the requested sample index
     */
    public String getTimeAtSeriesIndex(int idx);
}
