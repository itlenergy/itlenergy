/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.webclient.instee.alertme.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author stewart
 */
public class AlertMeChannel {

    private long start;
    private long end;
    private int interval;
    private Map<String, Object> values;
    
    private List<Double> max, min, average;

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public int getInterval() {
        return interval;
    }

    public List<Double> getMax() {
        if (max == null && values != null)
            max = (List<Double>) values.get("max");
        
        return max;
    }

    public List<Double> getMin() {
        if (min == null && values != null)
            min = (List<Double>) values.get("min");
        
        return min;
    }
    
    public List<Double> getAverage() {
        if (average == null && values != null)
            average = (List<Double>) values.get("average");
        
        return average;
    }

    public AlertMeChannel toHalfHourly() {
        AlertMeChannel channel = new AlertMeChannel();
        
        int offset = (int)(1800 - start % 1800);
        int obsoffset = offset / interval;
        
        channel.start = start + offset;
        channel.end = end - end % 1800;
        channel.interval = 1800;
        
        List<Double> max = getMax();
        
        if (max != null)
            channel.max = toHalfHourly(obsoffset, max);
        
        List<Double> min = getMin();
        
        if (min != null)
            channel.min = toHalfHourly(obsoffset, min);
        
        List<Double> average = getAverage();
        
        if (average != null)
            channel.average = toHalfHourly(obsoffset, average);
        
        return channel;
    }

    private List<Double> toHalfHourly(int offset, List<Double> observations) {
        //align to the half hour
        int step = 1800 / interval;
        List<Double> halfhourly = new ArrayList<>();
        
        for (int i = offset; i < observations.size(); i += step) {
            halfhourly.add(observations.get(i));
        }
        
        return halfhourly;
    }
}
