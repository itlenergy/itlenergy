/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itl_energy.webclient.netatmo;

import com.itl_energy.webclient.itl.model.Measurement;
import com.itl_energy.webclient.itl.util.ITLClientUtilities;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stewart
 */
public class NetatmoMeasurement {
    private long beg_time, step_time;
    private double[] value;
    
    public long getStartTime() {
        return beg_time * 1000;
    }
    
    public long getStepTime() {
        return step_time * 1000;
    }
    
    public double[] getValues() {
        return value;
    }
    
    public List<Measurement> getMeasurements() {
        List<Measurement> measurements = new ArrayList<>(value.length);
        long time = getStartTime();
        long step = getStepTime();
        
        for (int i = 0; i < value.length; i++) {
            measurements.add(new Measurement(ITLClientUtilities.millisecondsToDateString(time), value[i]));
            time += step;
        }
        
        return measurements;
    }
}
