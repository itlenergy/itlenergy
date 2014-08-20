package com.itl_energy.webapi;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;
import com.itl_energy.model.DeployedSensor;
import com.itl_energy.repo.DeployedSensorCollection;

/**
 * The Web API RESTful interface to the Houses collection.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Path("deployedsensors")
@Stateless
public class DeployedSensorResource extends ResourceBase<DeployedSensor> {
	@EJB
	DeployedSensorCollection deployedSensors;
	
	@PostConstruct
	void init(){
		super.collection = this.deployedSensors;
	}
}
