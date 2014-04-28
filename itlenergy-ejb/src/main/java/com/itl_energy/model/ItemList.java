package com.itl_energy.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Represents a list of items that can be sent over the wire.
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@XmlRootElement
// not ideal, but required for serializer to find the child entities.
@XmlSeeAlso({DeployedSensor.class, House.class, Hub.class, HubLog.class, 
	Measurement.class, Sensor.class, Site.class, StatusEvent.class, Weather.class,
    User.class})
public class ItemList<TEntity> {
	private List<TEntity> items;
	
	public ItemList() {
		this.items = new ArrayList<>();
	}
	
	
	public ItemList(List<TEntity> items) {
		this.items = items;
	}
	
	@XmlAnyElement
	@XmlMixed
	public List<TEntity> getItems() {
		return this.items;
	}
	
	public void setItems(List<TEntity> items) {
		this.items = items;
	}
}
