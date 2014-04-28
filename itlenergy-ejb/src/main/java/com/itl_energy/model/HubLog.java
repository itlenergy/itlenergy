package com.itl_energy.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the hub_log database table.
 * 
 */
@Entity
@Table(name="hub_log")
@NamedQueries({
    @NamedQuery(name="HubLog.findAll", query="SELECT h FROM HubLog h"),
    @NamedQuery(name="HubLog.findByHubId", query="SELECT h FROM HubLog h WHERE h.hubId = :hubId")
})
@XmlRootElement
public class HubLog implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer hubLogId;
	private Integer hubId;
	private Integer hubLogCode;
	private String hubLogMessage;
	private Timestamp hubLogTime;

	public HubLog() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="hub_log_id")
	public Integer getHubLogId() {
		return this.hubLogId;
	}

	public void setHubLogId(Integer hubLogId) {
		this.hubLogId = hubLogId;
	}


	@Column(name="hub_id")
	public Integer getHubId() {
		return this.hubId;
	}

	public void setHubId(Integer hubId) {
		this.hubId = hubId;
	}


	@Column(name="hub_log_code")
	public Integer getHubLogCode() {
		return this.hubLogCode;
	}

	public void setHubLogCode(Integer hubLogCode) {
		this.hubLogCode = hubLogCode;
	}


	@Column(name="hub_log_message")
	public String getHubLogMessage() {
		return this.hubLogMessage;
	}

	public void setHubLogMessage(String hubLogMessage) {
		this.hubLogMessage = hubLogMessage;
	}


	@Column(name="hub_log_time")
	public Timestamp getHubLogTime() {
		return this.hubLogTime;
	}

	public void setHubLogTime(Timestamp hubLogTime) {
		this.hubLogTime = hubLogTime;
	}

}