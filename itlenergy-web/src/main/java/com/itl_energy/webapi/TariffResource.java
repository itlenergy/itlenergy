package com.itl_energy.webapi;

import com.itl_energy.model.ItemList;
import com.itl_energy.model.TariffBlock;
import com.itl_energy.model.Tariff;
import com.itl_energy.repo.TariffCollection;
import com.itl_energy.repo.TariffBlockCollection;
import com.itl_energy.model.IsoDateAdapter;
import com.itl_energy.webcore.AuthRoles;
import java.net.URI;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource class for the Tariff entity.
 * 
 * @author Bruce Stephen
 * @version 14th October 2014
 * @version 8th December 2014
 */

@Path("tariffs")
@Stateless
public class TariffResource extends ResourceBase<Tariff>
    {
    @EJB TariffCollection tariffs;
    @EJB TariffBlockCollection blocks;
        
	@PostConstruct
	void init() {
		super.collection = this.tariffs;
	}
        
    @POST
    @Path("{tariffId}/blocks")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addTariffBlock(@PathParam("tariffId")int tariffId, TariffBlock t) {
        Tariff tariff= tariffs.get(tariffId);
        
        // make sure sensor exists
        if (tariff == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        
        // check authorization??
        requireHub(tariffId);
        
        // make sure the measurement id isn't set so we don't get a pkey conflict
        if (t.getTariffBlockId()!= 0)
            return Response.status(Response.Status.BAD_REQUEST).build();
        
        tariff.getTariffBlockCollection().add(t);
        
        URI uri = uriInfo.getAbsolutePathBuilder().path(t.getTariffBlockId().toString()).build();
        return Response.created(uri).build();
    }
    
    
    @PUT
    @Path("{tariffId}/blocks")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addTariffBlock(@PathParam("tariffId")int tariffId, ItemList<TariffBlock> t) {
        Tariff tariff= tariffs.get(tariffId);
        
        // make sure tariff exists
        if (tariff == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        
        // check authorization??
        requireHub(tariffId);
        
        for (TariffBlock tblock : blocks.getAll()) {
            // prevent submitting measurements for other sensors.
            if (tblock.getTariffBlockId() != tariffId)
                return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        blocks.insert(t.getItems());
        return Response.ok().build();
    }
    
    
    @GET
    @Path("{tariffId}/blocks")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getTariffBlocks(@PathParam("tariffId")int tariffId) {
        requireRole(AuthRoles.ADMIN);
        Tariff tariff = tariffs.get(tariffId);
        if (tariff == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        else {
            List<TariffBlock> items = blocks.findByTariffBlockId(tariffId);
            return Response.ok(new ItemList<>(items)).build();
        }
    }
    
    //TODO - this is wrong: should select blocks by bounding time
    
    @GET
    @Path("{tariffId}/blocks/{minTime}/{maxTime}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ItemList<TariffBlock> getBlocksByTime(@PathParam("tariffId")int tariffId, @PathParam("beginTime") String beginTime, @PathParam("endTime") String endTime) {
        requireRole(AuthRoles.ADMIN);
        IsoDateAdapter da = new IsoDateAdapter();
        return new ItemList<>(blocks.findByStartTime(da.unmarshal(beginTime)));
    }
    
    }
