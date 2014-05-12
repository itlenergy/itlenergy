package com.itl_energy.webclient.profile.distribution;

/**
 * Class representing a univariate Gamma distribution with two parameters.
 *
 * @author bstephen
 * @version 13th January 2011
 */
public class GammaDistribution extends UnivariateDistribution {

    /**
     * @uml.property name="alpha"
     */
    protected double alpha;
    /**
     * @uml.property name="beta"
     */
    protected double beta;

    /**
     * Creates a new Gamma distribution with alpha parameter set to 1 and the
     * beta parameter set to 0.5.
     */
    public GammaDistribution() {
        super();

        this.alpha = 1.0;
        this.beta = 0.5;
    }

    /**
     * Creates a new Gamma distribution with the parameter values specified.
     *
     * @param alpha the value for the alpha parameter
     * @param beta the value for the beta parameter
     */
    public GammaDistribution(double alpha, double beta) {
        super();

        this.alpha = alpha;
        this.beta = beta;
    }

    /**
     * @return @uml.property name="alpha"
     */
    public double getAlpha() {
        return this.alpha;
    }

    /**
     * @return @uml.property name="beta"
     */
    public double getBeta() {
        return this.beta;
    }

    /**
     * @param alpha
     * @uml.property name="alpha"
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * @param beta
     * @uml.property name="beta"
     */
    public void setBeta(double beta) {
        this.beta = beta;
    }

    public static double gamma(double x) {
        return Math.exp(GammaDistribution.logGamma(x));
    }

    public static double logGamma(double x) {
        double z;

        z = 1. / (x * x);
        x = x + 6;

        z = (((-0.000595238095238 * z + 0.000793650793651) * z - 0.002777777777778) * z + 0.083333333333333) / x;
        z = (x - 0.5) * Math.log(x) - x + 0.918938533204673 + z - Math.log(x - 1) - Math.log(x - 2) - Math.log(x - 3) - Math.log(x - 4) - Math.log(x - 5) - Math.log(x - 6);

        return z;
    }

    public static double digamma(double xi) {
        double p;

        double x = xi + 6;
        p = 1 / (x * x);
        p = (((0.004166666666667 * p - 0.003968253986254) * p + 0.008333333333333) * p - 0.083333333333333) * p;
        p = p + Math.log(x) - 0.5 / x - 1 / (x - 1) - 1 / (x - 2) - 1 / (x - 3) - 1 / (x - 4) - 1 / (x - 5) - 1 / (x - 6);

        return p;
    }

    public static double trigamma(double x) {
        double p;
        int i;

        x = x + 6;
        p = 1 / (x * x);
        p = (((((0.075757575757576 * p - 0.033333333333333) * p + 0.0238095238095238) * p - 0.033333333333333) * p + 0.166666666666667) * p + 1) / x + 0.5 * p;

        for (i = 0; i < 6; i++) {
            x = x - 1;
            p = 1 / (x * x) + p;
        }

        return p;
    }

    public void gradientDescentEstimate(double[] d) {
        this.sampleMean(d);

        double m = this.sampleMu;
        double mlog = 0.0;

        for (int i = 0; i < d.length; i++) {
            mlog += Math.log(d[i]);
        }

        mlog /= (double) d.length;

        this.sampleSigma = Math.log(m) - mlog;
        this.alpha = 0.5 / this.sampleSigma;

        /*
         for(int i=0;i<1000;i++)
         {
         double old_a=this.alpha;
         double y=Math.log(this.alpha)-this.sampleSigma;
			
         this.alpha=Math.exp(y)+1.0/2.0;
			
         //what this do?
         //i = find(y <= -2.22);
         //x(i) = -1./(y(i) - GammaDistribution.digamma(1));
			
         for(int j=0;j<5;j++)
         this.alpha=this.alpha-(GammaDistribution.digamma(this.alpha)-y)/GammaDistribution.trigamma(this.alpha);
			
         if(Math.abs(this.alpha-old_a)< 1e-8)
         break;
         }
         */
        // gen Newton
        for (int i = 0; i < 100; i++) {
            double old_a = this.alpha;
            double g = Math.log(this.alpha) - this.sampleSigma - GammaDistribution.digamma(this.alpha);
            double h = 1.0 / this.alpha - GammaDistribution.trigamma(this.alpha);

            this.alpha = 1.0 / (1.0 / this.alpha + g / (Math.pow(this.alpha, 2.0) * h));

            if (Math.abs(this.alpha - old_a) < 1e-8) {
                break;
            }
        }

        this.beta = m / this.alpha;
    }

    public double Likelihood(double obs) {
        double gamma = 0.0;

        gamma = (Math.pow(this.beta, this.alpha) / GammaDistribution.gamma(this.alpha)) * Math.pow(obs, this.alpha - 1.0) * Math.exp(-1.0 * obs * this.beta);

        return gamma;
    }

    public double Sample() {
        return 0.0;//TODO:must fix...
    }

    public String toString() {
        return "G(" + this.getAlpha() + "," + this.getBeta() + ")";
    }
}
