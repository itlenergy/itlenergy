package com.itl_energy.repo;

import javax.ejb.Stateless;

import com.itl_energy.model.DeployedSensor;

/**
 * Provides access to the list of Deployed Sensors in the database.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Stateless
public class DeployedSensorCollection extends CollectionBase<DeployedSensor> {

	public DeployedSensorCollection() {
		super(DeployedSensor.class);
	}
    

	@Override
	public int getId(DeployedSensor entity) {
		return entity.getTypeId();
	}

}
