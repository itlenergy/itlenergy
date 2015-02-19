package com.itl_energy.repo;

import com.itl_energy.model.ElectricalLoad;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Provides access to the electrical load observations in the database for a given site.
 * 
 * @author Bruce Stephen
 */
@Stateless
public class ElectricalLoadCollection extends CollectionBase<ElectricalLoad> {

	public ElectricalLoadCollection() {
		super(ElectricalLoad.class);
	}
    
    public List<ElectricalLoad> findBySiteId(Integer siteId) {
        return super.createNamedQuery("findBySiteId")
                .setParameter("siteId", siteId)
                .getResultList();
    }
    
    public List<ElectricalLoad> findByDateRange(Date minTime, Date maxTime) {
        return super.createNamedQuery("findByDateRange")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .getResultList();
    }
    
    public List<ElectricalLoad> findByDateRangeAndSiteId(Date minTime, Date maxTime, Integer siteId) {
        return super.createNamedQuery("findByDateRangeAndSiteId")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .setParameter("siteId", siteId)
                .getResultList();
    }
	
	@Override
	public int getId(ElectricalLoad entity) {
		return entity.getElectricalLoadObservationId();
	}
}