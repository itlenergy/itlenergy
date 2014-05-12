package com.itl_energy.webclient.profile.distribution;

/**
 * Class representing the Dirichlet distribution - a distribution over measures
 * or compositional data i.e. all observations sum to a constant.
 *
 * @author bstephen
 * @version 13th January 2011
 */
public class DirichletDistribution extends MultivariateDistribution {

    protected double[] alpha;
    protected double[] m;
    protected double s;

    public DirichletDistribution() {
        //make this a beta distribution by default
        this.setDimension(2);
    }

    public DirichletDistribution(int dim) {
        this.setDimension(dim);
    }

    public void setDimension(int dim) {
        this.alpha = new double[dim];
        this.m = new double[dim];
    }

    public int getDimension() {
        return this.alpha.length;
    }

    public double[] getAlpha() {
        return this.alpha;
    }

    public double[] getMean() {
        return this.m;
    }

    public double getPrecision() {
        return this.s;
    }

    public double likelihood(double[] x) {
        double likelihood = 0.0;
        double sum = 0.0;
        double prod = 1.0;
        double prodAlpha = 1.0;

        for (int i = 0; i < this.alpha.length; i++) {
            sum += this.alpha[i];
            prodAlpha *= GammaDistribution.gamma(this.alpha[i]);
            prod *= Math.pow((double) x[i], (double) (this.alpha[i] - 1.0));
        }

        likelihood = prod * GammaDistribution.gamma(sum) / prodAlpha;

        return likelihood;
    }

    protected void update() {
		//calculate the mean and variance...

        this.s = 0.0;

        for (int i = 0; i < this.alpha.length; i++) {
            this.s += this.alpha[i];
        }

        for (int i = 0; i < this.alpha.length; i++) {
            this.m[i] = this.alpha[i] / this.s;
        }
    }

    public String toString() {
        String s = "Di(" + this.alpha[0];

        for (int i = 1; i < this.alpha.length; i++) {
            s += "," + this.alpha[i];
        }

        s += ")";

        return s;
    }
}
