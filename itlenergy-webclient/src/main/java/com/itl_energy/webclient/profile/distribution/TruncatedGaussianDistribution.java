package com.itl_energy.webclient.profile.distribution;

/**
 * Class representing a truncated univariate Gaussian distribution with mean and
 * variance parameters.
 *
 * @author bstephen
 * @version 12th July 2011
 */
public class TruncatedGaussianDistribution extends UnivariateDistribution {

    protected double mu;
    protected double sigma;
    protected double tau;
    protected double lower;
    protected double upper;

    /**
     * Creates a Truncated Gaussian distribution with zero mean and variance of
     * 1.
     */
    public TruncatedGaussianDistribution() {
        super();

        this.lower = Double.NEGATIVE_INFINITY;
        this.upper = Double.POSITIVE_INFINITY;

        this.mu = 0.0;
        this.sigma = 1.0;
        this.tau = 1.0 / this.sigma;
    }

    /**
     * Creates a Truncated Gaussian distribution with the specified mean and
     * variance.
     *
     * @param mu the mean this distribution is to take
     * @param sig the variance for this distribution
     */
    public TruncatedGaussianDistribution(double mu, double sig) {
        super();

        this.mu = mu;
        this.sigma = sig;
        this.tau = 1.0 / this.sigma;

        this.lower = Double.NEGATIVE_INFINITY;
        this.upper = Double.POSITIVE_INFINITY;
    }

    /**
     * Creates a Truncated Gaussian distribution with the specified mean and
     * variance, truncated at the points given.
     *
     * @param mu the mean this distribution is to take
     * @param sig the variance for this distribution
     * @param lo the lower truncation for this distribution
     * @param hi the upper truncation for this distribution
     */
    public TruncatedGaussianDistribution(double mu, double sig, double lo, double hi) {
        super();

        this.mu = mu;
        this.sigma = sig;
        this.tau = 1.0 / this.sigma;

        this.lower = lo;
        this.upper = hi;
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

    public void setLowerTruncation(double lo) {
        this.lower = lo;
    }

    public double likelihood(double obs) {
        double gauss = 0.0;
        double y = Math.pow(obs - this.mu, 2.0) / this.sigma;

        gauss = Math.exp(-0.5 * y) / (Math.sqrt(this.sigma) * Math.sqrt(2.0 * Math.PI));

        return gauss / (1.0 - this.cumulative(this.lower));
    }

    protected double cumulative(double z) {
        double x = (z - this.mu) / Math.sqrt(this.sigma);
        double a1 = 0.398942280444E+00;
        double a2 = 0.399903438504E+00;
        double a3 = 5.75885480458E+00;
        double a4 = 29.8213557808E+00;
        double a5 = 2.62433121679E+00;
        double a6 = 48.6959930692E+00;
        double a7 = 5.92885724438E+00;
        double b0 = 0.398942280385E+00;
        double b1 = 3.8052E-08;
        double b2 = 1.00000615302E+00;
        double b3 = 3.98064794E-04;
        double b4 = 1.98615381364E+00;
        double b5 = 0.151679116635E+00;
        double b6 = 5.29330324926E+00;
        double b7 = 4.8385912808E+00;
        double b8 = 15.1508972451E+00;
        double b9 = 0.742380924027E+00;
        double b10 = 30.789933034E+00;
        double b11 = 3.99019417011E+00;
        double cdf;
        double q;
        double y;
		//
        // |X| <= 1.28.
        //
        if (Math.abs(x) <= 1.28) {
            y = 0.5 * x * x;

            q = 0.5 - Math.abs(x)
                    * (a1 - a2 * y / (y + a3 - a4 / (y + a5 + a6 / (y + a7))));
			//
            // 1.28 < |X| <= 12.7
            //
        }
        else if (Math.abs(x) <= 12.7) {
            y = 0.5 * x * x;

            q = Math.exp(-y)
                    * b0
                    / (Math.abs(x) - b1 + b2
                    / (Math.abs(x) + b3 + b4
                    / (Math.abs(x) - b5 + b6
                    / (Math.abs(x) + b7 - b8
                    / (Math.abs(x) + b9 + b10
                    / (Math.abs(x) + b11))))));
			//
            // 12.7 < |X|
            //
        }
        else {
            q = 0.0;
        }

		//
        // Take account of negative X.
        //
        if (x < 0.0) {
            cdf = q;
        }
        else {
            cdf = 1.0 - q;
        }

        return cdf;
    }

    public String toString() {
        String s = "TruncatedGaussian(";

        s += this.mu;
        s += ",";
        s += this.sigma;
        s += ")";

        return s;
    }
}
