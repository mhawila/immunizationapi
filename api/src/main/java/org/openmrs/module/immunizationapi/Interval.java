package org.openmrs.module.immunizationapi;

import javax.naming.OperationNotSupportedException;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Willa aka Baba Imu on 1/31/18.
 */

@Entity(name = "immunizationapi.Interval")
@Table(name = "immunizationapi_interval", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "vaccine_configuration_id", "rank1", "rank2" }) })
public class Interval implements Serializable {
	
	@Id
	@GeneratedValue
	private Integer interval_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccine_configuration_id")
	private VaccineConfiguration vaccineConfiguration;
	
	@Embedded
	@NotNull
	private TimeValue value;
	
	@Column
	private Integer rank1;
	
	@Column
	private Integer rank2;
	
	public Interval() {
	}
	
	public Interval(TimeValue value) {
		this.value = value;
	}
	
	public Interval(TimeValue value, Integer rank1, Integer rank2) {
		this.value = value;
		
		if (rank1 < 1 || rank2 < 1) {
			throw new IllegalArgumentException("Cannot pass -ve number of ranks values");
		}
		
		if (rank1 >= rank2 || rank2 - rank1 > 1) {
			throw new IllegalArgumentException("rank1 must be 1 less than rank2");
		}
		
		this.rank1 = rank1;
		this.rank2 = rank2;
	}
	
	public VaccineConfiguration getVaccineConfiguration() {
		return vaccineConfiguration;
	}
	
	public void setVaccineConfiguration(VaccineConfiguration vaccineConfiguration) {
		this.vaccineConfiguration = vaccineConfiguration;
	}
	
	public TimeValue getValue() {
		return value;
	}
	
	public void setValue(TimeValue value) {
		this.value = value;
	}
	
	public Integer getInterval_id() {
		return interval_id;
	}
	
	public void setInterval_id(Integer interval_id) {
		this.interval_id = interval_id;
	}
	
	public Integer getRank1() {
		return rank1;
	}
	
	public void setRank1(Integer rank1) {
		this.rank1 = rank1;
	}
	
	public Integer getRank2() {
		return rank2;
	}
	
	public void setRank2(Integer rank2) {
		this.rank2 = rank2;
	}
	
	@Override
	public String toString() {
		return "vaccine: " + vaccineConfiguration + ", rank1: " + rank1 + ", rank2: " + rank2 + ", value: " + value;
	}
}
