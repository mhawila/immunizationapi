package org.openmrs.module.immunizationapi;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResourceTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.openmrs.module.immunizationapi.RestTestConstants.EXISTING_CONCEPT_UUID;
import static org.openmrs.module.immunizationapi.RestTestConstants.TEST_DATASET_FILENAME;
import static org.openmrs.module.immunizationapi.RestTestConstants.VACCINE_CONFIGURATION_UUID;
import static org.openmrs.module.immunizationapi.RestTestConstants.VACCINE_NAME;

/**
 * Created by Willa aka Baba Imu on 2/7/18.
 */
public class VaccineConfigurationResourceTest extends BaseDelegatingResourceTest<VaccineConfigurationResource, VaccineConfiguration> {
	
	@Autowired
	private ConceptService conceptService;

	private VaccineConfigurationResource vcResource;

	@Before
	public void setup() throws Exception {
		executeXmlDataSet(TEST_DATASET_FILENAME);
		vcResource = new VaccineConfigurationResource();
	}

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
	public void shouldConvertANewVaccineConfiguration() {
		SimpleObject simpleObject = createSimpleVaccineConfiguration();

		VaccineConfiguration converted = vcResource.convert(simpleObject);

		assertNotNull(converted);
		assertEquals("Name is not set correctly", VACCINE_NAME, converted.getName());
		assertEquals("The concept is not set", EXISTING_CONCEPT_UUID, converted.getConcept().getUuid());
	}

	@Test
	public void shouldConvertANewVaccineConfigurationWithIntervals() {
		SimpleObject toPost = createSimpleVaccineConfiguration();
		List<SimpleObject> intervals = new ArrayList<>();
		intervals.add(new SimpleObject().add("timeUnit", "MONTHS").add("timeValue", 10).add("rank1", 1).add("rank2", 2));
		toPost.add("intervals", intervals);
		VaccineConfiguration converted = vcResource.convert(toPost);

		assertNotNull(converted);
	}

	@Override
	public void validateDefaultRepresentation() throws Exception {
		super.validateDefaultRepresentation();
		assertPropPresent("name");
		assertPropPresent("concept");
		assertPropPresent("intervals");
		assertPropPresent("numberOfTimes");
		assertPropPresent("display");
	}

	@Override
	public void validateFullRepresentation() throws Exception {
		super.validateFullRepresentation();
		validateDefaultRepresentation();
		assertPropPresent("auditInfo");
	}

	private SimpleObject createSimpleVaccineConfiguration() {
		return new SimpleObject().add("name", VACCINE_NAME).add("concept", EXISTING_CONCEPT_UUID)
				.add("numberOfTimes", 5);
	}
}
