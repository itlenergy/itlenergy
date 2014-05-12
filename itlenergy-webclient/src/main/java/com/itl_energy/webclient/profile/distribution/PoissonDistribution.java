package com.itl_energy.webclient.profile.distribution;

/**
 * Class representing a Poisson distribution with a single intensity parameter.
 *
 * @author bstephen
 * @version 13th January 2011
 */
public class PoissonDistribution extends UnivariateDistribution {

    /**
     * @uml.property name="lambda"
     */
    protected double lambda;

    /**
     * Create a Poisson distribution with an intensity of 1.
     */
    public PoissonDistribution() {
        super();

        this.lambda = 1;
    }

    /**
     * Create a Poisson distribution with an intensity of the value specified.
     *
     * @param lambda the value of the intensity parameter for this Poisson
     * distribution
     */
    public PoissonDistribution(double lambda) {
        super();

        this.lambda = lambda;
    }

    /**
     * @return @uml.property name="lambda"
     */
    public double getLambda() {
        return this.lambda;
    }

    /**
     * @param lambda
     * @uml.property name="lambda"
     */
    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double Likelihood(double obs) {
        return Math.exp(-1.0 * this.lambda + obs * Math.log(this.lambda) - GammaDistribution.logGamma(obs + 1));
    }

    public double Sample() {
        //from Knuth...
        double L = Math.exp(-1.0 * this.lambda);
        int k = 0;
        double p = 1.0;

        do {
            k++;
            p = p * Math.random();
        } while (p > L);

        return k - 1;
    }

    public String toString() {
        return "Po(" + Double.toString(this.lambda) + ")";
    }
}
