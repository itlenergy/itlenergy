package com.itl_energy.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the site database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Site.findAll", query="SELECT s FROM Site s"),
@NamedQuery(name="Site.findNamed", query="SELECT s FROM Site s WHERE s.siteName=:name"),
@NamedQuery(name="Site.findByID", query="SELECT s FROM Site s WHERE s.siteId=:id")})
@XmlRootElement
public class Site implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer siteId;
	private float altitude;
	private float latitude;
	private float longitude;
	private String siteName;

	public Site() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="site_id")
	public Integer getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}


	public float getAltitude() {
		return this.altitude;
	}

	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}


	public float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}


	public float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}


	@Column(name="site_name")
	public String getSiteName() {
		return this.siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

}