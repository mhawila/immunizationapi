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

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.immunizationapi.AdministeredVaccine;
import org.openmrs.module.immunizationapi.ImmunizationAPIConfig;
import org.openmrs.module.immunizationapi.VaccineConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The main service of this module, which is exposed for other modules. See
 * moduleApplicationContext.xml on how it is wired up.
 */
public interface ImmunizationAPIService extends OpenmrsService {
	
	/**
	 * @param vaccineConfiguration
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional
	VaccineConfiguration saveVaccineConfiguration(VaccineConfiguration vaccineConfiguration) throws APIException;
	
	/**
	 * @param vaccineConfiguration
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional
	VaccineConfiguration retireVaccineConfiguration(VaccineConfiguration vaccineConfiguration, String reason)
	        throws APIException;
	
	/**
	 * @param vaccineConfiguration
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional
	VaccineConfiguration unretireVaccineConfiguration(VaccineConfiguration vaccineConfiguration) throws APIException;
	
	/**
	 * @param vaccineConfiguration
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional
	void purgeVaccineConfiguration(VaccineConfiguration vaccineConfiguration) throws APIException;
	
	/**
	 * @param id
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	VaccineConfiguration getVaccineConfigurationById(Integer id) throws APIException;
	
	/**
	 * @param uuid
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	VaccineConfiguration getVaccineConfigurationByUuid(String uuid) throws APIException;
	
	/**
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<VaccineConfiguration> getAllVaccineConfigurations() throws APIException;
	
	/**
	 * @param includeRetired
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<VaccineConfiguration> getAllVaccineConfigurations(boolean includeRetired) throws APIException;
	
	/**
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	Integer getCountOfAllVaccineConfigurations() throws APIException;
	
	/**
	 * @param includeRetired
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	Integer getCountOfAllVaccineConfigurations(boolean includeRetired) throws APIException;
	
	/**
	 * @param administeredVaccine
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional
	AdministeredVaccine saveAdministeredVaccine(AdministeredVaccine administeredVaccine) throws APIException;
	
	/**
	 * @param administeredVaccine
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional
	AdministeredVaccine voidAdministeredVaccine(AdministeredVaccine administeredVaccine, String reason) throws APIException;
	
	/**
	 * @param administeredVaccine
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional
	AdministeredVaccine unVoidAdministeredVaccine(AdministeredVaccine administeredVaccine) throws APIException;
	
	/**
	 * @param administeredVaccine
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional
	void purgeAdministeredVaccine(AdministeredVaccine administeredVaccine) throws APIException;
	
	/**
	 * @param id
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	AdministeredVaccine getAdministeredVaccineById(Integer id) throws APIException;
	
	/**
	 * @param uuid
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	AdministeredVaccine getAdministeredVaccineByUuid(String uuid) throws APIException;
	
	/**
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<AdministeredVaccine> getAllAdministeredVaccines() throws APIException;
	
	/**
	 * @param includeVoided
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<AdministeredVaccine> getAllAdministeredVaccines(boolean includeVoided) throws APIException;
	
	/**
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	Integer getCountOfAllAdministeredVaccines() throws APIException;
	
	/**
	 * @param includeVoided
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	Integer getCountOfAllAdministeredVaccines(boolean includeVoided) throws APIException;
}
