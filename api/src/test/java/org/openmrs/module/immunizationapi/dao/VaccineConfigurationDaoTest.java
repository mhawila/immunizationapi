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

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.immunizationapi.Interval;
import org.openmrs.module.immunizationapi.SearchMode;
import org.openmrs.module.immunizationapi.TimeUnit;
import org.openmrs.module.immunizationapi.TimeValue;
import org.openmrs.module.immunizationapi.VaccineConfiguration;
import org.openmrs.module.immunizationapi.api.dao.VaccineConfigurationDao;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * It is an integration test (extends BaseModuleContextSensitiveTest), which verifies DAO methods
 * against the in-memory H2 database. The database is initially loaded with data from
 * standardTestDataset.xml in openmrs-api. All test methods are executed in transactions, which are
 * rolled back by the end of each test method.
 */
public class VaccineConfigurationDaoTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	private VaccineConfigurationDao dao;
	
	@Autowired
	private DbSessionFactory sessionFactory;
	
	@Autowired
	private ConceptService conceptService;
	
	private Concept testConcept;
	
	@Before
	public void setup() throws Exception {
		executeDataSet("immunizationapi-api-data-set.xml");
		testConcept = conceptService.getConcept(3);
	}
	
	@Test
	public void saveOrUpdate_shouldSaveANewVaccineConfiguration() {
		VaccineConfiguration vc = new VaccineConfiguration("Polio", new Concept(1000));
		
		assertNull(vc.getId());
		
		vc = dao.saveOrUpdate(vc);
		
		assertNotNull(vc.getId());
	}
	
	@Test
	public void saveOrUpdate_shouldSaveVaccineConfigurationWithIntervals() {
		VaccineConfiguration vc = makeStarterVaccineConfiguration();
		
		assertNull(vc.getId());
		vc = dao.saveOrUpdate(vc);
		assertNotNull(vc.getId());
		
		// Ensure intervals are persisted to the underlying datastore
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Interval.class)
		        .setProjection(Projections.rowCount());
		assertEquals("Two intervals should be persisted", 3l, criteria.uniqueResult());
	}
	
	@Test
	public void saveOrUpdate_shouldSaveVaccineConfigurationWithAgeIfSet() {
		VaccineConfiguration vc = makeStarterVaccineConfiguration();
		
		double ageRequired = 2.0;
		vc.setAgeFirstTimeRequired(ageRequired);
		vc.setAgeUnit(TimeUnit.valueOf("YEARS"));
		
		assertNull(vc.getId());
		vc = dao.saveOrUpdate(vc);
		
		// Ensure the age_first_time_required & age_unit fields are populated. (Pull fresh).
		Integer newId = vc.getId();
		assertNotNull(newId);
		sessionFactory.getCurrentSession().evict(vc);
		
		vc = dao.getById(newId);
		
		assertEquals("age first time should be populated", ageRequired, vc.getAgeFirstTimeRequired(), 0.01);
		assertEquals("age unit should be populated", "YEARS", vc.getAgeUnit().name());
	}
	
	@Test
	public void search_shouldSearchAtTheBeginningWithSearchText() {
		String expectedName = "Example Vaccine";
		List<VaccineConfiguration> configs = dao.search("Example", SearchMode.START, false, 0, 50);
		
		assertFalse(configs.isEmpty());
		assertTrue(configs.get(0).getName().equals(expectedName));
		
		configs = dao.search("No where at the begining", SearchMode.START, true, 0, 50);
		assertTrue(configs.isEmpty());
	}
	
	@Test
	public void search_shouldSearchAtTheEndWithSearchText() {
		String expectedName = "Another Example Vaccine Too";
		List<VaccineConfiguration> configs = dao.search("too", SearchMode.END, false, 0, 50);
		
		assertFalse(configs.isEmpty());
		assertTrue(configs.get(0).getName().equals(expectedName));
		
		configs = dao.search("No where at the begining", SearchMode.END, true, 0, 50);
		assertTrue(configs.isEmpty());
	}
	
	@Test
	public void search_shouldSearchAnywhereWithSearchText() {
		List<VaccineConfiguration> configs = dao.search("Vaccine", SearchMode.ANYWHERE, false, 0, 50);
		assertEquals(2, configs.size());
	}
	
	@Test
	public void searchCountAndCountReturnedShouldBeEqual() {
		List<VaccineConfiguration> configs = dao.search("Vaccine", SearchMode.ANYWHERE, false, 0, 50);
		int count = dao.getCountOfSearch("Vaccine", SearchMode.ANYWHERE, false, 0, 50);
		assertTrue(count == configs.size());
	}
	
	private VaccineConfiguration makeStarterVaccineConfiguration() {
		VaccineConfiguration vc = new VaccineConfiguration("Polio", testConcept);
		vc.addInterval(new Interval(new TimeValue(6.0, TimeUnit.MONTHS), 1, 2));
		vc.addInterval(new Interval(new TimeValue(12.0, TimeUnit.MONTHS), 2, 3));
		
		return vc;
	}
}
