package org.openmrs.module.immunizationapi;

import org.openmrs.module.immunizationapi.api.ImmunizationAPIService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Willa aka Baba Imu on 2/6/18.
 */
@Resource(name = RestConstants.VERSION_1 + "/immunizationapi/administeredvaccine", supportedClass = AdministeredVaccine.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*" })
public class AdministeredVaccineResource extends DataDelegatingCrudResource<AdministeredVaccine> {
	
	@Autowired
	private ImmunizationAPIService immunizationAPIService;

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
		if (representation instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("display");
			description.addProperty("vaccineConfiguration", Representation.REF);
			description.addProperty("obs", Representation.REF);
			description.addSelfLink();
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			return description;
		} else if (representation instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("display");
			description.addProperty("vaccineConfiguration", Representation.REF);
			description.addProperty("obs", Representation.REF);
			description.addProperty("auditInfo");
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	/**
	 * @param administeredVaccine
	 * @return vaccine name
	 */
	@PropertyGetter("display")
	public String getDisplayString(AdministeredVaccine administeredVaccine) {
		return administeredVaccine.getVaccineConfiguration().getName();
	}
	
	@Override
	public AdministeredVaccine getByUniqueId(String uuid) {
		return immunizationAPIService.getAdministeredVaccineByUuid(uuid);
	}
	
	@Override
	protected void delete(AdministeredVaccine administeredVaccine, String reason, RequestContext requestContext)
	        throws ResponseException {
		immunizationAPIService.retireAdministeredVaccine(administeredVaccine, reason);
	}
	
	@Override
	public AdministeredVaccine newDelegate() {
		return new AdministeredVaccine();
	}
	
	@Override
	public AdministeredVaccine save(AdministeredVaccine administeredVaccine) {
		return immunizationAPIService.saveAdministeredVaccine(administeredVaccine);
	}
	
	@Override
	public void purge(AdministeredVaccine administeredVaccine, RequestContext requestContext) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException("Sorry! Purging not allowed via REST");
	}
}
