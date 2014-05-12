package com.itl_energy.webclient.profile.distribution;

public class WishartDistribution {

    protected double n;
    protected double[][] V;

    public WishartDistribution(int d) {
        this.V = new double[d][d];
    }

    public WishartDistribution() {
        this(2);
    }

    public int getDimension() {
        return this.V.length;
    }

    public double getDegreesOfFreedom() {
        return this.n;
    }

    public double[][] getV() {
        return this.V;
    }
}
