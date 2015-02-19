package com.itl_energy.webapi;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.itl_energy.model.House;
import com.itl_energy.model.IsoDateAdapter;
import com.itl_energy.model.ItemList;
import com.itl_energy.model.Site;
import com.itl_energy.model.Weather;
import com.itl_energy.repo.HouseCollection;
import com.itl_energy.repo.SiteCollection;
import com.itl_energy.repo.WeatherCollection;
import com.itl_energy.webcore.AuthRoles;


/**
 * The Web API RESTful interface to the Sites collection.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 * @author Bruce Stephen
 */
@Path("sites")
@Stateless
public class SiteResource extends ResourceBase<Site> {
	@EJB SiteCollection sites;
    @EJB HouseCollection houses;
    @EJB WeatherCollection weather;
	
	@PostConstruct
	void init() {
		super.collection = this.sites;
	}
    
    
        /**
	 * Gets a single item specified by the given name.
	 * @param name The name of the Site to retrieve.
	 * @return The Site if found, or a 404 response.
	 */
	/*@GET
	@Path("{name}")
        @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getSingle(@PathParam("name")String name) {
        requireRole(getRole);
        
        List<Site> site = sites.findBySiteName(name);
		if (site != null) {
			return Response.ok(new ItemList<>(site)).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}*/
        
    /**
	 * Gets all houses for the specified site.
     * @param id Any valid site id.
	 * @return HTTP 404 if site not found, or HTTP 200 ItemList entity response.
	 */
	@GET
    @Path("{id}/houses")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getHouses(@PathParam("id")int id) { 
        requireRole(AuthRoles.ADMIN);
        Site site = sites.get(id);
        if (site == null)
            return Response.status(Status.NOT_FOUND).build();
        else {
            List<House> items = houses.findBySiteId(id);
            return Response.ok(new ItemList<>(items)).build();
        }
	}
    
	/**
	 * Bulk inserts weather for a given site
     * @param id Any valid site id.
	 * @return HTTP 404 if site not found, HTTP 403 if not authorised to write weather data
	 * for the site or HTTP 200 ItemList entity response.
	 */
	 @PUT
	    @Path("{id}/weather")
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	    public Response addWeather(@PathParam("id")int id, ItemList<Weather> w) {
	    	Site site = sites.get(id);
	    	
	    	if (site == null)
	    		return Response.status(Status.NOT_FOUND).build();
	        
	        // check authorization
	        requireSite(id);
	        
	        for (Weather weather : w.getItems()) {
	            // prevent submitting weather for other sites.
	            if (weather.getSiteId() != id)
	                return Response.status(Response.Status.FORBIDDEN).build();
	        }
	        
	        weather.insert(w.getItems());
	        return Response.ok().build();
	    }
	 
    /**
	 * Gets all weather observations for the specified site.
     * @param id Any valid site id.
	 * @return HTTP 404 if site not found, or HTTP 200 ItemList entity response.
	 */
	@GET
    @Path("{id}/weather")
	@Produces({"application/json", "application/xml"})
	public Response getWeather(@PathParam("id")int id) { 
        requireRole(AuthRoles.ADMIN);
        Site site = sites.get(id);
        if (site == null)
            return Response.status(Status.NOT_FOUND).build();
        else {
            List<Weather> items = weather.findBySiteId(id);
            return Response.ok(new ItemList<>(items)).build();
        }
	}
    
    
    @GET
    @Path("{id}/weather/{minTime}/{maxTime}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ItemList<Weather> getMeasurementsByTime(@PathParam("id")int id, @PathParam("minTime") String minTime, @PathParam("maxTime") String maxTime) {
        requireRole(AuthRoles.ADMIN);
        IsoDateAdapter da = new IsoDateAdapter();
        return new ItemList<>(weather.findByDateRangeAndSiteId(da.unmarshal(minTime), da.unmarshal(maxTime), id));
    }
}