package org.openmrs.module.immunizationapi;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResourceTest;

import static org.openmrs.module.immunizationapi.RestTestConstants.ADMINISTERED_VACCINE_UUID;
import static org.openmrs.module.immunizationapi.RestTestConstants.VACCINE_NAME;

public class AdministeredVaccineResourceTest extends BaseDelegatingResourceTest<AdministeredVaccineResource, AdministeredVaccine> {
	
	@Override
	public AdministeredVaccine newObject() {
		VaccineConfiguration vc = new VaccineConfiguration(VACCINE_NAME, new Concept(2000));
		Obs obs = new Obs();
		
		AdministeredVaccine administeredVaccine = new AdministeredVaccine(vc, obs);
		administeredVaccine.setUuid(ADMINISTERED_VACCINE_UUID);
		
		return administeredVaccine;
	}
	
	@Override
	public String getDisplayProperty() {
		return VACCINE_NAME;
	}
	
	@Override
	public String getUuidProperty() {
		return ADMINISTERED_VACCINE_UUID;
	}
}
