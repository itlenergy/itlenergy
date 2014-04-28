package com.itl_energy.repo;

import java.util.List;
import javax.ejb.Stateless;

import com.itl_energy.model.HubLog;

/**
 * Provides access to the hub logs stored in the database.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Stateless
public class HubLogCollection extends CollectionBase<HubLog> {

	public HubLogCollection() {
		super(HubLog.class);
	}
    
    public List<HubLog> findByHubId(Integer hubId) {
        return super.createNamedQuery("findByHubId")
                .setParameter("hubId", hubId)
                .getResultList();
    }
	
	@Override
	public int getId(HubLog entity) {
		return entity.getHubLogId();
	}
}
