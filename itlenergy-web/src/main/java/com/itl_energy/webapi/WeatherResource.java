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
import com.itl_energy.model.Weather;
import com.itl_energy.repo.WeatherCollection;
import com.itl_energy.webcore.AuthRoles;

/**
 * The Web API RESTful interface to the weather observations collection.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Path("weather")
@Stateless
public class WeatherResource extends ResourceBase<Weather> {
	@EJB
	WeatherCollection weather;
	
	@PostConstruct
	void init() {
		super.collection = this.weather;
	}
    
    @GET
    @Path("{minTime}/{maxTime}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ItemList<Weather> getByTime(@PathParam("minTime") String minTime, @PathParam("maxTime") String maxTime) {
        requireRole(AuthRoles.ADMIN);
        IsoDateAdapter da = new IsoDateAdapter();
        return new ItemList<>(weather.findByDateRange(da.unmarshal(minTime), da.unmarshal(maxTime)));
    }
}
