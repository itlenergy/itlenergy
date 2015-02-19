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

import com.itl_energy.model.House;
import com.itl_energy.model.Hub;
import com.itl_energy.model.Tariff;
import com.itl_energy.model.ItemList;
import com.itl_energy.repo.HouseCollection;
import com.itl_energy.repo.HubCollection;
import com.itl_energy.repo.ForecastStatusCollection;
import com.itl_energy.repo.TariffCollection;
import com.itl_energy.webcore.AuthRoles;

/**
 * The Web API RESTful interface to the Houses collection.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 * @author Bruce Stephen
 */
@Path("houses")
@Stateless
public class HouseResource extends ResourceBase<House>
    {
    @EJB HouseCollection houses;
    @EJB HubCollection hubs;
    @EJB TariffCollection tariffs;
    @EJB ForecastStatusCollection forecasts;
    
    @PostConstruct
    void init()
        {
	super.collection = this.houses;
	}
    
    /**
     * Gets all hubs for the specified house.
     * @param id Any valid house id.
     * @return HTTP 404 if house not found, or HTTP 200 ItemList entity response.
     */
    @GET
    @Path("{id}/hubs")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getHubs(@PathParam("id")int id)
        { 
        requireRole(AuthRoles.ADMIN);
        House house = houses.get(id);
        
        if (house == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        else
            {
            List<Hub> items = hubs.findByHouseId(id);
            return Response.ok(new ItemList<>(items)).build();
            }
	}
    
     /**
     * Gets all tariffs for the specified house.
     * @param id Any valid house id.
     * @return HTTP 404 if house not found, or HTTP 200 ItemList entity response.
     */
    @GET
    @Path("{id}/tariffs")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getTariffs(@PathParam("id")int id)
        { 
        requireRole(AuthRoles.ADMIN);
        House house = houses.get(id);
        
        if (house == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        else
            {
            List<Tariff> items = tariffs.findByHouseId(id);
            return Response.ok(new ItemList<>(items)).build();
            }
	}
    }