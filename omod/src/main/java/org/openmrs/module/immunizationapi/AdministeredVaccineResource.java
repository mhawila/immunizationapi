package org.openmrs.module.immunizationapi;

import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.module.immunizationapi.api.ImmunizationAPIService;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.Map;

/**
 * Created by Willa aka Baba Imu on 2/6/18.
 */
@Resource(name = RestConstants.VERSION_1 + "/immunizationapi/administeredvaccine", supportedClass = AdministeredVaccine.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*" })
public class AdministeredVaccineResource extends DataDelegatingCrudResource<AdministeredVaccine> {
	
	private ImmunizationAPIService immunizationAPIService;
	
	public AdministeredVaccineResource() {
		super();
		immunizationAPIService = Context.getService(ImmunizationAPIService.class);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("uuid");
		description.addProperty("display");
		description.addProperty("vaccineConfiguration", Representation.REF);
		description.addProperty("obs", Representation.REF);
		description.addSelfLink();
		
		if (representation instanceof DefaultRepresentation) {
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			return description;
		}
		
		if (representation instanceof FullRepresentation) {
			description.addProperty("auditInfo");
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
	
	@PropertySetter("obs")
	public static void setObs(AdministeredVaccine administeredVaccine, Object value) {
		if (value instanceof String) {
			//It is a uuid
			administeredVaccine.setObs(Context.getObsService().getObsByUuid((String) value));
		} else if (value instanceof Map) {
			Obs convertedObs = (Obs) ConversionUtil.convert(value, Obs.class);
			administeredVaccine.setObs(convertedObs);
		}
	}
	
	@Override
	public AdministeredVaccine getByUniqueId(String uuid) {
		return immunizationAPIService.getAdministeredVaccineByUuid(uuid);
	}
	
	@Override
	protected void delete(AdministeredVaccine administeredVaccine, String reason, RequestContext requestContext)
	        throws ResponseException {
		immunizationAPIService.voidAdministeredVaccine(administeredVaccine, reason);
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
	
	@Override
	public String getResourceVersion() {
		return "2.0";
	}
	
	@Override
	protected String getResourceName() {
		return super.getResourceName();
	}
}
