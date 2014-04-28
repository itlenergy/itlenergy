package com.itl_energy.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the status_events database table.
 * 
 */
@Entity
@Table(name="status_events")
@NamedQuery(name="StatusEvent.findAll", query="SELECT s FROM StatusEvent s")
@XmlRootElement
public class StatusEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer statusId;
	private String statusDescription;

	public StatusEvent() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="status_id")
	public Integer getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}


	@Column(name="status_description")
	public String getStatusDescription() {
		return this.statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

}