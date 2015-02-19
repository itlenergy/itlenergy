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
import com.itl_energy.model.Generation;
import com.itl_energy.repo.GenerationCollection;
import com.itl_energy.webcore.AuthRoles;

/**
 * The Web API RESTful interface to the electrical generation collection.
 * 
 * @author Bruce Stephen
 */
@Path("generation")
@Stateless
public class GenerationResource extends ResourceBase<Generation> {
	@EJB
	GenerationCollection generation;
	
	@PostConstruct
	void init() {
		super.collection = this.generation;
	}
    
    @GET
    @Path("{minTime}/{maxTime}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ItemList<Generation> getByTime(@PathParam("minTime") String minTime, @PathParam("maxTime") String maxTime) {
        requireRole(AuthRoles.ADMIN);
        IsoDateAdapter da = new IsoDateAdapter();
        return new ItemList<>(generation.findByDateRange(da.unmarshal(minTime), da.unmarshal(maxTime)));
    }
}