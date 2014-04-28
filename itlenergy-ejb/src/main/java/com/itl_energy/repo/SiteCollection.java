package com.itl_energy.repo;

import java.util.List;
import javax.ejb.Stateless;

import com.itl_energy.model.Site;

/**
 * Provides access to the list of sites in the database.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 * @author Bruce Stephen <bruce.stephen@strath.ac.uk>
 */
@Stateless
public class SiteCollection extends CollectionBase<Site> {

	public SiteCollection() {
		super(Site.class);
	}
    
	@Override
	public int getId(Site entity) {
		return entity.getSiteId();
	}
        
        /**
         * Returns the site for the given unique ID
         * 
         * @param siteId - the id of the required site
         * @return the site for the given Id or NULL if it doesn't exist 
         */
        public Site findBySiteId(Integer siteId) {
        return super.createNamedQuery("findByID")
                .setParameter("siteId", siteId)
                .getResultList().get(0);
        }
        
        /**
         * Returns the site for the given name
         * 
         * @param siteId - the id of the required site
         * @return the site(s) for the given name - name is not a unique identifier 
         */
        public List<Site> findBySiteName(String siteName) {
        return super.createNamedQuery("findNamed")
                .setParameter("siteName", siteName)
                .getResultList();
        }
}