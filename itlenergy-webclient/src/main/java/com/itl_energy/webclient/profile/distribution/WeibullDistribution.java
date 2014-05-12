package com.itl_energy.webclient.profile.distribution;

/**
 * Class representing a 2-parameter Weibull distribution with functionality for
 * gradient descent based MLE parameter estimation.
 *
 * @author bstephen
 * @version 13th October 2011
 */
public class WeibullDistribution extends UnivariateDistribution {

    protected double lambda;//shape parameter
    protected double k;//scale parameter

    public WeibullDistribution() {
        this.lambda = 1.0;//looks like a non-zero Gauss...
        this.k = 5.0;
    }

    public WeibullDistribution(double shape, double scale) {
        this.lambda = scale;
        this.k = shape;
    }

    public double getShape() {
        return this.k;
    }

    public double getScale() {
        return this.lambda;
    }

    public void setShape(double shape) {
        this.k = shape;
    }

    public void setScale(double scale) {
        this.lambda = scale;
    }

    public void methodOfMomentEstimate(double[] dat) {
        this.sampleMean(dat);
        this.sampleVariance(dat);

        double m = this.sampleMu;
        double s = this.sampleSigma;

        this.k = Math.pow(s / m, -1.019);
        this.lambda = m / GammaDistribution.gamma(1.0 + 1.0 / this.k);
    }

    public void newtonianMLE(double[] dat) {
        for (int i = 0; i < dat.length; i++) {

        }
    }

    public double cumulative(double x) {
        return 1.0 - Math.exp(-1.0 * Math.pow(x / this.lambda, this.k));
    }

    public double likelihood(double x) {
        return (this.k / this.lambda) * Math.pow(x / this.lambda, this.k - 1.0) * Math.exp(-1.0 * Math.pow(x / this.lambda, this.k));
    }

    public String toString() {
        return "W(" + this.getShape() + "," + this.getScale() + ")";
    }
}
