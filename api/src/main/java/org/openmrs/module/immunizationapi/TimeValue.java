package org.openmrs.module.immunizationapi;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by Willa aka Baba Imu on 1/31/18.
 */
@Embeddable
public class TimeValue {
	
	@Column(name = "time_value")
	private Double value;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "time_unit")
	private TimeUnit unit;
	
	public TimeValue() {
	}
	
	public TimeValue(Double value, TimeUnit unit) {
		this.unit = unit;
		this.value = value;
	}
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
	
	public TimeUnit getUnit() {
		return unit;
	}
	
	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}
	
	@Override
	public String toString() {
		return value + " " + unit;
	}
}
