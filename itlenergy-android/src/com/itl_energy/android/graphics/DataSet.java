package com.itl_energy.android.graphics;

import java.util.Vector;

public class DataSet {

    protected Vector<double[]> data;
    protected Vector<Long> times;
    protected int cap;
    protected int dimension;
    protected double[] maxs;
    protected double[] mins;

    public DataSet() {
        this.data = new Vector<double[]>();
        this.times = new Vector<Long>();

        this.cap = 100;

        this.setDimension(2);
    }

    public int getSize() {
        return this.data.size();
    }

    public void setDimension(int d) {
        this.dimension = d;

        this.maxs = new double[this.dimension];
        this.mins = new double[this.dimension];

        for (int i = 0; i < this.dimension; i++) {
            this.maxs[i] = Double.MIN_VALUE;
            this.mins[i] = Double.MAX_VALUE;
        }
    }

    public void reset() {
        this.setDimension(this.getDimension());
    }

    public void setCap(int cp) {
        this.cap = cp;
    }

    public long getTimeAt(int i) {
        return this.times.get(i).longValue();
    }

    public double[] getDataAt(int i) {
        return this.data.get(i);
    }

    public double getMin(int d) {
        return this.mins[d];
    }

    public void setMin(int d, double min) {
        this.mins[d] = min;
    }

    public double getMax(int d) {
        return this.maxs[d];
    }

    public void setMax(int d, double max) {
        this.maxs[d] = max;
    }

    public void addData(long time, double[] dat) {
        this.data.add(dat);
        this.times.add(time);

        for (int i = 0; i < this.dimension; i++) {
            if (dat[i] > this.maxs[i]) {
                this.maxs[i] = dat[i];
            }
            if (dat[i] < this.mins[i]) {
                this.mins[i] = dat[i];
            }
        }

        if (this.data.size() > cap) {
            this.data.remove(0);
            this.times.remove(0);
        }
    }

    public int getDimension() {
        return this.dimension;
    }
}
