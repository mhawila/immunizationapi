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

import org.openmrs.Patient;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.immunizationapi.AdministeredVaccine;
import org.openmrs.module.immunizationapi.ImmunizationAPIConfig;
import org.openmrs.module.immunizationapi.SearchMode;
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
	 * @param includeRetired
	 * @param firstResult
	 * @param maxResult
	 * @return
	 * @throws APIException
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<VaccineConfiguration> getAllVaccineConfigurations(boolean includeRetired, Integer firstResult, Integer maxResult)
	        throws APIException;
	
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
	 * This method returns the count of all vaccine configurations that would be returned if an
	 * actual search would have been performed instead. The search is based on the name of the
	 * configuration
	 * 
	 * @param searchText
	 * @param mode Either start, end or anywhere
	 * @param includeRetired
	 * @param firstResut
	 * @param maxResults
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	Integer getCountOfSearchVaccineConfigurations(String searchText, SearchMode mode, boolean includeRetired,
	        Integer firstResut, Integer maxResults) throws APIException;
	
	/**
	 * This method returns the count of all vaccine configurations that would be returned if an
	 * actual search would have been performed instead. The search is based on the name of the
	 * configuration includeRetired = false
	 * 
	 * @param searchText
	 * @param mode Either start, end or anywhere
	 * @param firstResut
	 * @param maxResults
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	Integer getCountOfSearchVaccineConfigurations(String searchText, SearchMode mode, Integer firstResut, Integer maxResults)
	        throws APIException;
	
	/**
	 * This method returns the count of all vaccine configurations that would be returned if an
	 * actual search would have been performed instead. The search is based on the name of the
	 * configuration disregarding paging in this case
	 * 
	 * @param searchText
	 * @param mode Either start, end or anywhere
	 * @param includeRetired
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	Integer getCountOfSearchVaccineConfigurations(String searchText, SearchMode mode, boolean includeRetired)
	        throws APIException;
	
	/**
	 * This method returns the count of all vaccine configurations that would be returned if an
	 * actual search would have been performed instead. The search is based on the name of the
	 * configuration includeRetired = false, no paging involved
	 * 
	 * @param searchText
	 * @param mode Either start, end or anywhere
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	Integer getCountOfSearchVaccineConfigurations(String searchText, SearchMode mode) throws APIException;
	
	/**
	 * This method searches the name of vaccine configurations and returns results.
	 * 
	 * @param searchText
	 * @param mode Either start, end or anywhere
	 * @param includeRetired
	 * @param firstResut
	 * @param maxResults
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<VaccineConfiguration> searchVaccineConfigurations(String searchText, SearchMode mode, boolean includeRetired,
	        Integer firstResut, Integer maxResults) throws APIException;
	
	/**
	 * This method searches the name of vaccine configurations and returns results. includeRetired =
	 * false.
	 * 
	 * @param searchText
	 * @param mode Either start, end or anywhere
	 * @param firstResut
	 * @param maxResults
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<VaccineConfiguration> searchVaccineConfigurations(String searchText, SearchMode mode, Integer firstResut,
	        Integer maxResults) throws APIException;
	
	/**
	 * This method searches the name of vaccine configurations and returns results. includeRetired =
	 * false, searchMode = anywhere
	 * 
	 * @param searchText
	 * @param firstResut
	 * @param maxResults
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<VaccineConfiguration> searchVaccineConfigurations(String searchText, Integer firstResut, Integer maxResults)
	        throws APIException;
	
	/**
	 * This method searches the name of vaccine configurations and returns results.
	 * 
	 * @param searchText
	 * @param mode Either start, end or anywhere
	 * @param includeRetired
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<VaccineConfiguration> searchVaccineConfigurations(String searchText, SearchMode mode, boolean includeRetired)
	        throws APIException;
	
	/**
	 * This method searches the name of vaccine configurations and returns results. includeRetired =
	 * false, searchMode = anywhere
	 * 
	 * @param searchText
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<VaccineConfiguration> searchVaccineConfigurations(String searchText) throws APIException;
	
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
	
	/**
	 * @param patient
	 * @param vaccineConfiguration
	 * @param startIndex
	 * @param limit
	 * @param includeVoided
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<AdministeredVaccine> getAdministeredVaccinesForPatient(Patient patient, VaccineConfiguration vaccineConfiguration,
	        Integer startIndex, Integer limit, boolean includeVoided);
	
	/**
	 * @param patient
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<AdministeredVaccine> getAdministeredVaccinesForPatient(Patient patient);
	
	/**
	 * @param patient
	 * @param vaccineConfiguration
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<AdministeredVaccine> getAdministeredVaccinesForPatient(Patient patient, VaccineConfiguration vaccineConfiguration);
	
	/**
	 * @param patient
	 * @param startIndex
	 * @param limit
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<AdministeredVaccine> getAdministeredVaccinesForPatient(Patient patient, Integer startIndex, Integer limit);
	
	/**
	 * @param patient
	 * @param startIndex
	 * @param limit
	 * @param includeVoided
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<AdministeredVaccine> getAdministeredVaccinesForPatient(Patient patient, Integer startIndex, Integer limit,
	        boolean includeVoided);
	
	/**
	 * @param patient
	 * @param includeVoided
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<AdministeredVaccine> getAdministeredVaccinesForPatient(Patient patient, boolean includeVoided);
	
	/**
	 * @param vaccineConfiguration
	 * @param startIndex
	 * @param limit
	 * @param includeVoided
	 * @return
	 */
	@Authorized(ImmunizationAPIConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	List<AdministeredVaccine> getAdministeredVaccinesForVaccineConfiguration(VaccineConfiguration vaccineConfiguration,
	        Integer startIndex, Integer limit, boolean includeVoided);
}
