package org.openmrs.module.immunizationapi;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.immunizationapi.api.ImmunizationAPIService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.MetadataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Willa aka Baba Imu on 2/7/18.
 */
@Resource(name = RestConstants.VERSION_1 + "/immunizationapi/vaccineconfiguration", supportedClass = VaccineConfiguration.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*" })
public class VaccineConfigurationResource extends MetadataDelegatingCrudResource<VaccineConfiguration> {
	
	private ImmunizationAPIService immunizationAPIService;
	
	public VaccineConfigurationResource() {
		super();
		immunizationAPIService = Context.getService(ImmunizationAPIService.class);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		
		if (representation instanceof DefaultRepresentation || representation instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("display");
			description.addProperty("name");
			description.addSelfLink();
		}
		
		if (representation instanceof DefaultRepresentation) {
			description.addProperty("concept", Representation.REF);
			description.addProperty("intervals");
			description.addProperty("numberOfTimes");
			description.addProperty("ageFirstTimeRequired");
			description.addProperty("ageUnit");
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			return description;
		}
		
		if (representation instanceof FullRepresentation) {
			description.addProperty("concept", Representation.DEFAULT);
			description.addProperty("intervals");
			description.addProperty("numberOfTimes");
			description.addProperty("description");
			description.addProperty("ageFirstTimeRequired");
			description.addProperty("ageUnit");
			description.addProperty("auditInfo");
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
	
	@PropertyGetter("ageUnit")
	public static String getAgeUnit(VaccineConfiguration vaccineConfiguration) {
		TimeUnit ageUnit = vaccineConfiguration.getAgeUnit();
		return ageUnit == null ? "" : ageUnit.name();
	}
	
	@PropertySetter("ageUnit")
	public static void setAgeUnit(VaccineConfiguration delegate, Object value) {
		delegate.setAgeUnit(TimeUnit.valueOf(((String) value).toUpperCase()));
	}
	
	/**
	 * @param vaccineConfiguration
	 * @return
	 */
	@PropertyGetter("intervals")
	public static List<SimpleObject> getIntervals(VaccineConfiguration vaccineConfiguration) {
		return vaccineConfiguration.getIntervals().stream().map(interval -> {
			return new SimpleObject()
					.add("rank1", interval.getRank1())
					.add("rank2",interval.getRank2())
					.add("timeUnit", interval.getValue().getUnit().name())
					.add("timeValue", interval.getValue().getValue());
		}).collect(Collectors.toList());
	}
	
	/**
	 * Annotated setter for Concept
	 * 
	 * @param vaccineConfiguration
	 * @param value
	 */
	@PropertySetter("concept")
	public static void setConcept(VaccineConfiguration vaccineConfiguration, Object value) {
		vaccineConfiguration.setConcept(Context.getConceptService().getConceptByUuid((String) value));
	}
	
	@PropertySetter("intervals")
	public static void setIntervals(VaccineConfiguration vaccineConfiguration, Object value) {
		List<LinkedHashMap> intervals = (List<LinkedHashMap>) value;
		// Create Interval instances
		List<Interval> instances = intervals.stream().map( i -> {
			TimeUnit timeUnit = TimeUnit.valueOf(((String)i.get("timeUnit")).toUpperCase());
			Double timeValue = ((Number)i.get("timeValue")).doubleValue();
			return new Interval(new TimeValue(timeValue, timeUnit), (Integer)i.get("rank1"), (Integer)i.get("rank2"));
		}).collect(Collectors.toList());

		vaccineConfiguration.setIntervals(instances);
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
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		Integer startIndex = null;
		Integer limit = null;
		boolean includeRetired = Boolean.parseBoolean(context.getParameter("includeAll"));

		try {
			startIndex = Integer.valueOf(context.getParameter("startIndex"));
		}
		catch (NumberFormatException e) {
			// Ignore
		}

		try {
			limit = Integer.valueOf(context.getParameter("limit"));
		}
		catch (NumberFormatException e) {
			// Ignore
		}

		List<VaccineConfiguration> vaccineConfigurations = immunizationAPIService.getAllVaccineConfigurations(includeRetired, startIndex, limit);
		return new NeedsPaging<>(vaccineConfigurations, context);
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = super.getCreatableProperties();
		
		description.addProperty("intervals");
		description.addRequiredProperty("concept");
		description.addProperty("numberOfTimes");
		description.addProperty("ageFirstTimeRequired");
		description.addProperty("ageUnit");
		
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = super.getUpdatableProperties();
		description.addProperty("retired");
		description.addProperty("retireReason");
		
		return description;
	}
	
	@Override
	public Object update(String uuid, SimpleObject propertiesToUpdate, RequestContext context) throws ResponseException {
		if (propertiesToUpdate.containsKey("retired")) {
			String retireReason = "Webservices post call with body having retired property set to true";
			if (propertiesToUpdate.containsKey("retireReason")) {
				retireReason = propertiesToUpdate.get("retireReason");
			}
			
			VaccineConfiguration configuration = immunizationAPIService.getVaccineConfigurationByUuid(uuid);
			Object retiredProperty = propertiesToUpdate.get("retired");
			Class retiredClass = retiredProperty.getClass();
			Boolean retiredValue = null;
			if (retiredClass == Boolean.class) {
				retiredValue = (Boolean) retiredProperty;
			} else if (retiredClass == String.class) {
				retiredValue = Boolean.valueOf((String) retiredProperty);
			} else {
				throw new APIException("Wrong value format passed for retired property" + retiredClass);
			}
			
			if (retiredValue) {
				delete(configuration, retireReason, context);
			} else {
				// Unretire because it is false. (I don't like this below, it is basically removing all auditing info!
				configuration.setRetired(false);
				configuration.setDateRetired(null);
				configuration.setRetiredBy(null);
				configuration.setRetireReason(null);
				configuration = immunizationAPIService.saveVaccineConfiguration(configuration);
			}
			return configuration;
		} else {
			return super.update(uuid, propertiesToUpdate, context);
		}
	}
	
	@Override
	public SimpleObject search(RequestContext context) throws ResponseException {
		// Allowed parameter to search (for allow name and concept)
		Set<String> params = context.getRequest().getParameterMap().keySet();
		for (String param : params) {
			if (param.equalsIgnoreCase("concept")) {
				throw new ResourceDoesNotSupportOperationException(
				        "passing concept not yet support, stay tuned, it will be in the future");
			}
			if (param.equalsIgnoreCase("name")) {
				String searchText = context.getParameter("name");
				Integer startIndex = null;
				Integer limit = null;
				boolean includeRetired = Boolean.parseBoolean(context.getParameter("includeAll"));
				try {
					startIndex = Integer.valueOf(context.getParameter("startIndex"));
				}
				catch (NumberFormatException e) {
					// Ignore
				}
				
				try {
					limit = Integer.valueOf(context.getParameter("limit"));
				}
				catch (NumberFormatException e) {
					// Ignore
				}
				
				String passedSearchMode = context.getParameter("searchMode");
				SearchMode mode = SearchMode.ANYWHERE;
				if (StringUtils.isNotBlank(passedSearchMode)) {
					try {
						mode = SearchMode.valueOf(passedSearchMode);
					}
					catch (IllegalArgumentException iae) {
						log.debug("Passed illegal value " + passedSearchMode + " for search mode");
					}
				}
				List<VaccineConfiguration> found = immunizationAPIService.searchVaccineConfigurations(searchText, mode, includeRetired, startIndex, limit);
				return new NeedsPaging<>(found, context).toSimpleObject(this);
			}
		}
		return super.search(context);
	}
	
	@Override
	public String getResourceVersion() {
		return "2.0";
	}
}
