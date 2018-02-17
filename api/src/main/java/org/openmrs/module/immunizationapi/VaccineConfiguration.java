package org.openmrs.module.immunizationapi;

import org.openmrs.BaseCustomizableMetadata;
import org.openmrs.Concept;
import org.openmrs.api.APIException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Willa aka Baba Imu on 1/31/18.
 */

@Entity(name = "immunizationapi.VaccineConfiguration")
@Table(name = "immunizationapi_vaccine_configuration")
public class VaccineConfiguration extends BaseCustomizableMetadata implements Serializable {
	
	@Id
	@GeneratedValue
	@Column(name = "vaccine_configuration_id")
	private Integer vaccineConfigurationId;
	
	@ManyToOne
	@JoinColumn(name = "concept_id", referencedColumnName = "concept_id")
	private Concept concept;
	
	@Column(name = "number_of_times")
	private Integer numberOfTimes = 0;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vaccineConfiguration", cascade = CascadeType.ALL)
	private List<Interval> intervals = new ArrayList<>();

	@Column(name = "age_first_time_required")
	private Double ageFirstTimeRequired;

	@Enumerated(EnumType.STRING)
	@Column(name = "age_unit")
	private TimeUnit ageUnit;

	public VaccineConfiguration() {
	}

	public VaccineConfiguration(String name, Concept concept) {
	    setName(name);
		this.concept = concept;
	}
	
	public VaccineConfiguration(String name, Concept concept, Integer numberOfTimes) {
	    setName(name);
		this.concept = concept;
		this.numberOfTimes = numberOfTimes;
	}
	
	public VaccineConfiguration(String name, Concept concept, Integer numberOfTimes, List<Interval> intervals) {
	    setName(name);
		this.concept = concept;
		this.numberOfTimes = numberOfTimes;
		this.intervals = intervals;
	}

	@Override
	public Integer getId() {
		return vaccineConfigurationId;
	}
	
	@Override
	public void setId(Integer id) {
		vaccineConfigurationId = id;
	}
	
	public Integer getVaccineConfigurationId() {
		return vaccineConfigurationId;
	}
	
	public void setVaccineConfigurationId(Integer vaccineConfigurationId) {
		this.vaccineConfigurationId = vaccineConfigurationId;
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
	
	public List<Interval> getIntervals() {
		return intervals;
	}
	
	public void setIntervals(List<Interval> intervals) {
		this.intervals = intervals;

		for(Interval interval: intervals) {
			interval.setVaccineConfiguration(this);
		}
	}

	public Double getAgeFirstTimeRequired() {
		return ageFirstTimeRequired;
	}

	public void setAgeFirstTimeRequired(Double ageFirstTimeRequired) {
		this.ageFirstTimeRequired = ageFirstTimeRequired;
	}

	public TimeUnit getAgeUnit() {
		return ageUnit;
	}

	public void setAgeUnit(TimeUnit ageUnit) {
		this.ageUnit = ageUnit;
	}

	public void addInterval(final Interval interval) {
	    if(intervals == null) {
	        intervals = new ArrayList<>();
        }
        else {
	        // Check if there is any with similar rank1 and rank2
            for(Interval iv: intervals) {
            	if(iv.getRank1() == interval.getRank1() && iv.getRank2() == interval.getRank2()) {
					throw new APIException("An interval with the same rank1 and rank2 already exists");
				}
			}
        }
		intervals.add(interval);
	    interval.setVaccineConfiguration(this);
    }

    public void addIntervals(Collection<Interval> intervals) {
		for(Interval interval: intervals) {
			addInterval(interval);
		}
	}

}
