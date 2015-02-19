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
import com.itl_energy.model.ForecastStatus;
import com.itl_energy.repo.ForecastStatusCollection;
import com.itl_energy.webcore.AuthRoles;

/**
 * The Web API RESTful interface to the individual household forecast collection.
 * 
 * @author Bruce Stephen
 */
@Path("forecast_status")
@Stateless
public class ForecastStatusResource extends ResourceBase<ForecastStatus> {
	@EJB
	ForecastStatusCollection forecastStatus;
	
	@PostConstruct
	void init() {
		super.collection = this.forecastStatus;
	}
    
    @GET
    @Path("{minTime}/{maxTime}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ItemList<ForecastStatus> getByTime(@PathParam("minTime") String minTime, @PathParam("maxTime") String maxTime) {
        requireRole(AuthRoles.ADMIN);
        IsoDateAdapter da = new IsoDateAdapter();
        return new ItemList<>(forecastStatus.findByDateRange(da.unmarshal(minTime), da.unmarshal(maxTime)));
    }
}