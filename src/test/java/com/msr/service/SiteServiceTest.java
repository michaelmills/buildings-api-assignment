package com.msr.service;

import com.msr.data.SiteRepository;
import com.msr.model.Site;
import com.msr.model.SiteUse;
import com.msr.model.UseType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class SiteServiceTest {
	@TestConfiguration
	static class EmployeeServiceImplTestContextConfiguration {

		@Bean
		public SiteService employeeService() {
			return new SiteService();
		}
	}

	@Autowired
	private SiteService service;

	@MockBean
	private SiteRepository siteRepository;

	private Site santaSite;
	private Site elonSite;
	private SiteUse siteUse1;
	private SiteUse siteUse2;
	private SiteUse siteUse3;
	private UseType useType1;
	private UseType useType2;

	@Before
	public void setUp() {
		useType1 = UseType.builder().id(12).name("Office").build();
		useType2 = UseType.builder().id(33).name("Home").build();

		siteUse1 = SiteUse.builder().id(99).description("ToyShed").sizeSqft(2000).useType(useType1).build();

		siteUse2 = SiteUse.builder().id(3645).description("Cottage").sizeSqft(900).useType(useType2).build();

		siteUse3 = SiteUse.builder().id(9210).description("ToyFactory").sizeSqft(5000).useType(useType1).build();

		santaSite = Site.builder().id(1).name("Santa's Domain").address("2525 North Pole Way").city("Nicktown").state("MN")
		                .zipcode("01225").siteUses(Arrays.asList(siteUse1, siteUse2, siteUse3)).build();

		elonSite = Site.builder().id(2).name("SpaceX").address("1111 Milky Way").city("Jupiter").state("TX").zipcode(
				"10101").siteUses(Collections.singletonList(siteUse3)).build();
	}

	@Test
	public void testGetSiteById() {
		when(siteRepository.findById(santaSite.getId())).thenReturn(Optional.of(santaSite));

		Optional<Site> result = service.getSiteById(santaSite.getId());

		assertTrue(result.isPresent());

		Site resultSite = result.get();
		assertEquals(santaSite.getId(), resultSite.getId());
		assertEquals(siteUse1.getSizeSqft() + siteUse2.getSizeSqft() + siteUse3.getSizeSqft(), resultSite.getTotalSize());
		assertEquals(siteUse1.getUseType(), resultSite.getPrimaryType());
	}

	@Test
	public void testGetSiteById_NoResults() {
		int unknownId = 999;
		when(siteRepository.findById(unknownId)).thenReturn(Optional.empty());

		Optional<Site> result = service.getSiteById(unknownId);

		assertFalse(result.isPresent());
	}

	@Test
	public void testGetSiteById_SameSqft() {
		siteUse2 = siteUse2.toBuilder().sizeSqft(siteUse1.getSizeSqft()).build();

		santaSite = santaSite.toBuilder().siteUses(Arrays.asList(siteUse1, siteUse2)).build();

		when(siteRepository.findById(santaSite.getId())).thenReturn(Optional.of(santaSite));

		Optional<Site> result = service.getSiteById(santaSite.getId());

		assertTrue(result.isPresent());

		Site resultSite = result.get();
		assertEquals(santaSite.getId(), resultSite.getId());
		assertEquals(siteUse1.getSizeSqft() + siteUse2.getSizeSqft(), resultSite.getTotalSize());
		assertEquals(siteUse2.getUseType(), resultSite.getPrimaryType());
	}

	@Test
	public void testGetAllSites() {
		when(siteRepository.findAll()).thenReturn(Arrays.asList(santaSite, elonSite));

		List<Site> result = service.getAllSites();

		assertFalse(result.isEmpty());
		assertEquals(2, result.size());

		Site resultSite1 = result.get(0);
		assertEquals(santaSite.getId(), resultSite1.getId());
		assertEquals(siteUse1.getSizeSqft() + siteUse2.getSizeSqft() + siteUse3.getSizeSqft(), resultSite1.getTotalSize());
		assertEquals(siteUse1.getUseType(), resultSite1.getPrimaryType());

		Site resultSite2 = result.get(1);
		assertEquals(santaSite.getId(), resultSite2.getId());
		assertEquals(siteUse3.getSizeSqft(), resultSite2.getTotalSize());
		assertEquals(siteUse3.getUseType(), resultSite2.getPrimaryType());
	}

	@Test
	public void testGetAllSites_NoSites() {
		when(siteRepository.findAll()).thenReturn(Collections.emptyList());

		List<Site> result = service.getAllSites();

		assertTrue(result.isEmpty());
	}

	@Test
	public void testSitesByState() {
		when(siteRepository.findByState(santaSite.getState())).thenReturn(Collections.singletonList(santaSite));

		List<Site> result = service.getSitesByState(santaSite.getState());

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());

		Site resultSite1 = result.get(0);
		assertEquals(santaSite.getId(), resultSite1.getId());
		assertEquals(siteUse1.getSizeSqft() + siteUse2.getSizeSqft() + siteUse3.getSizeSqft(), resultSite1.getTotalSize());
		assertEquals(siteUse1.getUseType(), resultSite1.getPrimaryType());
	}

	@Test
	public void testSitesByState_NoSites() {
		when(siteRepository.findByState("OOOOO")).thenReturn(Collections.emptyList());

		List<Site> result = service.getSitesByState("CA");

		assertTrue(result.isEmpty());
	}
}
