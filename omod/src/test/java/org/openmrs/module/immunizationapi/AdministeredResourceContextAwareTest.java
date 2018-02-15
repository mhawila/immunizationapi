package org.openmrs.module.immunizationapi;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.ConceptDescription;
import org.openmrs.ConceptName;
import org.openmrs.api.ConceptService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Willa aka Baba Imu on 2/15/18.
 */
public class AdministeredResourceContextAwareTest extends BaseModuleWebContextSensitiveTest {
	
	private AdministeredVaccineResource resource;
	
	@Autowired
	private ConceptService conceptService;
	
	private Concept conceptWithDateDatatype;
	
	@Before
	public void setup() {
		resource = new AdministeredVaccineResource();
		conceptWithDateDatatype = createConceptWithDateDataType();
	}
	
	@Test
	public void shouldSetNonUuidObsValue() {
		SimpleObject obsProperties = new SimpleObject().add("person", "ba1b19c2-3ed6-4f63-b8c0-f762dc8d7562")
		        .add("concept", conceptWithDateDatatype.getUuid()).add("obsDatetime", "2018-02-16 10:45:50")
		        .add("value", "2018-02-16");
		
		AdministeredVaccine administeredVaccine = new AdministeredVaccine();
		
		assertNull(administeredVaccine.getObs());
		resource.setObs(administeredVaccine, obsProperties);
		assertNotNull(administeredVaccine.getObs());
	}
	
	@Test
	public void shouldSetAUuidObsValue() {
		String existingObsUuid = "be48cdcb-6a76-47e3-9f2e-2635032f3a9a";
		AdministeredVaccine administeredVaccine = new AdministeredVaccine();
		
		assertNull(administeredVaccine.getObs());
		resource.setObs(administeredVaccine, existingObsUuid);
		assertNotNull(administeredVaccine.getObs());
		assertEquals("uuid should equal existing obs", existingObsUuid, administeredVaccine.getObs().getUuid());
	}
	
	private Concept createConceptWithDateDataType() {
		Concept concept = new Concept();
		concept.addDescription(new ConceptDescription("Thi is such pain in the butt!", Locale.getDefault()));
		concept.setPreferredName(new ConceptName("Vaccine for Testing", Locale.getDefault()));
		concept.setDatatype(conceptService.getConceptDatatype(6));
		concept = conceptService.saveConcept(concept);
		return concept;
	}
}
