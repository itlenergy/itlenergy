package com.itl_energy.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The persistent class for the site database table.
 *
 */
@Entity
@Table(name = "site")
@NamedQueries({
    @NamedQuery(name = "Site.findAll", query = "SELECT s FROM Site s"),
    @NamedQuery(name = "Site.findBySiteId", query = "SELECT s FROM Site s WHERE s.siteId = :siteId"),
    @NamedQuery(name = "Site.findNamed", query = "SELECT s FROM Site s WHERE s.siteName=:name"),
    @NamedQuery(name = "Site.findByID", query = "SELECT s FROM Site s WHERE s.siteId=:id"),
    @NamedQuery(name = "Site.findByLatitude", query = "SELECT s FROM Site s WHERE s.latitude = :latitude"),
    @NamedQuery(name = "Site.findByLongitude", query = "SELECT s FROM Site s WHERE s.longitude = :longitude"),
    @NamedQuery(name = "Site.findByAltitude", query = "SELECT s FROM Site s WHERE s.altitude = :altitude"),
    @NamedQuery(name = "Site.findBySiteName", query = "SELECT s FROM Site s WHERE s.siteName = :siteName")})
@XmlRootElement
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer siteId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Float latitude;
    private Float longitude;
    private Float altitude;
    @NotNull
    @Size(min = 1, max = 32)
    private String siteName;
    private Collection<ElectricalLoadForecast> electricalLoadForecastCollection;
    private Collection<WeatherForecast> weatherForecastCollection;
    private Collection<ElectricalLoad> electricalLoadCollection;
    private Collection<House> houseCollection;
    private Collection<Generation> generationCollection;

    public Site() {
    }

    public Site(Integer siteId) {
        this.siteId = siteId;
    }

    public Site(Integer siteId, String siteName) {
        this.siteId = siteId;
        this.siteName = siteName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "site_id")
    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    @Column(name = "latitude")
    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    @Column(name = "longitude")
    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @Column(name = "altitude")
    public Float getAltitude() {
        return altitude;
    }

    public void setAltitude(Float altitude) {
        this.altitude = altitude;
    }

    @Basic(optional = false)
    @Column(name = "site_name")
    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siteId")
    @XmlTransient
    public Collection<ElectricalLoadForecast> getElectricalLoadForecastCollection() {
        return electricalLoadForecastCollection;
    }

    public void setElectricalLoadForecastCollection(Collection<ElectricalLoadForecast> electricalLoadForecastCollection) {
        this.electricalLoadForecastCollection = electricalLoadForecastCollection;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siteId")
    @XmlTransient
    public Collection<WeatherForecast> getWeatherForecastCollection() {
        return weatherForecastCollection;
    }

    public void setWeatherForecastCollection(Collection<WeatherForecast> weatherForecastCollection) {
        this.weatherForecastCollection = weatherForecastCollection;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siteId")
    @XmlTransient
    public Collection<ElectricalLoad> getElectricalLoadCollection() {
        return electricalLoadCollection;
    }

    public void setElectricalLoadCollection(Collection<ElectricalLoad> electricalLoadCollection) {
        this.electricalLoadCollection = electricalLoadCollection;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siteId")
    @XmlTransient
    public Collection<House> getHouseCollection() {
        return houseCollection;
    }

    public void setHouseCollection(Collection<House> houseCollection) {
        this.houseCollection = houseCollection;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siteId")
    @XmlTransient
    public Collection<Generation> getGenerationCollection() {
        return generationCollection;
    }

    public void setGenerationCollection(Collection<Generation> generationCollection) {
        this.generationCollection = generationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (siteId != null ? siteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Site)) {
            return false;
        }
        Site other = (Site) object;
        if ((this.siteId == null && other.siteId != null) || (this.siteId != null && !this.siteId.equals(other.siteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itl_energy.model.Site[ siteId=" + siteId + " ]";
    }
}