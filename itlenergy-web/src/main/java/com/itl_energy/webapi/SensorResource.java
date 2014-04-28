package com.itl_energy.webapi;

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
import com.itl_energy.model.IsoDateAdapter;
import com.itl_energy.model.ItemList;
import com.itl_energy.model.Measurement;
import com.itl_energy.model.Sensor;
import com.itl_energy.repo.MeasurementCollection;
import com.itl_energy.repo.SensorCollection;
import com.itl_energy.webcore.AuthRoles;

/**
 * The Web API RESTful interface to the Sensor collection.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Path("sensors")
@Stateless
public class SensorResource extends ResourceBase<Sensor> {
	@EJB SensorCollection sensors;
    @EJB MeasurementCollection measurements;
	
	@PostConstruct
	void init() {
		super.collection = this.sensors;
	}
    
    
    @POST
    @Path("{sensorId}/measurements")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addMeasurement(@PathParam("sensorId")int sensorId, Measurement m) {
        Sensor sensor = sensors.get(sensorId);
        
        // make sure sensor exists
        if (sensor == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        
        // check authorization
        requireHub(sensorId);
        
        // make sure the measurement id isn't set so we don't get a pkey conflict
        if (m.getMeasurementId() != 0)
            return Response.status(Response.Status.BAD_REQUEST).build();
        
        measurements.insert(m);
        URI uri = uriInfo.getAbsolutePathBuilder().path(m.getMeasurementId().toString()).build();
        return Response.created(uri).build();
    }
    
    
    @PUT
    @Path("{sensorId}/measurements")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addMeasurements(@PathParam("sensorId")int sensorId, ItemList<Measurement> m) {
        Sensor sensor = sensors.get(sensorId);
        
        // make sure sensor exists
        if (sensor == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        
        // check authorization
        requireHub(sensorId);
        
        for (Measurement measurement : m.getItems()) {
            // prevent submitting measurements for other sensors.
            if (measurement.getSensorId() != sensorId)
                return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        measurements.insert(m.getItems());
        return Response.ok().build();
    }
    
    
    @GET
    @Path("{sensorId}/measurements")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getMeasurements(@PathParam("sensorId")int sensorId) {
        requireRole(AuthRoles.ADMIN);
        Sensor sensor = sensors.get(sensorId);
        if (sensor == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        else {
            List<Measurement> items = measurements.findBySensorId(sensorId);
            return Response.ok(new ItemList<>(items)).build();
        }
    }
    
    
    @GET
    @Path("{sensorId}/measurements/{minTime}/{maxTime}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ItemList<Measurement> getMeasurementsByTime(@PathParam("sensorId")int sensorId, @PathParam("minTime") String minTime, @PathParam("maxTime") String maxTime) {
        requireRole(AuthRoles.ADMIN);
        IsoDateAdapter da = new IsoDateAdapter();
        return new ItemList<>(measurements.findByDateRangeAndSensorId(da.unmarshal(minTime), da.unmarshal(maxTime), sensorId));
    }
}
