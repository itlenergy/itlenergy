package com.itl_energy.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the house database table.
 * 
 */
@Entity
@NamedQueries({
    @NamedQuery(name="House.findAll", query="SELECT h FROM House h"),
    @NamedQuery(name="House.findBySiteId", query="SELECT h FROM House h WHERE h.siteId = :siteId")
})
@XmlRootElement
public class House implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer houseId;
	private Boolean centralHeatingGas;
	private Boolean cookingGas;
	private Integer floors;
	private Integer occupants;
	private Integer rooms;
	private Integer siteId;

	public House() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="house_id")
	public Integer getHouseId() {
		return this.houseId;
	}

	public void setHouseId(Integer houseId) {
		this.houseId = houseId;
	}


	@Column(name="central_heating_gas")
	public Boolean getCentralHeatingGas() {
		return this.centralHeatingGas;
	}

	public void setCentralHeatingGas(Boolean centralHeatingGas) {
		this.centralHeatingGas = centralHeatingGas;
	}


	@Column(name="cooking_gas")
	public Boolean getCookingGas() {
		return this.cookingGas;
	}

	public void setCookingGas(Boolean cookingGas) {
		this.cookingGas = cookingGas;
	}


	public Integer getFloors() {
		return this.floors;
	}

	public void setFloors(Integer floors) {
		this.floors = floors;
	}


	public Integer getOccupants() {
		return this.occupants;
	}

	public void setOccupants(Integer occupants) {
		this.occupants = occupants;
	}


	public Integer getRooms() {
		return this.rooms;
	}

	public void setRooms(Integer rooms) {
		this.rooms = rooms;
	}


	@Column(name="site_id")
	public Integer getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

}