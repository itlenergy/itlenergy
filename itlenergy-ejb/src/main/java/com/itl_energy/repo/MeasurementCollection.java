package com.itl_energy.repo;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

import com.itl_energy.model.Measurement;

/**
 * Provides access to the measurement rows stored in the database.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Stateless
public class MeasurementCollection extends CollectionBase<Measurement> {

	public MeasurementCollection() {
		super(Measurement.class);
	}
    
    public List<Measurement> findBySensorId(Integer sensorId) {
        return super.createNamedQuery("findBySensorId")
                .setParameter("sensorId", sensorId)
                .getResultList();
    }
    
    public List<Measurement> findByDateRange(Date minTime, Date maxTime) {
        return super.createNamedQuery("findByDateRange")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .getResultList();
    }
    
    public List<Measurement> findByDateRangeAndSensorId(Date minTime, Date maxTime, Integer sensorId) {
        return super.createNamedQuery("findByDateRangeAndSensorId")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .setParameter("sensorId", sensorId)
                .getResultList();
    }
	
	@Override
	public int getId(Measurement entity) {
		return entity.getMeasurementId();
	}
}
