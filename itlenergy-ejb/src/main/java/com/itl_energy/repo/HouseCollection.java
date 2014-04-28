package com.itl_energy.repo;

import java.util.List;
import javax.ejb.Stateless;

import com.itl_energy.model.House;

/**
 * Provides access to the list of houses stored in the database.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Stateless
public class HouseCollection extends CollectionBase<House> {

	public HouseCollection() {
		super(House.class);
	}
    
    
    public List<House> findBySiteId(Integer siteId) {
        return super.createNamedQuery("findBySiteId")
                .setParameter("siteId", siteId)
                .getResultList();
    }
    
	
	@Override
	public int getId(House entity) {
		return entity.getHouseId();
	}
}
