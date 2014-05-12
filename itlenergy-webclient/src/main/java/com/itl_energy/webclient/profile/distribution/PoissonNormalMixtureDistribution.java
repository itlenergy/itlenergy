package com.itl_energy.webclient.profile.distribution;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Mixture of Poisson Normal distributions for modelling non-stationary
 * count/error co-occurrences.
 *
 * @author bstephen
 * @version 8th July 2011
 */
public class PoissonNormalMixtureDistribution extends MultivariateDistribution {

    protected PoissonNormalDistribution[] mix;
    protected double[] pi;

    public PoissonNormalMixtureDistribution() {
        this.setNumberOfMixtures(1);
    }

    public double likelihood(double[] observe) {
        double like = 0.0;

        for (int i = 0; i < this.pi.length; i++) {
            like += this.pi[i] * this.mix[i].likelihood(observe);
        }

        return like;
    }

    public double likelihood(double[] observe, int i) {
        return this.pi[i] * this.mix[i].likelihood(observe);
    }

    public int dominantMixture(double[] observe) {
        int dom = -1;
        double like = Double.MIN_VALUE;

        for (int i = 0; i < this.pi.length; i++) {
            if (like < this.mix[i].likelihood(observe)) {
                like = this.mix[i].likelihood(observe);
                dom = i;
            }
        }

        return dom;
    }

    public int getNumberOfMixtures() {
        return this.pi.length;
    }

    public void setNumberOfMixtures(int k) {
        this.pi = new double[k];
        this.mix = new PoissonNormalDistribution[k];

        for (int i = 0; i < this.mix.length; i++) {
            this.mix[i] = new PoissonNormalDistribution();
        }
    }

    public double[] pi() {
        return this.pi;
    }

    public PoissonNormalDistribution[] getMixtureModel() {
        return this.mix;
    }

    public boolean loadModelFromFile(String fname) {
        DataInputStream dis;

        try {
			//open the file by creating an InputStream
            //from it:
            FileInputStream fis = new FileInputStream(fname);

            dis = new DataInputStream(fis);

            try {
                int k = dis.readInt();

                this.mix = new PoissonNormalDistribution[k];
                this.pi = new double[k];

                for (int i = 0; i < k; i++) {
                    this.pi[i] = dis.readDouble();
                    this.mix[i] = new PoissonNormalDistribution();
                    this.mix[i].loadModel(dis);
                }

                //close the DataInputStream and the underlying FileInputStream
                dis.close();
            }
            catch (IOException ioe) {
                ioe.printStackTrace(System.out);

                return false;
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace(System.out);

            return false;
        }

        return true;
    }

    public boolean saveModelToFile(String fname) {
        DataOutputStream dos;

        try {
            FileOutputStream fos = new FileOutputStream(fname);
            dos = new DataOutputStream(fos);

            try {
                dos.writeInt(this.mix.length);

                for (int i = 0; i < this.mix.length; i++) {
                    dos.writeDouble(this.pi[i]);
                    this.mix[i].saveModel(dos);
                }

                dos.flush();
                dos.close();
            }
            catch (IOException ioe) {
                ioe.printStackTrace(System.out);
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace(System.out);

            return false;
        }

        return true;
    }

    public String toString() {
        String s = "Mixture of Poisson-Normals: \n";

        for (int i = 0; i < this.mix.length; i++) {
            s += "Mixing: " + this.pi[i];
            s += "\t" + this.mix[i].toString();
            s += "\n";
        }

        return s;
    }

    public PoissonNormalMixtureDistribution replicate() {
        PoissonNormalMixtureDistribution mod = new PoissonNormalMixtureDistribution();

        mod.setNumberOfMixtures(this.getNumberOfMixtures());

        for (int i = 0; i < mod.getNumberOfMixtures(); i++) {
            mod.pi[i] = this.pi[i];

            mod.mix[i].marginal1.setLambda((this.mix[i].marginal1.getLambda()));
            mod.mix[i].marginal2.setMu((this.mix[i].marginal2.getMu()));
            mod.mix[i].marginal2.setSigma((this.mix[i].marginal2.getSigma()));
        }

        return mod;
    }
}
