package com.itl_energy.repo;

import com.itl_energy.model.ForecastStatus;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Provides access to the electrical generation observations in the database for a given site.
 * 
 * @author Bruce Stephen
 */
@Stateless
public class ForecastStatusCollection extends CollectionBase<ForecastStatus> {

	public ForecastStatusCollection() {
		super(ForecastStatus.class);
	}
    
    public List<ForecastStatus> findBySiteId(Integer siteId) {
        return super.createNamedQuery("findBySiteId")
                .setParameter("siteId", siteId)
                .getResultList();
    }
    
    public List<ForecastStatus> findByDateRange(Date minTime, Date maxTime) {
        return super.createNamedQuery("findByDateRange")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .getResultList();
    }
    
    public List<ForecastStatus> findByDateRangeAndSiteId(Date minTime, Date maxTime, Integer siteId) {
        return super.createNamedQuery("findByDateRangeAndSiteId")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .setParameter("siteId", siteId)
                .getResultList();
    }
	
	@Override
	public int getId(ForecastStatus entity) {
		return entity.getForecastStatusId();
	}
}