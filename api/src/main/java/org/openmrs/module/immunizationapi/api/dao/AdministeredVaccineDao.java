package org.openmrs.module.immunizationapi.api.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.OpenmrsDataDAO;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.immunizationapi.AdministeredVaccine;
import org.openmrs.module.immunizationapi.VaccineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Willa aka Baba Imu on 2/1/18.
 */
@Repository("immunizationapi.AdministeredVaccineDao")
public class AdministeredVaccineDao implements OpenmrsDataDAO<AdministeredVaccine> {
	
	@Autowired
	private DbSessionFactory sessionFactory;
	
	private DbSession getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public List<AdministeredVaccine> getAll(boolean includeVoided) {
		Criteria criteria = createAllCriteria(includeVoided);
		return (List<AdministeredVaccine>) criteria.uniqueResult();
	}
	
	@Override
	public int getAllCount(boolean includeVoided) {
		Criteria criteria = createAllCriteria(includeVoided);
		return ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	@Override
	public List<AdministeredVaccine> getAll(boolean includeVoided, Integer firstResult, Integer maxResult) {
		Criteria criteria = createAllCriteria(includeVoided);
		return (List<AdministeredVaccine>) criteria.setFirstResult(firstResult).setMaxResults(maxResult).list();
	}
	
	@Override
	public AdministeredVaccine getById(Serializable serializable) {
		Integer id = (Integer) serializable;
		return (AdministeredVaccine) getSession().createCriteria(AdministeredVaccine.class)
		        .add(Restrictions.eq("administeredVaccineId", id)).uniqueResult();
	}
	
	@Override
	public AdministeredVaccine getByUuid(String uuid) {
		return (AdministeredVaccine) getSession().createCriteria(AdministeredVaccine.class)
		        .add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}
	
	@Override
	public void delete(AdministeredVaccine administeredVaccine) {
		getSession().delete(administeredVaccine);
	}
	
	@Override
	public AdministeredVaccine saveOrUpdate(AdministeredVaccine administeredVaccine) {
		getSession().saveOrUpdate(administeredVaccine);
		return administeredVaccine;
	}
	
	private Criteria createAllCriteria(boolean includedVoided) {
		Criteria criteria = getSession().createCriteria(AdministeredVaccine.class, "av");
		
		if (!includedVoided) {
			criteria.add(Restrictions.eq("voided", false));
		}
		
		return criteria;
	}
}
