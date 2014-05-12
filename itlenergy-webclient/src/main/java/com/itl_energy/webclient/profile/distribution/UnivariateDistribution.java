package com.itl_energy.webclient.profile.distribution;

/**
 * Abstract class representing a probability distribution over one variable.
 *
 * @version 13th January 2011
 * @author bstephen
 */
public class UnivariateDistribution extends Distribution {

    protected double sampleMu;
    protected double sampleSigma;

    protected UnivariateDistribution() {
        this.sampleMu = 0.0;
        this.sampleSigma = 0.0;
    }

    public void sampleMean(double[] obs) {
        this.sampleMu = 0.0;

        for (int i = 0; i < obs.length; i++) {
            this.sampleMu += obs[i];
        }

        this.sampleMu /= (double) obs.length;
    }

    public double getSampleMean() {
        return this.sampleMu;
    }

    public void sampleVariance(double[] obs) {
        this.sampleSigma = 0.0;

        for (int i = 0; i < obs.length; i++) {
            this.sampleSigma += Math.pow(obs[i] - this.sampleMu, 2.0);
        }

        this.sampleSigma /= (double) obs.length;
    }

    public double getSampleVariance() {
        return this.sampleSigma;
    }
}
