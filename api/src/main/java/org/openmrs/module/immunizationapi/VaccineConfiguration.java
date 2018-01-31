package org.openmrs.module.immunizationapi;

import org.openmrs.BaseCustomizableMetadata;
import org.openmrs.Concept;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Willa aka Baba Imu on 1/31/18.
 */

@Entity(name = "immunizationapi.VaccineConfiguration")
@Table(name = "immunizationapi_vaccine_configuration")
public class VaccineConfiguration extends BaseCustomizableMetadata {
	
	@Id
	@GeneratedValue
	@Column(name = "vaccination_configuration_id")
	private Integer vaccinationConfigurationId;
	
	@ManyToOne
	@JoinColumn(name = "concept_id", referencedColumnName = "concept_id")
	private Concept concept;
	
	@Column(name = "number_of_times")
	private Integer numberOfTimes = 1;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vaccine_configuration_id")
	private Set<Interval> intervals = new HashSet<Interval>();
	
	public VaccineConfiguration(Concept concept) {
		this.concept = concept;
	}
	
	public VaccineConfiguration(Concept concept, Integer numberOfTimes) {
		this.concept = concept;
		this.numberOfTimes = numberOfTimes;
	}
	
	public VaccineConfiguration(Concept concept, Integer numberOfTimes, Set<Interval> intervals) {
		this.concept = concept;
		this.numberOfTimes = numberOfTimes;
		this.intervals = intervals;
	}
	
	@Override
	public Integer getId() {
		return vaccinationConfigurationId;
	}
	
	@Override
	public void setId(Integer id) {
		vaccinationConfigurationId = id;
	}
	
	public Integer getVaccinationConfigurationId() {
		return vaccinationConfigurationId;
	}
	
	public void setVaccinationConfigurationId(Integer vaccinationConfigurationId) {
		this.vaccinationConfigurationId = vaccinationConfigurationId;
	}
	
	public Concept getConcept() {
		return concept;
	}
	
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
	
	public Integer getNumberOfTimes() {
		return numberOfTimes;
	}
	
	public void setNumberOfTimes(Integer numberOfTimes) {
		this.numberOfTimes = numberOfTimes;
	}
	
	public Set<Interval> getIntervals() {
		return intervals;
	}
	
	public void setIntervals(Set<Interval> intervals) {
		this.intervals = intervals;
	}
}
