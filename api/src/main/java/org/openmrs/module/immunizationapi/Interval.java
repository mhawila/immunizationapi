package org.openmrs.module.immunizationapi;

import javax.naming.OperationNotSupportedException;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by Willa aka Baba Imu on 1/31/18.
 */

@Entity(name = "immunizationapi.Interval")
@Table(name = "immunizationapi_interval")
public class Interval {
	
	@Column(name = "vaccine_configuration_id")
	@NotNull
	private Integer vaccineConfigurationId;
	
	@Embedded
	@NotNull
	private TimeValue value;
	
	@Column
	private Integer rank1;
	
	@Column
	private Integer rank2;
	
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
	
	public TimeValue getValue() {
		return value;
	}
	
	public void setValue(TimeValue value) {
		this.value = value;
	}
	
	public VaccineConfiguration getVaccineConfiguration() throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Not yet implemented!");
	}
}
