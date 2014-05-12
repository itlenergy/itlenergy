package com.itl_energy.webclient.profile.distribution;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class representing a joint distribution between a count random variable and
 * an error variable. No dependence is assumed between the two variables.
 *
 * @author bstephen
 * @version 8th July 2011
 */
public class PoissonNormalDistribution extends MultivariateDistribution {

    protected PoissonDistribution marginal1;
    protected GaussianDistribution marginal2;

    public PoissonNormalDistribution() {
        this.marginal1 = new PoissonDistribution();
        this.marginal2 = new GaussianDistribution();
    }

    public void setLambda(double lmb) {
        this.marginal1.setLambda(lmb);
    }

    public void setMu(double mu) {
        this.marginal2.setMu(mu);
    }

    public void setSigma(double sigma) {
        this.marginal2.setSigma(sigma);
    }

    public double likelihood(double[] obs) {
        //no dependence assumed: 
        return this.marginal1.Likelihood(obs[0]) * this.marginal1.Likelihood(obs[1]);
    }

    public boolean loadModel(DataInputStream reader) {
        try {
            this.setLambda(reader.readDouble());
            this.setMu(reader.readDouble());
            this.setSigma(reader.readDouble());
        }
        catch (IOException ioe) {
            ioe.printStackTrace(System.out);

            return false;
        }

        return true;
    }

    public boolean saveModel(DataOutputStream writer) {
        try {
            writer.writeDouble(this.marginal1.getLambda());
            writer.writeDouble(this.marginal2.getMu());
            writer.writeDouble(this.marginal2.getSigma());
        }
        catch (IOException ioe) {
            ioe.printStackTrace(System.out);

            return false;
        }

        return true;
    }

    public String toString() {
        String s = "PoissonGauss(";

        s += this.marginal1.getLambda();
        s += ",";
        s += this.marginal2.getMu();
        s += ",";
        s += this.marginal2.getSigma();

        s += ")";

        return s;
    }
}
