package org.openmrs.module.immunizationapi;

import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResourceTest;

public class AdministeredVaccineResourceTest extends BaseDelegatingResourceTest<AdministeredVaccineResource, AdministeredVaccine> {
	
	@Override
	public AdministeredVaccine newObject() {
		return null;
	}
	
	@Override
	public String getDisplayProperty() {
		return null;
	}
	
	@Override
	public String getUuidProperty() {
		return null;
	}
	
	@Override
	public void validateDefaultRepresentation() throws Exception {
		super.validateDefaultRepresentation();
		
	}
}
