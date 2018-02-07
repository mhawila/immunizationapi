package org.openmrs.module.immunizationapi;

import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.validator.ValidateUtil;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertTrue;

/**
 * Created by Willa aka Baba Imu on 2/1/18.
 */
public class AdministeredVaccineValidatorTest extends BaseModuleContextSensitiveTest {
	
	@Test
	public void validate_shouldFailIfVaccineConfigurationIsNull() {
		AdministeredVaccine administeredVaccine = new AdministeredVaccine(null, new Obs(1));
		Errors errors = new BindException(administeredVaccine, "administered");
		ValidateUtil.validate(administeredVaccine, errors);
		assertTrue(errors.hasFieldErrors("vaccineConfiguration"));
	}
	
	@Test
	public void validate_shouldFailIfObsIsNull() {
		VaccineConfiguration vaccineConfiguration = new VaccineConfiguration("Test V", new Concept(1));
		AdministeredVaccine administeredVaccine = new AdministeredVaccine(null, null);
		
		Errors errors = new BindException(administeredVaccine, "administered");
		ValidateUtil.validate(administeredVaccine, errors);
		assertTrue(errors.hasFieldErrors("obs"));
	}
	
	@Test
	public void validate_shouldFailIfProvidedRankDoesNotExistInVaccineConfiguration() {
		VaccineConfiguration vaccineConfiguration = new VaccineConfiguration("Test V", new Concept(1), 1);
		AdministeredVaccine administeredVaccine = new AdministeredVaccine(vaccineConfiguration, null, 2);
		
		Errors errors = new BindException(administeredVaccine, "administered");
		ValidateUtil.validate(administeredVaccine, errors);
		assertTrue(errors.hasFieldErrors("rank"));
	}
}
