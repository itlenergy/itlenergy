package com.itl_energy.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the hub database table.
 * 
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Hub.findAll", query="SELECT h FROM Hub h"),
    @NamedQuery(name="Hub.findByHouseId", query="SELECT h FROM Hub h WHERE h.houseId = :houseId")
})
@XmlRootElement
public class Hub implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer hubId;
	private Integer freeStorage;
	private Integer houseId;
	private Date lastUpdate;

	public Hub() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="hub_id")
	public Integer getHubId() {
		return this.hubId;
	}

	public void setHubId(Integer hubId) {
		this.hubId = hubId;
	}


	@Column(name="free_storage")
	public Integer getFreeStorage() {
		return this.freeStorage;
	}

	public void setFreeStorage(Integer freeStorage) {
		this.freeStorage = freeStorage;
	}


	@Column(name="house_id")
	public Integer getHouseId() {
		return this.houseId;
	}

	public void setHouseId(Integer houseId) {
		this.houseId = houseId;
	}


	@Temporal(TemporalType.DATE)
	@Column(name="last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}