package com.itl_energy.webclient.profile.distribution;

/**
 * Class representing a Student-t distribution.
 *
 * @version 13th January 2011
 * @author bstephen
 */
public class StudentTDistribution extends UnivariateDistribution {

    /**
     * @uml.property name="mu"
     */
    protected double mu;
    /**
     * @uml.property name="tau"
     */
    protected double tau;
    /**
     * @uml.property name="nu"
     */
    protected double nu;

    /**
     * Creates a Student-t distribution with zero mean, 1 degree of freedom and
     * a precision (inverse variance) of root 2.
     */
    public StudentTDistribution() {
        super();

        this.mu = 0.0;
        this.nu = 1.0;
        this.tau = Math.sqrt(2.0);
    }

    /**
     * Creates a Student-t distribution with the parameters specified.
     *
     * @param m the mean value this distribution is to take
     * @param t the precision of the distribution
     * @param a the degrees of freedom
     */
    public StudentTDistribution(double m, double t, double a) {
        super();

        this.mu = 0.0;
        this.nu = 1.0;
        this.tau = Math.sqrt(2.0);
    }

    /**
     * @return @uml.property name="mu"
     */
    public double getMu() {
        return this.mu;
    }

    /**
     * @return @uml.property name="tau"
     */
    public double getTau() {
        return this.tau;
    }

    /**
     * @return @uml.property name="nu"
     */
    public double getNu() {
        return this.nu;
    }

    /**
     * @param mu
     * @uml.property name="mu"
     */
    public void setMu(double mu) {
        this.mu = mu;
    }

    /**
     * @param tau
     * @uml.property name="tau"
     */
    public void setTau(double tau) {
        this.tau = tau;
    }

    /**
     * @param nu
     * @uml.property name="nu"
     */
    public void setNu(double nu) {
        this.nu = nu;
    }

    public double likelihood(double obs) {
        //This form is taken from Kevin Murphy's lecture notes. 
        double c = Math.exp(GammaDistribution.logGamma(this.nu * 0.5 + 0.5) - GammaDistribution.logGamma(this.nu * 0.5)) * Math.pow(this.nu * Math.PI * this.tau, -0.5);

        return c * Math.pow((1.0 + (1.0 / (this.nu * this.tau)) * Math.pow(obs - this.mu, 2.0)), (-(this.nu + 1.0) / 2.0));
    }

    /**
     * Generates a random sample from this distribution.
     *
     * @return a random sample drawn from a Student-t distribution with
     * parameters as specified by this class.
     */
    public double sample() {
        return this.mu + (Math.random() - 0.5) / this.tau;//TODO:very crude approximation...
    }

    /**
     * Displays the parameters of this distribution as a String
     *
     * @return a String instance representing the distributions parameterisation.
     */
    public String toString() {
        return "St(" + Double.toString(this.mu) + "," + Double.toString(this.tau) + "," + Double.toString(this.nu) + ")";
    }
}
