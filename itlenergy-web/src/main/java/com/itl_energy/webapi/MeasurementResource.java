package com.itl_energy.webapi;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.itl_energy.model.IsoDateAdapter;
import com.itl_energy.model.ItemList;
import com.itl_energy.model.Measurement;
import com.itl_energy.repo.MeasurementCollection;
import com.itl_energy.webcore.AuthRoles;

/**
 * The Web API RESTful interface to the Measurements collection.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Path("measurements")
@Stateless
public class MeasurementResource extends ResourceBase<Measurement> {
	@EJB MeasurementCollection measurements;
	
	@PostConstruct
	void init() {
		super.collection = this.measurements;
	}
    
    @GET
    @Path("{minTime}/{maxTime}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ItemList<Measurement> getByTime(@PathParam("minTime") String minTime, @PathParam("maxTime") String maxTime) {
        requireRole(AuthRoles.ADMIN);
        IsoDateAdapter da = new IsoDateAdapter();
        return new ItemList<>(measurements.findByDateRange(da.unmarshal(minTime), da.unmarshal(maxTime)));
    }
}
