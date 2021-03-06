package org.openmrs.module.immunizationapi;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.module.immunizationapi.api.ImmunizationAPIService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResourceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.openmrs.module.immunizationapi.RestTestConstants.EXISTING_CONCEPT_UUID;
import static org.openmrs.module.immunizationapi.RestTestConstants.EXISTING_VACCINE_CONFIGURATION_UUID;
import static org.openmrs.module.immunizationapi.RestTestConstants.TEST_DATASET_FILENAME;
import static org.openmrs.module.immunizationapi.RestTestConstants.VACCINE_CONFIGURATION_UUID;
import static org.openmrs.module.immunizationapi.RestTestConstants.VACCINE_NAME;

/**
 * Created by Willa aka Baba Imu on 2/7/18.
 */
public class VaccineConfigurationResourceTest extends BaseDelegatingResourceTest<VaccineConfigurationResource, VaccineConfiguration> {
	
	@Qualifier("immunizationapi.ImmunizationAPIService")
	@Autowired
	private ImmunizationAPIService immunizationAPIService;
	
	private VaccineConfigurationResource vcResource;
	
	@Before
	public void setup() throws Exception {
		executeDataSet(TEST_DATASET_FILENAME);
		vcResource = new VaccineConfigurationResource();
	}
	
	@Override
	public VaccineConfiguration newObject() {
		VaccineConfiguration vc = new VaccineConfiguration(VACCINE_NAME, new Concept(1000), 3);
		vc.setUuid(VACCINE_CONFIGURATION_UUID);
		return vc;
	}
	
	@Override
	public String getDisplayProperty() {
		return VACCINE_NAME;
	}
	
	@Override
	public String getUuidProperty() {
		return VACCINE_CONFIGURATION_UUID;
	}
	
	@Test
	public void shouldConvertANewVaccineConfiguration() {
		SimpleObject simpleObject = createSimpleVaccineConfiguration();
		
		VaccineConfiguration converted = vcResource.convert(simpleObject);
		
		assertNotNull(converted);
		assertEquals("Name is not set correctly", VACCINE_NAME, converted.getName());
		assertEquals("The concept is not set", EXISTING_CONCEPT_UUID, converted.getConcept().getUuid());
	}
	
	@Test
	public void shouldConvertANewVaccineConfigurationWithIntervals() {
		SimpleObject toPost = createSimpleVaccineConfiguration();
		List<SimpleObject> intervals = new ArrayList<>();
		intervals.add(new SimpleObject().add("timeUnit", "MONTHS").add("timeValue", 10).add("rank1", 1).add("rank2", 2));
		toPost.add("intervals", intervals);
		VaccineConfiguration converted = vcResource.convert(toPost);

		assertNotNull(converted);
		assertTrue("interval should have one item", converted.getIntervals().size() == 1);
	}
	
	@Test
	public  void shouldUpdateWithNewProvidedInterval() {
		VaccineConfiguration vaccineConfiguration =
				immunizationAPIService.getVaccineConfigurationByUuid(EXISTING_VACCINE_CONFIGURATION_UUID);
		assertEquals("rank1 is 1", 1, (int) vaccineConfiguration.getIntervals().get(0).getRank1());

		SimpleObject toUpdate = new SimpleObject();
		List<SimpleObject> intervals = new ArrayList<>();
		intervals.add(new SimpleObject().add("timeUnit", "MONTHS").add("timeValue", 10).add("rank1", 2).add("rank2", 3));
		toUpdate.add("intervals", intervals);

		vcResource.update(vaccineConfiguration.getUuid(), toUpdate, new RequestContext());

		assertEquals("rank1 is 1", 2, (int) vaccineConfiguration.getIntervals().get(0).getRank1());
	}
	
