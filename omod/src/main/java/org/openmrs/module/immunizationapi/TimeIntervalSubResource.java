package org.openmrs.module.immunizationapi;

import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * Created by Willa aka Baba Imu on 2/7/18.
 */
@SubResource(path = "interval", parent = VaccineConfigurationResource.class, supportedClass = Interval.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*" })
public class TimeIntervalSubResource extends DelegatingSubResource<Interval, VaccineConfiguration, VaccineConfigurationResource> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("display");
		description.addSelfLink();
		
		if (representation instanceof RefRepresentation) {
			return description;
		}
		
		description.addProperty("rank1");
		description.addProperty("rank2");
		description.addProperty("timeUnit");
		description.addProperty("timeValue");
		
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
	 * @param interval
	 * @return vaccine name
	 */
	@PropertyGetter("display")
	public String getDisplayString(Interval interval) {
		return interval.toString();
	}
	
	/**
	 * @param interval
	 * @return
	 */
	@PropertyGetter("timeUnit")
	public String getTimeUnit(Interval interval) {
		return interval.getValue().getUnit().name();
	}
	
	@PropertyGetter("timeValue")
	public double getTimeValue(Interval interval) {
		return interval.getValue().getValue();
	}
	
	@Override
	public VaccineConfiguration getParent(Interval interval) {
		return interval.getVaccineConfiguration();
	}
	
	@Override
	public void setParent(Interval interval, VaccineConfiguration vaccineConfiguration) {
		throw new ResourceDoesNotSupportOperationException("Can not set parent");
	}
	
	@Override
	public PageableResult doGetAll(VaccineConfiguration vaccineConfiguration, RequestContext requestContext)
	        throws ResponseException {
		return new NeedsPaging<Interval>(vaccineConfiguration.getIntervals(), requestContext);
	}
	
	@Override
	public Interval getByUniqueId(String s) {
		throw new ResourceDoesNotSupportOperationException("Cannot fetch by unique ID");
	}
	
	@Override
	protected void delete(Interval interval, String s, RequestContext requestContext) throws ResponseException {
		
	}
	
	@Override
	public Interval newDelegate() {
		return new Interval();
	}
	
	@Override
	public Interval save(Interval interval) {
		return null;
	}
	
	@Override
	public void purge(Interval interval, RequestContext requestContext) throws ResponseException {
		
	}
	
	@Override
	protected String getUniqueId(Interval delegate) {
		VaccineConfiguration vc = delegate.getVaccineConfiguration();
		
		if (vc != null) {
			return vc.getUuid();
		}
		return null;
	}
}
