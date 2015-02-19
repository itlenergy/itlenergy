package com.itl_energy.repo;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

import com.itl_energy.model.Actuations;

/**
 * Provides access to the actuations for a given sensor/device stored in the database.
 * 
 * @author Bruce Stephen
 */
@Stateless
public class ActuationCollection extends CollectionBase<Actuations> {

	public ActuationCollection() {
		super(Actuations.class);
	}
    
    public List<Actuations> findBySensorId(Integer sensorId) {
        return super.createNamedQuery("findBySensorId")
                .setParameter("sensorId", sensorId)
                .getResultList();
    }
    
    public List<Actuations> findByDateRange(Date minTime, Date maxTime) {
        return super.createNamedQuery("findByDateRange")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .getResultList();
    }
    
    public List<Actuations> findByDateRangeAndSensorId(Date minTime, Date maxTime, Integer sensorId) {
        return super.createNamedQuery("findByDateRangeAndSensorId")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .setParameter("sensorId", sensorId)
                .getResultList();
    }
	
	@Override
	public int getId(Actuations entity) {
		return entity.getActuationId();
	}
}
