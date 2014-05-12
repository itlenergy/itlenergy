package com.itl_energy.webclient.profile;

/**
 * Interface to represent a sequence of load measurements regardless of their
 * source.
 *
 * @version 13th December 2010
 * @author bstephen
 */
public interface LoadTimeSeries extends TimeSeries {

    /**
     * Gets the load at the beginning of the series.
     *
     * @return the value of the first value in the load series in kWh
     */

    public double getBeginLoad();

    /**
     * Gets the load at the end of the series.
     *
     * @return the value of the latest value in the load series in kWh
     */
    public double getEndLoad();

    /**
     * Gets the value of the load (in kWh) at a given point
     *
     * @param idx the sample index to return
     * @return the value of the load (kWh) at the requested sample index
     */
    public double getLoadAtSeriesIndex(int idx);
}
