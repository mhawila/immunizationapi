package org.openmrs.module.immunizationapi.api.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.openmrs.Patient;
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
	
	public List<AdministeredVaccine> getAdministeredVaccinesForPatient(Patient patient,
	        VaccineConfiguration vaccineConfiguration, Integer startIndex, Integer limit, boolean includeVoided) {
		Criteria criteria = createCriteriaForPatient(patient, vaccineConfiguration, startIndex, limit, includeVoided);
		
		return (List<AdministeredVaccine>) criteria.list();
	}
	
	public List<AdministeredVaccine> getAdministeredVaccineForVaccineConfiguration(
	        VaccineConfiguration vaccineConfiguration, Integer startIndex, Integer limit, boolean includeVoided) {
		Criteria criteria = getSession().createCriteria(AdministeredVaccine.class).add(
		    Restrictions.eq("vaccineConfiguration", vaccineConfiguration));
		
		if (!includeVoided) {
			criteria.add(Restrictions.eq("voided", false));
		}
		
		if (startIndex != null) {
			criteria.addOrder(Order.asc("dateCreated"));
			criteria.setFirstResult(startIndex);
		}
		
		if (limit != null) {
			criteria.setMaxResults(limit);
		}
		
		return (List<AdministeredVaccine>) criteria.list();
	}
	
	public int getCountForPatient(Patient patient, VaccineConfiguration vaccineConfiguration, boolean includeVoided) {
		Criteria criteria = createCriteriaForPatient(patient, vaccineConfiguration, null, null, includeVoided);
		
		return ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	private Criteria createCriteriaForPatient(Patient patient, VaccineConfiguration vaccineConfiguration,
	        Integer startIndex, Integer limit, boolean includeVoided) {
		Criteria criteria = getSession().createCriteria(AdministeredVaccine.class)
		        .createAlias("obs", "obs", JoinType.INNER_JOIN).add(Restrictions.eq("obs.person", patient.getPerson()));
		
		if (vaccineConfiguration != null) {
			criteria.add(Restrictions.eq("vaccineConfiguration", vaccineConfiguration));
		}
		
		if (!includeVoided) {
			criteria.add(Restrictions.eq("voided", false));
		}
		
		if (startIndex != null) {
			criteria.addOrder(Order.asc("dateCreated"));
			criteria.setFirstResult(startIndex);
		}
		
		if (limit != null) {
			criteria.setMaxResults(limit);
		}
		
		return criteria;
	}
}
