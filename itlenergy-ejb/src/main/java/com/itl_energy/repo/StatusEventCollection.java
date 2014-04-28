package com.itl_energy.repo;

import javax.ejb.Stateless;

import com.itl_energy.model.StatusEvent;

/**
 * Provides access to the status events in the database.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Stateless
public class StatusEventCollection extends CollectionBase<StatusEvent> {
	
	public StatusEventCollection() {
		super(StatusEvent.class);
	}
	
	@Override
	public int getId(StatusEvent entity) {
		return entity.getStatusId();
	}
}
