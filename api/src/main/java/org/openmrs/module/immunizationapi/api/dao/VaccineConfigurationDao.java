/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.immunizationapi.api.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.OpenmrsMetadataDAO;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.immunizationapi.VaccineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository("immunizationapi.VaccineConfigurationDao")
public class VaccineConfigurationDao implements OpenmrsMetadataDAO<VaccineConfiguration> {
	
	@Autowired
	DbSessionFactory sessionFactory;
	
	private DbSession getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public List<VaccineConfiguration> getAll(boolean includeRetired) {
		Criteria criteria = createAllCriteria(includeRetired);
		return (List<VaccineConfiguration>) criteria.uniqueResult();
	}
	
	@Override
	public int getAllCount(boolean includeRetired) {
		Criteria criteria = createAllCriteria(includeRetired);
		return ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	@Override
	public List<VaccineConfiguration> getAll(boolean includeRetired, Integer firstResult, Integer maxResult) {
		Criteria criteria = createAllCriteria(includeRetired);
		return (List<VaccineConfiguration>) criteria.setFirstResult(firstResult).setMaxResults(maxResult).list();
	}
	
	@Override
	public VaccineConfiguration getById(Serializable serializable) {
		Integer id = (Integer) serializable;
		return (VaccineConfiguration) getSession().createCriteria(VaccineConfiguration.class)
		        .add(Restrictions.eq("vaccinationConfigurationId", id)).uniqueResult();
	}
	
	@Override
	public VaccineConfiguration getByUuid(String uuid) {
		return (VaccineConfiguration) getSession().createCriteria(VaccineConfiguration.class)
		        .add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}
	
	@Override
	public void delete(VaccineConfiguration vaccineConfiguration) {
		getSession().delete(vaccineConfiguration);
	}
	
	@Override
	public VaccineConfiguration saveOrUpdate(VaccineConfiguration vaccineConfiguration) {
		getSession().saveOrUpdate(vaccineConfiguration);
		return vaccineConfiguration;
	}
	
	private Criteria createAllCriteria(boolean includeRetired) {
		Criteria criteria = getSession().createCriteria(VaccineConfiguration.class, "vc");
		
		if (!includeRetired) {
			criteria.add(Restrictions.eq("retired", false));
		}
		
		return criteria;
	}
}
