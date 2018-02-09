package org.openmrs.module.immunizationapi;

import org.openmrs.module.immunizationapi.api.ImmunizationAPIService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.MetadataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Willa aka Baba Imu on 2/7/18.
 */
@Resource(name = RestConstants.VERSION_1 + "/immunizationapi/vaccineconfiguration", supportedClass = VaccineConfiguration.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*" })
public class VaccineConfigurationResource extends MetadataDelegatingCrudResource<VaccineConfiguration> {
	
	@Autowired
	private ImmunizationAPIService immunizationAPIService;
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
		if (representation instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("display");
			description.addProperty("concept", Representation.REF);
			description.addProperty("intervals", Representation.REF);
			description.addProperty("numberOfTimes");
			description.addSelfLink();
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			return description;
		} else if (representation instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("display");
			description.addProperty("concept", Representation.REF);
			description.addProperty("intervals", Representation.REF);
			description.addProperty("numberOfTimes");
			description.addProperty("auditInfo");
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	/**
	 * @param vaccineConfiguration
	 * @return vaccine name
	 */
	@PropertyGetter("display")
	public String getDisplayString(VaccineConfiguration vaccineConfiguration) {
		return vaccineConfiguration.getName();
	}
	
	@Override
	public VaccineConfiguration getByUniqueId(String uuid) {
		return immunizationAPIService.getVaccineConfigurationByUuid(uuid);
	}
	
	@Override
	public VaccineConfiguration newDelegate() {
		return new VaccineConfiguration();
	}
	
	@Override
	public VaccineConfiguration save(VaccineConfiguration vaccineConfiguration) {
		return immunizationAPIService.saveVaccineConfiguration(vaccineConfiguration);
	}
	
	@Override
	public void purge(VaccineConfiguration vaccineConfiguration, RequestContext requestContext) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException("Sorry! Purging not allowed for now");
	}
}
