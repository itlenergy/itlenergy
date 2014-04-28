package com.itl_energy.repo;

import java.util.List;
import javax.ejb.Stateless;

import com.itl_energy.model.Hub;

/**
 * Provides access to the list of hubs stored in the database.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Stateless
public class HubCollection extends CollectionBase<Hub> {

	public HubCollection() {
		super(Hub.class);
	}
    
    
    public List<Hub> findByHouseId(Integer houseId) {
        return super.createNamedQuery("findByHouseId")
                .setParameter("houseId", houseId)
                .getResultList();
    }
	
	@Override
	public int getId(Hub entity) {
		return entity.getHubId();
	}
}
