package org.openmrs.module.immunizationapi;

import org.junit.Assert;
import org.junit.Before;
import org.openmrs.module.immunizationapi.api.ImmunizationAPIService;
import org.openmrs.module.webservices.rest.web.Hyperlink;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResourceTest;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static org.openmrs.module.immunizationapi.RestTestConstants.EXISTING_VACCINE_CONFIGURATION_UUID;
import static org.openmrs.module.immunizationapi.RestTestConstants.TEST_DATASET_FILENAME;

/**
 * Created by Willa aka Baba Imu on 2/8/18.
 */
public class TimeIntervalSubResourceTest extends BaseDelegatingResourceTest<TimeIntervalSubResource, Interval> {
	
	private static final Double TIME_VALUE = 10.0;
	
	private static final TimeUnit unit = TimeUnit.YEARS;
	
	private static Interval intervalInstance = null;
	
	@Qualifier("immunizationapi.ImmunizationAPIService")
	@Autowired
	private ImmunizationAPIService immunizationAPIService;
	
	static {
		intervalInstance = new Interval(new TimeValue(TIME_VALUE, unit), 1, 2);
	}
	
	@Before
	public void setup() throws Exception {
		executeXmlDataSet(TEST_DATASET_FILENAME);
		
		VaccineConfiguration vc = immunizationAPIService.getVaccineConfigurationByUuid(EXISTING_VACCINE_CONFIGURATION_UUID);
		intervalInstance.setVaccineConfiguration(vc);
	}
	
	@Override
	public Interval newObject() {
		return intervalInstance;
	}
	
	@Override
	public String getDisplayProperty() {
		return intervalInstance.toString();
	}
	
	@Override
	public String getUuidProperty() {
		throw new ResourceDoesNotSupportOperationException("The TimeInterval sub-resource does not have UUID property ");
	}
	
	@Override
    public void validateRefRepresentation() throws Exception {
        assertPropNotPresent("uuid");
        assertPropPresent("display");
        assertPropEquals("display", getDisplayProperty());
        assertPropPresent("links");

        List<Hyperlink> links = (List<Hyperlink>) getRepresentation().get("links");

        Assert.assertTrue(links.stream().anyMatch(link -> link.getRel().equals("self")));

        links.stream().filter(link -> link.getRel().equals("self")).map(link -> link.getUri()).findFirst()
                .ifPresent(Assert::assertNotNull);
    }
	
	@Override
	public void validateDefaultRepresentation() throws Exception {
		validateRefRepresentation();
		assertPropEquals("rank1", intervalInstance.getRank1());
		assertPropEquals("rank2", intervalInstance.getRank2());
		assertPropEquals("timeUnit", intervalInstance.getValue().getUnit().name());
		assertPropEquals("timeValue", intervalInstance.getValue().getValue());
	}
	
	@Override
	public void validateFullRepresentation() throws Exception {
		validateDefaultRepresentation();
		assertPropPresent("auditInfo");
	}
}
