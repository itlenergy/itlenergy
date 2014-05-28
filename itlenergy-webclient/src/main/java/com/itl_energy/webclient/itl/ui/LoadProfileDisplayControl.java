package com.itl_energy.webclient.itl.ui;

import java.awt.Dimension;

import com.itl_energy.webclient.profile.visualisation.FilledAreaGraphControl;

/**
 * Control for displaying load profile over a selected period.
 *
 * @author Bruce Stephen
 * @date 18th July 2013
 */
public class LoadProfileDisplayControl extends FilledAreaGraphControl {

    private static final long serialVersionUID = -1646910558940246090L;
    protected FilledAreaGraphControl profile;

    public LoadProfileDisplayControl() {
        this.profile = new FilledAreaGraphControl();

		//this.add(this.profile);
        this.setMinimumSize(new Dimension(500, 400));

        this.addData(null);
        this.repaint();
    }

	//TODO: will change when real data arrives...
    public void addData(double[][] data) {
        double[][] pdf = new double[48][2];

        for (int j = 0; j < 48; j++) {
            pdf[j][0] = (j + 1) * 10.0 / 100.0;
            pdf[j][1] = Math.random() * 2.5;
        }

        super.addData(pdf);
    }
}
