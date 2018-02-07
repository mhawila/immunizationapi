package org.openmrs.module.immunizationapi;

import org.openmrs.annotation.Handler;
import org.openmrs.validator.BaseCustomizableValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by Willa aka Baba Imu on 2/1/18.
 */
@Handler(supports = { AdministeredVaccine.class }, order = 10)
public class AdministeredVaccineValidator extends BaseCustomizableValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> aClass) {
		return AdministeredVaccine.class.isAssignableFrom(aClass);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		AdministeredVaccine administeredVaccine = (AdministeredVaccine) target;
		ValidationUtils.rejectIfEmpty(errors, "vaccineConfiguration",
		    "AdministeredVaccine.error.vaccineConfiguration.required");
		ValidationUtils.rejectIfEmpty(errors, "obs", "AdministeredVaccine.error.obs.required");
	}
}
