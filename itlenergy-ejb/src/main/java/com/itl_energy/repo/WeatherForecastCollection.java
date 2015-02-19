package com.itl_energy.repo;

import com.itl_energy.model.WeatherForecast;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Provides access to the weather forecasts in the database for a given site.
 * 
 * @author Bruce Stephen
 */
@Stateless
public class WeatherForecastCollection extends CollectionBase<WeatherForecast> {

	public WeatherForecastCollection() {
		super(WeatherForecast.class);
	}
    
    public List<WeatherForecast> findBySiteId(Integer siteId) {
        return super.createNamedQuery("findBySiteId")
                .setParameter("siteId", siteId)
                .getResultList();
    }
    
    public List<WeatherForecast> findByDateRange(Date minTime, Date maxTime) {
        return super.createNamedQuery("findByDateRange")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .getResultList();
    }
    
    public List<WeatherForecast> findByDateRangeAndSiteId(Date minTime, Date maxTime, Integer siteId) {
        return super.createNamedQuery("findByDateRangeAndSiteId")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .setParameter("siteId", siteId)
                .getResultList();
    }
	
	@Override
	public int getId(WeatherForecast entity) {
		return entity.getWeatherForecastId();
	}
}