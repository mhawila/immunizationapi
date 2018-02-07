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

import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.module.immunizationapi.Interval;
import org.openmrs.module.immunizationapi.TimeUnit;
import org.openmrs.module.immunizationapi.TimeValue;
import org.openmrs.module.immunizationapi.VaccineConfiguration;
import org.openmrs.module.immunizationapi.api.dao.VaccineConfigurationDao;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * It is an integration test (extends BaseModuleContextSensitiveTest), which verifies DAO methods
 * against the in-memory H2 database. The database is initially loaded with data from
 * standardTestDataset.xml in openmrs-api. All test methods are executed in transactions, which are
 * rolled back by the end of each test method.
 */
public class VaccineConfigurationDaoTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	private VaccineConfigurationDao dao;
	
	@Test
	public void saveOrUpdate_shouldSaveANewVaccineConfiguration() {
		VaccineConfiguration vc = new VaccineConfiguration("Polio", new Concept(1000));
		
		assertNull(vc.getId());
		
		vc = dao.saveOrUpdate(vc);
		
		assertNotNull(vc.getId());
	}
	
	@Test
	public void saveOrUpdate_shouldSaveVaccineConfigurationWithIntervals() {
		VaccineConfiguration vc = new VaccineConfiguration("Polio", new Concept(1000));
		vc.addInterval(new Interval(new TimeValue(6.0, TimeUnit.MONTHS), 1, 2));
		vc.addInterval(new Interval(new TimeValue(12.0, TimeUnit.MONTHS), 2, 3));
		
		assertNull(vc.getId());
		vc = dao.saveOrUpdate(vc);
		assertNotNull(vc);
	}
	
}
