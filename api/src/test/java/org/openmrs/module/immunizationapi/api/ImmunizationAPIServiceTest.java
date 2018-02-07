/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.immunizationapi.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.Concept;
import org.openmrs.api.APIException;
import org.openmrs.api.UserService;
import org.openmrs.module.immunizationapi.VaccineConfiguration;
import org.openmrs.module.immunizationapi.api.dao.VaccineConfigurationDao;
import org.openmrs.module.immunizationapi.api.impl.ImmunizationAPIServiceImpl;
import static org.mockito.Mockito.*;

/**
 * This is a unit test, which verifies logic in ImmunizationAPIService. It doesn't extend
 * BaseModuleContextSensitiveTest, thus it is run without the in-memory DB and Spring context.
 */
public class ImmunizationAPIServiceTest {
	
	@InjectMocks
	ImmunizationAPIServiceImpl basicModuleService;
	
	@Mock
	VaccineConfigurationDao vcDao;
	
	@Mock
	UserService userService;
	
	@Before
	public void setupMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test(expected = APIException.class)
	public void saveVaccinationConfiguration_shouldThrowIfNumberOfTimesIsGreaterThan1ButWithoutInterval()
	        throws APIException {
		VaccineConfiguration vc = new VaccineConfiguration("Polio B", new Concept(2));
		vc.setNumberOfTimes(2);
		when(vcDao.saveOrUpdate(vc)).thenReturn(vc);
		
		basicModuleService.saveVaccineConfiguration(vc);
	}
	
}
