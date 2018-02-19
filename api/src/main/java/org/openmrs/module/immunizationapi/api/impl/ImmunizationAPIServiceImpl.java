/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.immunizationapi.api.impl;

import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.immunizationapi.AdministeredVaccine;
import org.openmrs.module.immunizationapi.VaccineConfiguration;
import org.openmrs.module.immunizationapi.api.ImmunizationAPIService;
import org.openmrs.module.immunizationapi.api.dao.AdministeredVaccineDao;
import org.openmrs.module.immunizationapi.api.dao.VaccineConfigurationDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ImmunizationAPIServiceImpl extends BaseOpenmrsService implements ImmunizationAPIService {
	
	@Autowired
	private VaccineConfigurationDao vaccineConfigurationDao;
	
	@Autowired
	private AdministeredVaccineDao administeredVaccineDao;
	
	UserService userService;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public VaccineConfiguration saveVaccineConfiguration(VaccineConfiguration vaccineConfiguration) throws APIException {
		return vaccineConfigurationDao.saveOrUpdate(vaccineConfiguration);
	}
	
	@Override
	public VaccineConfiguration retireVaccineConfiguration(VaccineConfiguration vaccineConfiguration, String reason)
	        throws APIException {
		return vaccineConfigurationDao.saveOrUpdate(vaccineConfiguration);
	}
	
	@Override
	public VaccineConfiguration unretireVaccineConfiguration(VaccineConfiguration vaccineConfiguration) throws APIException {
		return vaccineConfigurationDao.saveOrUpdate(vaccineConfiguration);
	}
	
	@Override
	public void purgeVaccineConfiguration(VaccineConfiguration vaccineConfiguration) throws APIException {
		vaccineConfigurationDao.delete(vaccineConfiguration);
	}
	
	@Override
	public VaccineConfiguration getVaccineConfigurationById(Integer id) throws APIException {
		return vaccineConfigurationDao.getById(id);
	}
	
	@Override
	public VaccineConfiguration getVaccineConfigurationByUuid(String uuid) throws APIException {
		return vaccineConfigurationDao.getByUuid(uuid);
	}
	
	@Override
	public List<VaccineConfiguration> getAllVaccineConfigurations() throws APIException {
		return vaccineConfigurationDao.getAll(false);
	}
	
	@Override
	public List<VaccineConfiguration> getAllVaccineConfigurations(boolean includeRetired) throws APIException {
		return vaccineConfigurationDao.getAll(includeRetired);
	}
	
	@Override
	public Integer getCountOfAllVaccineConfigurations() throws APIException {
		return vaccineConfigurationDao.getAllCount(false);
	}
	
	@Override
	public Integer getCountOfAllVaccineConfigurations(boolean includeRetired) throws APIException {
		return vaccineConfigurationDao.getAllCount(includeRetired);
	}
	
	/***** AdministeredVaccine related stuff ***/
	@Override
	public AdministeredVaccine saveAdministeredVaccine(AdministeredVaccine administeredVaccine) throws APIException {
		return administeredVaccineDao.saveOrUpdate(administeredVaccine);
	}
	
	@Override
	public AdministeredVaccine voidAdministeredVaccine(AdministeredVaccine administeredVaccine, String reason)
	        throws APIException {
		Context.getObsService().voidObs(administeredVaccine.getObs(), reason);
		return administeredVaccineDao.saveOrUpdate(administeredVaccine);
	}
	
	@Override
	public AdministeredVaccine unVoidAdministeredVaccine(AdministeredVaccine administeredVaccine) throws APIException {
		//Void associated obs.
		Context.getObsService().unvoidObs(administeredVaccine.getObs());
		return administeredVaccineDao.saveOrUpdate(administeredVaccine);
	}
	
	@Override
	public void purgeAdministeredVaccine(AdministeredVaccine administeredVaccine) throws APIException {
		administeredVaccineDao.delete(administeredVaccine);
	}
	
	@Override
	public AdministeredVaccine getAdministeredVaccineById(Integer id) throws APIException {
		return administeredVaccineDao.getById(id);
	}
	
	@Override
	public AdministeredVaccine getAdministeredVaccineByUuid(String uuid) throws APIException {
		return administeredVaccineDao.getByUuid(uuid);
	}
	
	@Override
	public List<AdministeredVaccine> getAllAdministeredVaccines() throws APIException {
		return administeredVaccineDao.getAll(false);
	}
	
	@Override
	public List<AdministeredVaccine> getAllAdministeredVaccines(boolean includeVoided) throws APIException {
		return administeredVaccineDao.getAll(includeVoided);
	}
	
	@Override
	public Integer getCountOfAllAdministeredVaccines() throws APIException {
		return administeredVaccineDao.getAllCount(false);
	}
	
	@Override
	public Integer getCountOfAllAdministeredVaccines(boolean includeVoided) throws APIException {
		return administeredVaccineDao.getAllCount(includeVoided);
	}
	
	@Override
	public List<AdministeredVaccine> getAdministeredVaccinesForPatient(Patient patient,
	        VaccineConfiguration vaccineConfiguration, Integer startIndex, Integer limit, boolean includeVoided) {
		return administeredVaccineDao.getAdministeredVaccinesForPatient(patient, vaccineConfiguration, startIndex, limit,
		    includeVoided);
	}
	
	@Override
	public List<AdministeredVaccine> getAdministeredVaccinesForPatient(Patient patient) {
		return administeredVaccineDao.getAdministeredVaccinesForPatient(patient, null, null, null, false);
	}
	
	@Override
	public List<AdministeredVaccine> getAdministeredVaccinesForPatient(Patient patient,
	        VaccineConfiguration vaccineConfiguration) {
		return administeredVaccineDao.getAdministeredVaccinesForPatient(patient, vaccineConfiguration, null, null, false);
	}
	
	@Override
	public List<AdministeredVaccine> getAdministeredVaccinesForPatient(Patient patient, Integer startIndex, Integer limit) {
		return administeredVaccineDao.getAdministeredVaccinesForPatient(patient, null, startIndex, limit, false);
	}
	
	@Override
	public List<AdministeredVaccine> getAdministeredVaccinesForPatient(Patient patient, Integer startIndex, Integer limit,
	        boolean includeVoided) {
		return administeredVaccineDao.getAdministeredVaccinesForPatient(patient, null, startIndex, limit, includeVoided);
	}
	
	@Override
	public List<AdministeredVaccine> getAdministeredVaccinesForPatient(Patient patient, boolean includeVoided) {
		return administeredVaccineDao.getAdministeredVaccinesForPatient(patient, null, null, null, includeVoided);
	}
}
