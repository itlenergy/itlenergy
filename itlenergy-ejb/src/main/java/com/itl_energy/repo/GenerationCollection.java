package com.itl_energy.repo;

import com.itl_energy.model.Generation;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Provides access to the electrical generation observations in the database for a given site.
 * 
 * @author Bruce Stephen
 */
@Stateless
public class GenerationCollection extends CollectionBase<Generation> {

	public GenerationCollection() {
		super(Generation.class);
	}
    
    public List<Generation> findBySiteId(Integer siteId) {
        return super.createNamedQuery("findBySiteId")
                .setParameter("siteId", siteId)
                .getResultList();
    }
    
    public List<Generation> findByDateRange(Date minTime, Date maxTime) {
        return super.createNamedQuery("findByDateRange")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .getResultList();
    }
    
    public List<Generation> findByDateRangeAndSiteId(Date minTime, Date maxTime, Integer siteId) {
        return super.createNamedQuery("findByDateRangeAndSiteId")
                .setParameter("minTime", minTime)
                .setParameter("maxTime", maxTime)
                .setParameter("siteId", siteId)
                .getResultList();
    }
	
	@Override
	public int getId(Generation entity) {
		return entity.getGenerationObservationId();
	}
}