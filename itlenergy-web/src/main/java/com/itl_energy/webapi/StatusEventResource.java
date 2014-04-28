package com.itl_energy.webapi;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import com.itl_energy.model.StatusEvent;
import com.itl_energy.repo.StatusEventCollection;

/**
 * The Web API RESTful interface to the Status Event collection.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Path("events")
@Stateless
public class StatusEventResource extends ResourceBase<StatusEvent> {
	@EJB
	StatusEventCollection events;
	
	@PostConstruct
	void init() {
		super.collection = this.events;
	}
}
