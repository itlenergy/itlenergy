package com.itl_energy.android.graphics;

public class TimeSeriesDataSet extends DataSet {

    protected DateManipulator dateParse;

    public TimeSeriesDataSet() {
        super();

        this.dateParse = new DateManipulator();
    }

    public void clear() {
        this.data.clear();
        this.times.clear();
    }

    public void addData(String time, double[] dat) {
        this.addData(this.dateParse.timeToUTC(time), dat);
    }
}
