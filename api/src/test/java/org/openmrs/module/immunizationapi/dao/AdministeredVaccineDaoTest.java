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
import org.openmrs.Person;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PersonService;
import org.openmrs.module.immunizationapi.AdministeredVaccine;
import org.openmrs.module.immunizationapi.VaccineConfiguration;
import org.openmrs.module.immunizationapi.api.dao.AdministeredVaccineDao;
import org.openmrs.module.immunizationapi.api.dao.VaccineConfigurationDao;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * It is an integration test (extends BaseModuleContextSensitiveTest), which verifies DAO methods
 * against the in-memory H2 database. The database is initially loaded with data from
 * standardTestDataset.xml in openmrs-api. All test methods are executed in transactions, which are
 * rolled back by the end of each test method.
 */
public class AdministeredVaccineDaoTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	private AdministeredVaccineDao administeredVaccineDao;
	
	@Autowired
	private VaccineConfigurationDao vaccineConfigurationDao;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private PersonService personService;
	
	private Concept testConcept;
	
	private VaccineConfiguration existingConfiguration;
	
	private Person existingPerson;
	
	@Before
	public void setup() {
		testConcept = conceptService.getConcept(3);
		existingConfiguration = new VaccineConfiguration("Test Vaccine", testConcept, 1);
		existingPerson = personService.getPerson(1);
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
}
