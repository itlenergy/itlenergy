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
import com.itl_energy.model.Actuations;
import com.itl_energy.repo.ActuationCollection;
import com.itl_energy.webcore.AuthRoles;

/**
 * The Web API RESTful interface to the actuatable devices collection.
 * 
 * @author Bruce Stephen
 */
@Path("actuations")
@Stateless
public class ActuationsResource extends ResourceBase<Actuations> {
	@EJB ActuationCollection actuations;
	
	@PostConstruct
	void init() {
		super.collection = this.actuations;
	}
    
    @GET
    @Path("{minTime}/{maxTime}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ItemList<Actuations> getByTime(@PathParam("minTime") String minTime, @PathParam("maxTime") String maxTime) {
        requireRole(AuthRoles.ADMIN);
        IsoDateAdapter da = new IsoDateAdapter();
        return new ItemList<>(actuations.findByDateRange(da.unmarshal(minTime), da.unmarshal(maxTime)));
    }
}
