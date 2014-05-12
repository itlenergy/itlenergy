package com.itl_energy.webclient.profile.strat;

/**
 * Interface representing the abstract model which converts a measurement into a
 * stratum label.
 *
 * @version 5th January 2011
 * @author bstephen
 */
public interface MeasurementStratifier {

    /**
     * Converts or discretizes a measurement into a stratum label.
     *
     * @param meas the measurement to be stratified
     * @return the strata label assigned to the measurement
     */
    public int stratify(Object meas);

    /**
     * Gets a list of strata labels that this model produces.
     *
     * @return strata labels as an array of integers
     */
    public String[] getStratum();
}
