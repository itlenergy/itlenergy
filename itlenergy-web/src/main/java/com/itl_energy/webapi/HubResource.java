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

import com.itl_energy.model.Hub;
import com.itl_energy.model.HubLog;
import com.itl_energy.model.ItemList;
import com.itl_energy.model.Sensor;
import com.itl_energy.repo.HubCollection;
import com.itl_energy.repo.HubLogCollection;
import com.itl_energy.repo.SensorCollection;
import com.itl_energy.webcore.AuthRoles;
import com.itl_energy.webcore.AuthenticationService;

/**
 * The Web API RESTful interface to the Hubs collection.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Path("hubs")
@Stateless
public class HubResource extends ResourceBase<Hub> {
	@EJB HubCollection hubs;
    @EJB HubLogCollection logs;
    @EJB SensorCollection sensors;
	
	@PostConstruct
	void init() {
		super.collection = this.hubs;
	}
    
    
    /**
	 * Gets all logs for the specified hub.
     * @param id Any valid hub id.
	 * @return HTTP 404 if hub not found, or HTTP 200 ItemList entity response.
	 */
	@GET
    @Path("{hubId}/logs")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getLogs(@PathParam("hubId")int hubId) { 
        requireRole(AuthRoles.ADMIN);
        Hub hub = hubs.get(hubId);
        if (hub == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        else {
            List<HubLog> items = logs.findByHubId(hubId);
            return Response.ok(new ItemList<>(items)).build();
        }
	}
    
    
    /**
	 * Gets all sensors for the specified hub.
     * @param id Any valid hub id.
	 * @return HTTP 404 if hub not found, or HTTP 200 ItemList entity response.
	 */
	@GET
    @Path("{hubId}/sensors")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getSensors(@PathParam("hubId")int hubId) { 
        requireRole(AuthRoles.ADMIN);
        Hub hub = hubs.get(hubId);
        if (hub == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        else {
            List<Sensor> items = sensors.findByHubId(hubId);
            return Response.ok(new ItemList<>(items)).build();
        }
	}
}
