package com.itl_energy.webclient.profile.distribution;

/**
 * Interface for representing a distribution over binary or categorical data.
 *
 * @version 13th January 2011
 * @author bstephen
 */
public interface DiscreteDistribution {

    /**
     * Returns the number of categories in this distribution.
     *
     * @return the number of categories this distribution is over.
     */
    public int getCategories();

    /**
     * Sets the number of categories in this distribution.
     *
     * @param cat the number of categories this distribution has.
     */
    public void setCategories(int cat);
}
