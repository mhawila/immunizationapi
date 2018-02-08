package org.openmrs.module.immunizationapi;

import org.openmrs.BaseCustomizableMetadata;
import org.openmrs.Concept;
import org.openmrs.api.APIException;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	@Column(name = "vaccination_configuration_id")
	private Integer vaccinationConfigurationId;
	
	@ManyToOne
	@JoinColumn(name = "concept_id", referencedColumnName = "concept_id")
	private Concept concept;
	
	@Column(name = "number_of_times")
	private Integer numberOfTimes = 1;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vaccineConfiguration")
	private List<Interval> intervals = new ArrayList<>();

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
	
	public List<Interval> getIntervals() {
		return intervals;
	}
	
	public void setIntervals(List<Interval> intervals) {
		this.intervals = intervals;
	}

	public void addInterval(final Interval interval) {
	    if(intervals == null) {
	        intervals = new ArrayList<>();
	        intervals.add(interval);
        }
        else {
	        // Check if there is any with similar rank1 and rank2
            for(Interval iv: intervals) {
            	if(iv.getRank1() == interval.getRank1() && iv.getRank2() == interval.getRank2()) {
					throw new APIException("An interval with the same rank1 and rank2 already exists");
				}
			}
			intervals.add(interval);
        }
    }

    public void addIntervals(Collection<Interval> intervals) {
		for(Interval interval: intervals) {
			addInterval(interval);
		}
	}

}
