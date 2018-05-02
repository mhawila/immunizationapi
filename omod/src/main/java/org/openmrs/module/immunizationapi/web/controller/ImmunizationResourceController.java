package org.openmrs.module.immunizationapi.web.controller;

import org.openmrs.Patient;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PatientService;
import org.openmrs.module.immunizationapi.AdministeredVaccine;
import org.openmrs.module.immunizationapi.AdministeredVaccineResource;
import org.openmrs.module.immunizationapi.ImmunizationAPIConstants;
import org.openmrs.module.immunizationapi.VaccineConfiguration;
import org.openmrs.module.immunizationapi.api.ImmunizationAPIService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.RestUtil;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by Willa aka Baba Imu on 2/13/18.
 */
@RestController
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/immunizationapi")
public class ImmunizationResourceController extends MainResourceController {
	
	@Autowired
	private ImmunizationAPIService immunizationAPIService;
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	@Qualifier("adminService")
	private AdministrationService adminService;
	
	public ImmunizationResourceController() {
		super();
	}
	
	@Override
	@RequestMapping(value = "/{resource}/{uuid}", method = RequestMethod.GET)
	public Object retrieve(@PathVariable("resource") String resource, @PathVariable("uuid") String uuid,
	        HttpServletRequest request, HttpServletResponse response) throws ResponseException {
		return super.retrieve(resource, uuid, request, response);
	}
	
	@Override
	@RequestMapping(value = "/{resource}", method = RequestMethod.POST)
	public Object create(@PathVariable("resource") String resource, @RequestBody SimpleObject post,
	        HttpServletRequest request, HttpServletResponse response) throws ResponseException {
		return super.create(resource, post, request, response);
	}
	
	@Override
	@RequestMapping(value = "/{resource}/{uuid}", method = RequestMethod.POST)
	public Object update(@PathVariable("resource") String resource, @PathVariable("uuid") String uuid,
	        @RequestBody SimpleObject post, HttpServletRequest request, HttpServletResponse response)
	        throws ResponseException {
		return super.update(resource, uuid, post, request, response);
	}
	
	@Override
	@RequestMapping(value = "/{resource}/{uuid}", method = RequestMethod.DELETE, params = "!purge")
	public Object delete(@PathVariable("resource") String resource, @PathVariable("uuid") String uuid,
	        @RequestParam(value = "reason", defaultValue = "web service call") String reason, HttpServletRequest request,
	        HttpServletResponse response) throws ResponseException {
		return super.delete(resource, uuid, reason, request, response);
	}
	
	@Override
	@RequestMapping(value = "/{resource}/{uuid}", method = RequestMethod.DELETE, params = "purge")
	public Object purge(@PathVariable("resource") String resource, @PathVariable("uuid") String uuid,
	        HttpServletRequest request, HttpServletResponse response) throws ResponseException {
		return super.purge(resource, uuid, request, response);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{resource}", method = RequestMethod.GET)
	public SimpleObject get(@PathVariable("resource") String resource, HttpServletRequest request,
	        HttpServletResponse response) throws ResponseException {
		return super.get(resource, request, response);
	}
	
	@RequestMapping(value = "/administeredvaccine", method = RequestMethod.GET)
	@ResponseBody
	public Object searchAdministeredVaccines(@RequestParam(value = "patient", required = false) String patientUuid,
	        @RequestParam(value = "vaccineConfiguration", required = false) String vaccineConfigurationUuid,
			HttpServletRequest request,
	        HttpServletResponse response) throws ResponseException {

		RequestContext context = RestUtil.getRequestContext(request, response);
		Patient patient = null;
		VaccineConfiguration configuration = null;
		AdministeredVaccineResource resource = new AdministeredVaccineResource();

		
		if (isNotBlank(patientUuid)) {
			patient = patientService.getPatientByUuid(patientUuid);
		}
		
		if (isNotBlank(vaccineConfigurationUuid)) {
			configuration = immunizationAPIService.getVaccineConfigurationByUuid(vaccineConfigurationUuid);
		}

		if(patient != null  || configuration != null) {
			List<AdministeredVaccine> administeredVaccines = null;
			if (patient != null) {
				administeredVaccines = immunizationAPIService
						.getAdministeredVaccinesForPatient(patient, configuration, context.getStartIndex(),
								context.getLimit(),context.getIncludeAll());
			}
			else if (configuration != null) {
				administeredVaccines = immunizationAPIService
						.getAdministeredVaccinesForVaccineConfiguration(configuration, context.getStartIndex(),
								context.getLimit(), context.getIncludeAll());
			}

			return new SimpleObject().add("results", administeredVaccines.stream()
					.map(administeredVaccine -> resource.asRepresentation(administeredVaccine, context.getRepresentation()))
					.collect(Collectors.toList()));
		}
		
		throw new ResourceDoesNotSupportOperationException("Either patient or configuration uuid have to be passed for"
		        + "search to be conducted");
	}
	
	/**
	 * This controller method fetches and returns the value of the searched global property value.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/globalproperty", method = RequestMethod.GET)
	@ResponseBody
	public SimpleObject getImmunizationGlobalPropertyValue(@RequestParam(value = "property") String property,
	        HttpServletRequest request, HttpServletResponse response) throws ResponseException {
		String propertyValue = adminService.getGlobalProperty(property);
		if(propertyValue == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return new SimpleObject().add("value", propertyValue);
	}
	
	@Override
	public String getNamespace() {
		return super.getNamespace() + "/immunizationapi";
	}
}
