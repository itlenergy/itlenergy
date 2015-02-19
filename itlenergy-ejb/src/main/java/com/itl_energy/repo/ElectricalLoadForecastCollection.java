package com.itl_energy.repo;

import com.itl_energy.model.ElectricalLoadForecast;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Provides access to the electrical load forecasts in the database for a given site.
 * 
 * @author Bruce Stephen
 */
@Stateless
public class ElectricalLoadForecastCollection extends CollectionBase<ElectricalLoadForecast> {

	public ElectricalLoadForecastCollection() {
		super(ElectricalLoadForecast.class);
	}
    
    public List<ElectricalLoadForecast> findBySiteId(Integer siteId) {
        return super.createNamedQuery("findBySiteId")
                .setParameter("siteId", siteId)
                .getResultList();
    }
    
    public List<ElectricalLoadForecast> findByDateRange(Date minTime, Date maxTime) {
        return super.createNamedQuery("findByDateRange")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .getResultList();
    }
    
    public List<ElectricalLoadForecast> findByDateRangeAndSiteId(Date minTime, Date maxTime, Integer siteId) {
        return super.createNamedQuery("findByDateRangeAndSiteId")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .setParameter("siteId", siteId)
                .getResultList();
    }
	
	@Override
	public int getId(ElectricalLoadForecast entity) {
		return entity.getElectricalLoadForecastId();
	}
}