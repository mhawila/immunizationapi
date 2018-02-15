package org.openmrs.module.immunizationapi.web.controller;

import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Willa aka Baba Imu on 2/13/18.
 */
@RestController
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/immunizationapi")
public class ImmunizationResourceController extends MainResourceController {
	
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
	
	@Override
	public String getNamespace() {
		return super.getNamespace() + "/immunizationapi";
	}
}
