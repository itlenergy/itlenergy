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
import com.itl_energy.model.ElectricalLoadForecast;
import com.itl_energy.repo.ElectricalLoadForecastCollection;
import com.itl_energy.webcore.AuthRoles;

/**
 * The Web API RESTful interface to the electrical load forecasts collection.
 * 
 * @author Bruce Stephen
 */
@Path("electrical_load_forecast")
@Stateless
public class ElectricalLoadForecastResource extends ResourceBase<ElectricalLoadForecast> {
	@EJB
	ElectricalLoadForecastCollection loadForecast;
	
	@PostConstruct
	void init() {
		super.collection = this.loadForecast;
	}
    
    @GET
    @Path("{minTime}/{maxTime}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ItemList<ElectricalLoadForecast> getByTime(@PathParam("minTime") String minTime, @PathParam("maxTime") String maxTime) {
        requireRole(AuthRoles.ADMIN);
        IsoDateAdapter da = new IsoDateAdapter();
        return new ItemList<>(loadForecast.findByDateRange(da.unmarshal(minTime), da.unmarshal(maxTime)));
    }
}