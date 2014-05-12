package com.itl_energy.webclient.profile.distribution;

/**
 * Class representing a Beta distribution.
 *
 * @author bstephen
 * @version 3rd October 2011
 */
public class BetaDistribution extends UnivariateDistribution {

    protected double alpha;
    protected double beta;

    public BetaDistribution() {
        //parameterisation for typical small fraction
        this.alpha = 0.5;
        this.beta = 0.5;
    }

    public BetaDistribution(double alpha, double beta) {
        //parameterisation for typical small fraction
        this.alpha = alpha;
        this.beta = beta;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getAlpha() {
        return this.alpha;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getBeta() {
        return this.beta;
    }

    public static double beta(double x, double y) {
        return Math.exp(GammaDistribution.logGamma(x) + GammaDistribution.logGamma(y) - GammaDistribution.logGamma(x + y));
    }

    public void methodOfMomentParameterEstimate(double[] d) {
        this.sampleMean(d);
        this.sampleVariance(d);

        this.alpha = this.sampleMu * ((this.sampleMu * (1.0 - this.sampleMu) / this.sampleSigma) - 1.0);
        this.beta = (1.0 - this.sampleMu) * ((this.sampleMu * (1.0 - this.sampleMu) / this.sampleSigma) - 1.0);
    }

    public double Likelihood(double obs) {
        double beta = 0.0;

        beta = (1.0 / BetaDistribution.beta(this.alpha, this.beta)) * (Math.pow(obs, this.alpha - 1.0) * Math.pow(1.0 - obs, this.beta - 1.0));

        return beta;
    }

    public String toString() {
        return "B(" + this.getAlpha() + "," + this.getBeta() + ")";
    }
}
