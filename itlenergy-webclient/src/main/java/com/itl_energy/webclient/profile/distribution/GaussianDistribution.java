package com.itl_energy.webclient.profile.distribution;

/**
 * Class representing a univariate Gaussian distribution with mean and variance
 * parameters.
 *
 * @author bstephen
 * @version 13th January 2011
 */
public class GaussianDistribution extends UnivariateDistribution {

    /**
     * @uml.property name="mu"
     */
    protected double mu;
    /**
     * @uml.property name="sigma"
     */
    protected double sigma;
    /**
     * @uml.property name="tau"
     */
    protected double tau;

    /**
     * Creates a Gaussian distribution with zero mean and variance of 1.
     */
    public GaussianDistribution() {
        super();

        this.mu = 0.0;
        this.sigma = 1.0;
        this.tau = 1.0 / this.sigma;
    }

    /**
     * Creates a Gaussian distribution with the specified mean and variance.
     *
     * @param mu the mean this distribution is to take
     * @param sig the variance for this distribution
     */
    public GaussianDistribution(double mu, double sig) {
        super();

        this.mu = mu;
        this.sigma = sig;
        this.tau = 1.0 / this.sigma;
    }

    /**
     * @return @uml.property name="mu"
     */
    public double getMu() {
        return this.mu;
    }

    /**
     * @return @uml.property name="sigma"
     */
    public double getSigma() {
        return this.sigma;
    }

    /**
     * @return @uml.property name="tau"
     */
    public double getTau() {
        return this.tau;
    }

    /**
     * @param mu
     * @uml.property name="mu"
     */
    public void setMu(double mu) {
        this.mu = mu;
    }

    /**
     * @param sigma
     * @uml.property name="sigma"
     */
    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    /**
     * @param tau
     * @uml.property name="tau"
     */
    public void setTau(double tau) {
        this.tau = tau;
    }

    public double likelihood(double obs) {
        double gauss = 0.0;
        double y = Math.pow(obs - this.mu, 2.0) / this.sigma;

        gauss = Math.exp(-0.5 * y) / (Math.sqrt(this.sigma) * Math.sqrt(2.0 * Math.PI));

        return gauss;
    }

    public double sample() {
        return this.mu + (Math.random() - 0.5) * this.sigma;//very crude approximation...
    }

    public String toString() {
        String s = "Gaussian(";

        s += this.mu;
        s += ",";
        s += this.sigma;
        s += ")";

        return s;
    }
}
