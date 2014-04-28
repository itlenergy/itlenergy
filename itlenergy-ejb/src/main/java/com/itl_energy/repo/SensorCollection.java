package com.itl_energy.repo;

import java.util.List;
import javax.ejb.Stateless;

import com.itl_energy.model.Sensor;

/**
 * Provides access to the list of sensors stored in the database.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Stateless
public class SensorCollection extends CollectionBase<Sensor> {
	
	public SensorCollection() {
		super(Sensor.class);
	}
    
    public List<Sensor> findByHubId(Integer hubId) {
        return super.createNamedQuery("findByHubId")
                .setParameter("hubId", hubId)
                .getResultList();
    }
	
	@Override
	public int getId(Sensor entity) {
		return entity.getSensorId();
	}
}
