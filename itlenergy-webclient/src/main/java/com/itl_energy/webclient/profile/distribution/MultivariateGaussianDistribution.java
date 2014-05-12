package com.itl_energy.webclient.profile.distribution;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Jama.Matrix;

/**
 * Multivariate Gaussian distribution based around the JAMA matrix algebra
 * package:
 *
 * http://math.nist.gov/javanumerics/jama/
 *
 * @author bstephen
 * @version 30th June 2011
 */
public class MultivariateGaussianDistribution {

    public final static int COVARIANCE_UNCONSTRAINED = 0;
    public final static int COVARIANCE_ELLIPTIC = 1;
    public final static int COVARIANCE_SPHERICAL = 2;

    protected double[] mu;
    protected Matrix sigma;
    protected Matrix tau;

    protected int covariance;
    protected double sigmadet;

    public MultivariateGaussianDistribution(int d) {
        this.mu = new double[d];
        this.sigma = new Matrix(d, d);
        this.tau = null;
        this.covariance = MultivariateGaussianDistribution.COVARIANCE_UNCONSTRAINED;
    }

    public MultivariateGaussianDistribution() {
        this(2);
    }

    public int getDimension() {
        return this.mu.length;
    }

    public void setDimension(int d) {
        this.mu = new double[d];
        this.sigma = new Matrix(d, d);
        this.tau = null;
    }

    public double[] getMu() {
        return this.mu;
    }

    public Matrix getPrecisionMatrix() {
        return this.sigma.inverse();
    }

    public double[][] getTau() {
        return this.sigma.inverse().getArray();
    }

    public Matrix getCovarianceMatrix() {
        return this.sigma.inverse();
    }

    public double[][] getSigma() {
        this.tau = null;

        return this.sigma.getArray();
    }

    public double likelihood(double[] obs) {
        if (this.tau == null) {
            this.tau = this.getPrecisionMatrix();
            this.sigmadet = this.sigma.det();
        }

        double like = 0.0;
        double maha = 0.0;
        double expdiv = Math.pow(2 * Math.PI, this.getDimension() * 0.5) * Math.sqrt(this.sigmadet);

        for (int m = 0; m < this.mu.length; m++) {
            for (int n = 0; n < this.mu.length; n++) {
                maha += (obs[m] - this.mu[m]) * this.tau.getArray()[m][n] * (obs[n] - this.mu[n]);
            }
        }

        like += Math.exp(-0.5 * maha) / expdiv;

        return like;
    }

    public void setCovarianceModel(int c) {
        this.covariance = c;
    }

    public boolean isSphericalCovariance() {
        return this.covariance == MultivariateGaussianDistribution.COVARIANCE_SPHERICAL;
    }

    public boolean isEllipticCovariance() {
        return this.covariance == MultivariateGaussianDistribution.COVARIANCE_ELLIPTIC;
    }

    public boolean isUnconstrainedCovariance() {
        return this.covariance == MultivariateGaussianDistribution.COVARIANCE_UNCONSTRAINED;
    }

    public boolean readIn(DataInputStream dis) {
        try {
            int d = dis.readInt();

            this.covariance = dis.readInt();

            for (int i = 0; i < d; i++) {
                this.mu[i] = dis.readDouble();
            }

            for (int i = 0; i < d; i++) {
                for (int j = 0; j < d; j++) {
                    this.sigma.getArray()[i][j] = dis.readDouble();
                }
            }

            return true;
        }
        catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }

        return false;
    }

    public boolean writeOut(DataOutputStream dos) {
        try {
            dos.writeInt(this.mu.length);
            dos.writeInt(this.covariance);

            for (int i = 0; i < this.mu.length; i++) {
                dos.writeDouble(this.mu[i]);
            }

            for (int i = 0; i < this.mu.length; i++) {
                for (int j = 0; j < this.mu.length; j++) {
                    dos.writeDouble(this.sigma.getArray()[i][j]);
                }
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace(System.err);

            return false;
        }

        return true;
    }

    public String toString() {
        String s = "Gaussian: \n";

        s += "	Mean: " + this.mu[0];

        for (int d = 1; d < this.mu.length; d++) {
            s += "," + this.mu[d];
        }

        s += "\n\n";

        for (int d1 = 0; d1 < this.mu.length; d1++) {
            if (d1 == 0) {
                s += "	Cov: " + this.sigma.getArray()[d1][0];
            }
            else {
                s += "	     " + this.sigma.getArray()[d1][0];
            }

            for (int d2 = 1; d2 < this.mu.length; d2++) {
                s += "," + this.sigma.getArray()[d1][d2];
            }

            s += "\n";
        }

        return s;
    }
}
