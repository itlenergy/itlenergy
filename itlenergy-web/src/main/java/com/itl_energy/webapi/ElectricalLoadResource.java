package com.itl_energy.webapi;

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
import com.itl_energy.model.ElectricalLoad;
import com.itl_energy.repo.ElectricalLoadCollection;
import com.itl_energy.webcore.AuthRoles;

/**
 * The Web API RESTful interface to the electrical loads collection.
 * 
 * @author Bruce Stephen
 */
@Path("electrical_load")
@Stateless
public class ElectricalLoadResource extends ResourceBase<ElectricalLoad> {
	@EJB
	ElectricalLoadCollection load;
	
	@PostConstruct
	void init() {
		super.collection = this.load;
	}
    
    @GET
    @Path("{minTime}/{maxTime}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ItemList<ElectricalLoad> getByTime(@PathParam("minTime") String minTime, @PathParam("maxTime") String maxTime) {
        requireRole(AuthRoles.ADMIN);
        IsoDateAdapter da = new IsoDateAdapter();
        return new ItemList<>(load.findByDateRange(da.unmarshal(minTime), da.unmarshal(maxTime)));
    }
}