package org.openmrs.module.immunizationapi;

import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * Created by Willa aka Baba Imu on 2/6/18.
 */
@Resource(name = RestConstants.VERSION_1 + "/administeredvaccine", supportedClass = AdministeredVaccine.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*" })
public class AdministeredVaccineResource extends DataDelegatingCrudResource<AdministeredVaccine> {
	
	@Override
	public AdministeredVaccine getByUniqueId(String s) {
		return null;
	}
	
	@Override
	protected void delete(AdministeredVaccine administeredVaccine, String s, RequestContext requestContext)
	        throws ResponseException {
		
	}
	
	@Override
	public AdministeredVaccine newDelegate() {
		return null;
	}
	
	@Override
	public AdministeredVaccine save(AdministeredVaccine administeredVaccine) {
		return null;
	}
	
	@Override
	public void purge(AdministeredVaccine administeredVaccine, RequestContext requestContext) throws ResponseException {
		
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
		if (representation instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("administeredVaccineId");
			description.addProperty("uuid");
			description.addProperty("display");
			description.addProperty("vaccineConfiguration", Representation.REF);
			description.addProperty("rank");
			description.addProperty("obs", Representation.REF);
			description.addSelfLink();
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			return description;
		} else if (representation instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("administeredVaccineId");
			description.addProperty("uuid");
			description.addProperty("display");
			description.addProperty("vaccineConfiguration", Representation.REF);
			description.addProperty("rank");
			description.addProperty("obs", Representation.REF);
			description.addProperty("auditInfo");
			description.addSelfLink();
			return description;
		}
		return null;
	}
}
