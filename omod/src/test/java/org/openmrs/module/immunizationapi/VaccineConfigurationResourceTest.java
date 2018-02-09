package org.openmrs.module.immunizationapi;

import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResourceTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.openmrs.module.immunizationapi.RestTestConstants.EXISTING_CONCEPT_UUID;
import static org.openmrs.module.immunizationapi.RestTestConstants.VACCINE_CONFIGURATION_UUID;
import static org.openmrs.module.immunizationapi.RestTestConstants.VACCINE_NAME;

/**
 * Created by Willa aka Baba Imu on 2/7/18.
 */
public class VaccineConfigurationResourceTest extends BaseDelegatingResourceTest<VaccineConfigurationResource, VaccineConfiguration> {
	
	@Autowired
	private ConceptService conceptService;
	
	@Override
	public VaccineConfiguration newObject() {
		VaccineConfiguration vc = new VaccineConfiguration(VACCINE_NAME, new Concept(1000), 3);
		vc.setUuid(VACCINE_CONFIGURATION_UUID);
		return vc;
	}
	
	@Override
	public String getDisplayProperty() {
		return VACCINE_NAME;
	}
	
	@Override
	public String getUuidProperty() {
		return VACCINE_CONFIGURATION_UUID;
	}
	
	@Test
	@Ignore
	public void shouldCreateANewVaccineConfiguration() {
		Concept concept = conceptService.getConcept(1);
		SimpleObject post = new SimpleObject().add("concept", EXISTING_CONCEPT_UUID);
	}
}
