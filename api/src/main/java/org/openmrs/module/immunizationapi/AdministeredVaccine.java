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
import java.io.Serializable;

/**
 * Created by Willa aka Baba Imu on 1/31/18.
 */

@Entity(name = "immunizationapi.AdministeredEntity")
@Table(name = "immunizationapi_administered_vaccine")
public class AdministeredVaccine extends BaseOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue
	@Column(name = "administered_vaccine_id")
	private Integer administeredVaccineId;
	
	@ManyToOne
	@JoinColumn(name = "vaccine_configuration_id")
	private VaccineConfiguration vaccineConfiguration;
	
	@OneToOne
	@JoinColumn(name = "obs_id")
	private Obs obs;
	
	public AdministeredVaccine() {
	}
	
	public AdministeredVaccine(VaccineConfiguration vaccineConfiguration, Obs obs) {
		this.vaccineConfiguration = vaccineConfiguration;
		this.obs = obs;
	}
	
	public VaccineConfiguration getVaccineConfiguration() {
		return vaccineConfiguration;
	}
	
	public void setVaccineConfiguration(VaccineConfiguration vaccineConfiguration) {
		this.vaccineConfiguration = vaccineConfiguration;
	}
	
	public Obs getObs() {
		return obs;
	}
	
	public void setObs(Obs obs) {
		this.obs = obs;
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
