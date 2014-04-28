package com.itl_energy.webapi;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import com.itl_energy.model.HubLog;
import com.itl_energy.repo.HubLogCollection;

/**
 * The Web API RESTful interface to the Hub Log collection 
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Path("hublogs")
@Stateless
public class HubLogResource extends ResourceBase<HubLog> {
	@EJB
	HubLogCollection hublogs;
	
	@PostConstruct
	void init() {
		super.collection = hublogs;
	}
}
