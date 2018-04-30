package org.openmrs.module.immunizationapi;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openmrs.module.immunizationapi.api.ImmunizationAPIService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VaccineConfigurationResourceUnitTest {
	
	@Mock
	private ImmunizationAPIService immunizationAPIService;
	
	@InjectMocks
	private VaccineConfigurationResource vcResource;
	
	@Before
	public void setup() throws Exception {
		vcResource = new VaccineConfigurationResource();
	}
	
	@Test
    @Ignore
    public void search_shouldSearch() {
        String searchText = "polio";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", searchText);
        RequestContext context = new RequestContext();
        context.setRequest(request);

        List<VaccineConfiguration> toRet = new ArrayList<>();
        toRet.add(new VaccineConfiguration());
        Mockito.when(immunizationAPIService.searchVaccineConfigurations(searchText)).thenReturn(toRet);

        SimpleObject response = vcResource.search(context);

        assertTrue(response.get("results") instanceof List);
        assertEquals(1, ((List<SimpleObject>) response.get("results")).size());
    }
}
