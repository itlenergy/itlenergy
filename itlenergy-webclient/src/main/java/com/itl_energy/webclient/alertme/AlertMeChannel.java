package com.itl_energy.webclient.alertme;

import com.itl_energy.webclient.itl.model.Measurement;
import com.itl_energy.webclient.itl.util.ITLClientUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents the result from an AlertMe API call for a measurement channel.
 * The client deserialises the JSON response into this class.
 */
public class AlertMeChannel {
    private long start;
    private long end;
    private int interval;
    private Map<String, Object> values;
    
    private List<Double> max, min, average;

    /**
     * Gets the start time of the readings in seconds.
     * @return 
     */
    public long getStart() {
        return start;
    }

    /**
     * Gets the end time of the readings in seconds.
     * @return 
     */
    public long getEnd() {
        return end;
    }

    /**
     * Gets the number of seconds between readings.
     * @return 
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Gets the maximum values for the channel.
     * @return 
     */
    public List<Double> getMax() {
        if (max == null && values != null)
            max = (List<Double>) values.get("max");
        
        return max;
    }
    
    /**
     * Gets the maximum values for the channel as a list of Measurement objects.
     * @return 
     */
    public List<Measurement> getMaxMeasurements() {
        return getMeasurements(getMax());
    }

    /**
     * Gets the minimum values for the channel.
     * @return 
     */
    public List<Double> getMin() {
        if (min == null && values != null)
            min = (List<Double>) values.get("min");
        
        return min;
    }
    
    /**
     * Gets the average values for the channel.
     * @return 
     */
    public List<Double> getAverage() {
        if (average == null && values != null)
            average = (List<Double>) values.get("average");
        
        return average;
    }

    /**
     * Converts the channel to a channel representing measurements taken every
     * half hour.
     * @return 
     */
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
    
    
    private List<Measurement> getMeasurements(List<Double> values) {
        List<Measurement> measurements = new ArrayList<>(values.size());
        long time = start * 1000;
        long step = interval * 1000;
        
        for (Double value : values) {
            measurements.add(new Measurement(ITLClientUtilities.millisecondsToDateString(time), value));
            time += step;
        }
        
        return measurements;
    }
}
