package com.itl_energy.webclient.profile.distribution;

/**
 * Class representing a log normal distribution.
 *
 * @author bstephen
 * @version 20th October 2011
 */
public class LogNormalDistribution extends GaussianDistribution {

    public LogNormalDistribution() {
        super();
    }

    public LogNormalDistribution(double mu, double sigma) {
        super(mu, sigma);
    }

    public double likelihood(double obs) {
        return super.likelihood(Math.log(obs));
    }

    public void maximumLikelihoodEstimate(double[] d) {
        this.mu = 0.0;

        for (int i = 0; i < d.length; i++) {
            this.mu += Math.log(d[i]);
        }

        this.mu /= (double) d.length;

        this.sigma = 0.0;

        for (int i = 0; i < d.length; i++) {
            this.sigma += Math.pow(Math.log(d[i]) - this.mu, 2.0);
        }

        this.sigma /= (double) d.length;
    }

    public String toString() {
        String s = "LogNormal(";

        s += this.mu;
        s += ",";
        s += this.sigma;
        s += ")";

        return s;
    }
}