	@Test
	public void shouldUpdateAnExistingIntervalIfTheRanksMatch() {
		VaccineConfiguration vaccineConfiguration =
				immunizationAPIService.getVaccineConfigurationByUuid(EXISTING_VACCINE_CONFIGURATION_UUID);
		int initialIntervalsCount = vaccineConfiguration.getIntervals().size();
		// Get the first one.
		Interval interval = vaccineConfiguration.getIntervals().get(0);
		int rank1 = interval.getRank1();
		int rank2 = interval.getRank2();

		SimpleObject toUpdate = new SimpleObject();
		List<SimpleObject> intervals = new ArrayList<>();
		int timeValue = 2;
		String timeUnit = "YEARS";
		intervals.add(new SimpleObject().add("timeUnit", timeUnit).add("timeValue", timeValue).add("rank1", rank1).add("rank2", rank2));
		intervals.add(new SimpleObject().add("timeUnit", "months").add("timeValue", 3).add("rank1", 2).add("rank2", 3));
		toUpdate.add("intervals", intervals);

		vcResource.update(vaccineConfiguration.getUuid(), toUpdate, new RequestContext());

		List<Interval> updatedIntervals = vaccineConfiguration.getIntervals();
		assertEquals("# of intervals should remain the same", initialIntervalsCount + 1, updatedIntervals.size());

		Interval withSameRanks = updatedIntervals.stream()
				.filter(interval1 -> interval1.getRank1() == rank1 && interval1.getRank2() == rank2)
				.findFirst()
				.get();
		assertNotNull(withSameRanks);
		assertEquals((double) timeValue, withSameRanks.getValue().getValue().doubleValue(), 0.001);
		assertEquals("Unit should be updated", timeUnit, withSameRanks.getValue().getUnit().name());
	}
	
	@Test
	public void shouldRemoveAllIntervalsIfAnEmptyListIsPassed() {
		VaccineConfiguration vaccineConfiguration = immunizationAPIService
		        .getVaccineConfigurationByUuid(EXISTING_VACCINE_CONFIGURATION_UUID);
		assertEquals("configuration has one interval", 1, vaccineConfiguration.getIntervals().size());
		
		SimpleObject toUpdate = new SimpleObject().add("intervals", new ArrayList<SimpleObject>());
		
		vcResource.update(vaccineConfiguration.getUuid(), toUpdate, new RequestContext());
		
		assertTrue("No intervals anymore", vaccineConfiguration.getIntervals().isEmpty());
	}
	
	@Test
	public void shouldRetireVaccineConfiguration() {
		VaccineConfiguration vaccineConfiguration = immunizationAPIService
		        .getVaccineConfigurationByUuid(EXISTING_VACCINE_CONFIGURATION_UUID);
		
		assertFalse("configuration should not retired", vaccineConfiguration.getRetired());
		
		String reason = "See if it is retired";
		vcResource.delete(vaccineConfiguration, reason, new RequestContext());
		
		assertTrue("configuration should be retired", vaccineConfiguration.getRetired());
		assertNotNull("Reason is set", vaccineConfiguration.getRetireReason());
		assertEquals("Proper reason should be set", reason, vaccineConfiguration.getRetireReason());
	}
	
	@Test
	public void update_shouldRetireOrUnretireVaccineConfigurationIfRetiredPropertyIsIncluded() {
		VaccineConfiguration configuration = immunizationAPIService
		        .getVaccineConfigurationByUuid(EXISTING_VACCINE_CONFIGURATION_UUID);
		
		assertFalse("configuration should not retired", configuration.getRetired());
		assertTrue(configuration.getRetireReason() == null || configuration.getRetireReason().length() == 0);
		
		String reason = "Pumzisha hii kitu";
		
		SimpleObject toUpdate = new SimpleObject().add("retired", true).add("retireReason", reason);
		
		vcResource.update(configuration.getUuid(), toUpdate, new RequestContext());
		
		assertTrue(configuration.getRetired());
		assertEquals("reason should be set", reason, configuration.getRetireReason());
		
		toUpdate = new SimpleObject().add("retired", "no");
		vcResource.update(configuration.getUuid(), toUpdate, new RequestContext());
		
		assertFalse("Should be unretired", configuration.getRetired());
		assertNull("reason should be null", configuration.getRetireReason());
		assertNull("retiredby should be null", configuration.getRetiredBy());
		assertNull("date retired should be null", configuration.getDateRetired());
	}
	
	@Override
	public void validateDefaultRepresentation() throws Exception {
		super.validateDefaultRepresentation();
		assertPropPresent("name");
		assertPropPresent("concept");
		assertPropPresent("intervals");
		assertPropPresent("numberOfTimes");
		assertPropPresent("display");
	}
	
	@Override
	public void validateFullRepresentation() throws Exception {
		super.validateFullRepresentation();
		validateDefaultRepresentation();
		assertPropPresent("auditInfo");
	}
	
	private SimpleObject createSimpleVaccineConfiguration() {
		return new SimpleObject().add("name", VACCINE_NAME).add("concept", EXISTING_CONCEPT_UUID).add("numberOfTimes", 5);
	}
}
