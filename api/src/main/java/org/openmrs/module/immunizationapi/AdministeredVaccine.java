package org.openmrs.module.immunizationapi;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Obs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by Willa aka Baba Imu on 1/31/18.
 */

@Entity(name = "immunizationapi.AdministeredEntity")
@Table(name = "immunizationapi_administered_vaccine")
public class AdministeredVaccine extends BaseOpenmrsData {
	
	@Id
	@GeneratedValue
	@Column(name = "administered_vaccine_id")
	private Integer administeredVaccineId;
	
	@ManyToOne
	@JoinColumn(name = "vaccine_configuration_id")
	private VaccineConfiguration vaccineConfiguration;
	
	@OneToOne
	@JoinColumn(name = "obs_id")
	private Obs associatedObs;
	
	@Column
	private Integer rank = null; //first, second ...e.t.c
	
	public AdministeredVaccine(VaccineConfiguration vaccineConfiguration, Obs associatedObs) {
		this.vaccineConfiguration = vaccineConfiguration;
		this.associatedObs = associatedObs;
	}
	
	public AdministeredVaccine(VaccineConfiguration vaccineConfiguration, Obs associatedObs, Integer rank) {
		this.vaccineConfiguration = vaccineConfiguration;
		this.associatedObs = associatedObs;
		this.rank = rank;
	}
	
	public VaccineConfiguration getVaccineConfiguration() {
		return vaccineConfiguration;
	}
	
	public void setVaccineConfiguration(VaccineConfiguration vaccineConfiguration) {
		this.vaccineConfiguration = vaccineConfiguration;
	}
	
	public Obs getAssociatedObs() {
		return associatedObs;
	}
	
	public void setAssociatedObs(Obs associatedObs) {
		this.associatedObs = associatedObs;
	}
	
	public Integer getRank() {
		return rank;
	}
	
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	
	@Override
	public Integer getId() {
		return administeredVaccineId;
	}
	
	@Override
	public void setId(Integer id) {
		administeredVaccineId = id;
	}
}
