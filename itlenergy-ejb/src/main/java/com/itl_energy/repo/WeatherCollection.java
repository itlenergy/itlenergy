package com.itl_energy.repo;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

import com.itl_energy.model.Weather;

/**
 * Provides access to the weather measurements in the database.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Stateless
public class WeatherCollection extends CollectionBase<Weather> {

	public WeatherCollection() {
		super(Weather.class);
	}
    
    public List<Weather> findBySiteId(Integer siteId) {
        return super.createNamedQuery("findBySiteId")
                .setParameter("siteId", siteId)
                .getResultList();
    }
    
    public List<Weather> findByDateRange(Date minTime, Date maxTime) {
        return super.createNamedQuery("findByDateRange")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .getResultList();
    }
    
    public List<Weather> findByDateRangeAndSiteId(Date minTime, Date maxTime, Integer siteId) {
        return super.createNamedQuery("findByDateRangeAndSiteId")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .setParameter("siteId", siteId)
                .getResultList();
    }
	
	@Override
	public int getId(Weather entity) {
		return entity.getWeatherObservationId();
	}
}
