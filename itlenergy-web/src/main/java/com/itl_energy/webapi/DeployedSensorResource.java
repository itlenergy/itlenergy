package com.itl_energy.webapi;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.itl_energy.model.DeployedSensor;
import com.itl_energy.model.ItemList;
import com.itl_energy.model.Sensor;
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
