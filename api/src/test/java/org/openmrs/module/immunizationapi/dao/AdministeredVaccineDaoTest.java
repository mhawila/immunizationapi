/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.immunizationapi.dao;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.module.immunizationapi.AdministeredVaccine;
import org.openmrs.module.immunizationapi.VaccineConfiguration;
import org.openmrs.module.immunizationapi.api.ImmunizationAPIService;
import org.openmrs.module.immunizationapi.api.dao.AdministeredVaccineDao;
import org.openmrs.module.immunizationapi.api.dao.VaccineConfigurationDao;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * It is an integration test (extends BaseModuleContextSensitiveTest), which verifies DAO methods
 * against the in-memory H2 database. The database is initially loaded with data from
 * standardTestDataset.xml in openmrs-api. All test methods are executed in transactions, which are
 * rolled back by the end of each test method.
 */
public class AdministeredVaccineDaoTest extends BaseModuleContextSensitiveTest {
	
	private static String TEST_FILE_NAME = "immunizationapi-api-data-set.xml";
	
	@Autowired
	private AdministeredVaccineDao administeredVaccineDao;
	
	@Autowired
	private VaccineConfigurationDao vaccineConfigurationDao;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private PatientService patientService;
	
	@Qualifier("immunizationapi.ImmunizationAPIService")
	@Autowired
	private ImmunizationAPIService immunizationAPIService;
	
	private Concept testConcept;
	
	private VaccineConfiguration existingConfiguration;
	
	private Person existingPerson;
	
	private Patient patient7, patient8;
	
	@Before
	public void setup() throws Exception {
		executeDataSet(TEST_FILE_NAME);
		testConcept = conceptService.getConcept(3);
		existingConfiguration = new VaccineConfiguration("Test Vaccine", testConcept, 1);
		existingPerson = personService.getPerson(1);
		
		patient7 = patientService.getPatient(7);
		patient8 = patientService.getPatient(8);
	}
	
	@Test
	public void saveOrUpdate_shouldSaveANewAdministeredVaccine() {
	}
	
	@Test
	public void saveOrUpdate_shouldPersistNewObs() {
		Obs associatedObs = new Obs(existingPerson, testConcept, new Date(), null);
		associatedObs.setValueDatetime(new Date());
		AdministeredVaccine toBeSaved = new AdministeredVaccine(existingConfiguration, associatedObs);
		
		assertNull("Before persisting administered_id should be null", toBeSaved.getId());
		assertNull("Before persisting obs_id should be null", toBeSaved.getObs().getId());
		toBeSaved = administeredVaccineDao.saveOrUpdate(toBeSaved);
		assertNotNull("After persisting administered vaccine should not be null", toBeSaved.getId());
		assertNotNull("After persisting obs id should not be null", toBeSaved.getObs().getId());
	}
	
	@Test
	public void getAdministeredVaccineForPatient_shouldReturnPatientAssociatedVaccines() {
		int count = administeredVaccineDao.getAdministeredVaccinesForPatient(patient7, null, null, null, true).size();
		assertEquals("The patient 7 should have 2 administrations", 2, count);
	}
	
	@Test
	public void getAdministeredVaccineForPatient_shouldNotIncludeVoidedOnes() {
		int count = administeredVaccineDao.getAdministeredVaccinesForPatient(patient8, null, null, null, false).size();
		assertEquals("The patient 8 should have 3 unvoided administration", 3, count);
	}
	
	@Test
	public void getAdministeredVaccineForPatient_shouldFilterCorrectly() {
		Integer startIndex = 0, limit = 2;
		int count = administeredVaccineDao.getAdministeredVaccinesForPatient(patient8, null, null, null, true).size();
		assertEquals("The patient 8 should have 4 total", 4, count);
		
		List<AdministeredVaccine> filtered = administeredVaccineDao.getAdministeredVaccinesForPatient(patient8, null, 0, 2,
		    true);
		assertEquals("The filtered should be 2", 2, filtered.size());
		assertEquals("The first record should be 10001", 10001, (int) filtered.get(0).getObs().getId());
		
		filtered = administeredVaccineDao.getAdministeredVaccinesForPatient(patient8, null, 4, null, true);
		assertTrue("should have no results", filtered.isEmpty());
	}
	
	@Test
	public void getAdministeredVaccineForPatient_shouldReturnAdministrationWithSpecificConfiguration() {
		VaccineConfiguration vc = immunizationAPIService.getVaccineConfigurationById(2);
		
		List<AdministeredVaccine> list = administeredVaccineDao.getAdministeredVaccinesForPatient(patient8, vc, null, null,
		    true);
		
		assertEquals("Should have only one element", 1, list.size());
		assertEquals("should have the same configuration", vc, list.get(0).getVaccineConfiguration());
	}
	
	@Test
	public void getAdministeredVaccineForVaccineConfiguration_shouldReturnForAGivenConfiguration() {
		VaccineConfiguration vc = immunizationAPIService.getVaccineConfigurationById(1);

		List<AdministeredVaccine> list = administeredVaccineDao.getAdministeredVaccineForVaccineConfiguration(vc,
				null, null, true);

		assertTrue("All should have the queried vaccine configuration",
				list.stream().allMatch(administeredVaccine -> administeredVaccine.getVaccineConfiguration().equals(vc)));
	}
	
	@Test
	public void getCountForPatient_shouldReturnCount() {
		int count = administeredVaccineDao.getCountForPatient(patient8, null, true);
		assertEquals("should have 4 records", 4, count);
		
		count = administeredVaccineDao.getCountForPatient(patient7, null, true);
		assertEquals("should have 2 records", 2, count);
	}
	
	@Test
	public void getCountForPatient_shouldReturnOnlyNonVoidedCountForPatient() {
		int count = administeredVaccineDao.getCountForPatient(patient8, null, false);
		assertEquals("It should be 3 only", 3, count);
	}
	
	@Test
	public void getCountForPatient_shouldReturnCountForAParticularVaccineConfiguration() {
		VaccineConfiguration vc = immunizationAPIService.getVaccineConfigurationById(1);
		int count = administeredVaccineDao.getCountForPatient(patient8, vc, true);
		assertEquals("should be 3", 3, count);
	}
}
