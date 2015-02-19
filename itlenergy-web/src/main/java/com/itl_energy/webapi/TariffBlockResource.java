package com.itl_energy.webapi;

import com.itl_energy.model.ItemList;
import com.itl_energy.model.Measurement;
import com.itl_energy.model.Sensor;
import com.itl_energy.model.TariffBlock;
import com.itl_energy.repo.TariffBlockCollection;
import com.itl_energy.webcore.AuthRoles;
import java.net.URI;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import com.itl_energy.model.IsoDateAdapter;
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
 * REST interface to the tariff block entity.
 * 
 * @author Bruce Stephen
 * @version 14th October 2014
 */

@Path("tariff_blocks")
@Stateless
public class TariffBlockResource extends ResourceBase<TariffBlock>
    {
    @EJB TariffBlockCollection tariffBlocks;
	
	@PostConstruct
	void init() {
		super.collection = this.tariffBlocks;
	}
    
    //TODO: how to get multiple queries here...
        
    @GET
    @Path("{tariffID}/tariff_blocks")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ItemList<TariffBlock> getByStartTime(@PathParam("minTime") String startTime) {
        requireRole(AuthRoles.ADMIN);
        IsoDateAdapter da = new IsoDateAdapter();
        return new ItemList<>(tariffBlocks.findByStartTime(da.unmarshal(startTime)));
    }
    }
