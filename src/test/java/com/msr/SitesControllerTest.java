package com.msr;

import com.msr.model.Site;
import com.msr.model.UseType;
import com.msr.service.SiteService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Intended as a starting point for unit testing SitesController
 */
@RunWith(SpringRunner.class)
@WebMvcTest(SitesController.class)
class SitesControllerTest {

    private final SitesController sitesController = new SitesController();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SiteService service;

    private Site santaSite;
    private Site elonSite;
    private UseType useType1;
    private UseType useType2;

    void setUp() {
        useType1 = UseType.builder().id(12).name("Office").build();
        useType2 = UseType.builder().id(33).name("Home").build();

        santaSite = Site.builder().id(1).name("Santa's Domain").address("2525 North Pole Way").city("Nicktown").state(
            "MN").zipcode("01225").totalSize(289).primaryType(useType2).build();

        elonSite = Site.builder().id(2).name("SpaceX").address("1111 Milky Way").city("Jupiter").state("TX").zipcode(
            "10101").totalSize(12345).primaryType(useType1).build();
    }

    @Test
    void testSampleResponse_NullMessageParameter() {
        String response = sitesController.getSampleResponse(null, false);
        assertEquals(SitesController.NO_SAMPLE_PARAM_PROVIDED, response);
    }

    @Test
    void testSampleResponse_EmptyMessageParameter() {
        String response = sitesController.getSampleResponse("", false);
        assertEquals(SitesController.NO_SAMPLE_PARAM_PROVIDED, response);
    }

    @Test
    void testSampleResponse_MessageParameterProvided() {
        String expectedString = "This is the expected output parameter.";
        String response = sitesController.getSampleResponse(expectedString, false);
        assertEquals(SitesController.SAMPLE_PARAM_PROVIDED + expectedString, response);
    }

    @Test
    void testSampleResponse_ThrowsWhenThrowErrorIsTrue() {
        assertThrows(RuntimeException.class, () -> sitesController.getSampleResponse(null, true));
    }

    /**
     * Intended to test the controller's get all sites functionality.
     */
    @Test
    void testGetAllSites() throws Exception {
        setUp();

        when(service.getAllSites()).thenReturn(Arrays.asList(santaSite, elonSite));

        mvc.perform(MockMvcRequestBuilders.get("/sites"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").isNotEmpty())
           .andExpect(jsonPath("$[0].id").value(santaSite.getId()))
           .andExpect(jsonPath("$[0].name").value(santaSite.getName()))
           .andExpect(jsonPath("$[0].total_size").value(santaSite.getTotalSize()))
           .andExpect(jsonPath("$[0].primary_type").value(notNullValue()))
           .andExpect(jsonPath("$[0].primary_type.id").value(useType2.getId()))
           .andExpect(jsonPath("$[0].primary_type.name").value(useType2.getName()))
           .andExpect(jsonPath("$[1].id").value(elonSite.getId()))
           .andExpect(jsonPath("$[1].name").value(elonSite.getName()))
           .andExpect(jsonPath("$[1].total_size").value(elonSite.getTotalSize()))
           .andExpect(jsonPath("$[1].primary_type").value(notNullValue()))
           .andExpect(jsonPath("$[1].primary_type.id").value(useType1.getId()))
           .andExpect(jsonPath("$[1].primary_type.name").value(useType1.getName()));
    }

    @Test
    void testGetAllSites_Empty() throws Exception {
        setUp();

        when(service.getAllSites()).thenReturn(Collections.emptyList());

        mvc.perform(MockMvcRequestBuilders.get("/sites"))
           .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllSites_ByState() throws Exception {
        setUp();

        when(service.getSitesByState("TX")).thenReturn(Collections.singletonList(elonSite));

        mvc.perform(MockMvcRequestBuilders.get("/sites?state=TX"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").isNotEmpty())
           .andExpect(jsonPath("$[0].id").value(elonSite.getId()))
           .andExpect(jsonPath("$[0].name").value(elonSite.getName()))
           .andExpect(jsonPath("$[0].total_size").value(elonSite.getTotalSize()))
           .andExpect(jsonPath("$[0].primary_type").value(notNullValue()))
           .andExpect(jsonPath("$[0].primary_type.id").value(useType1.getId()))
           .andExpect(jsonPath("$[0].primary_type.name").value(useType1.getName()));
    }

    @Test
    void testGetAllSites_ByState_Empty() throws Exception {
        setUp();

        when(service.getSitesByState("CA")).thenReturn(Collections.emptyList());

        mvc.perform(MockMvcRequestBuilders.get("/sites?state=CA"))
           .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllSites_Exception() throws Exception {
        setUp();

        when(service.getAllSites()).thenThrow(new RuntimeException());

        mvc.perform(MockMvcRequestBuilders.get("/sites"))
           .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetSiteById() throws Exception {
        setUp();

        int siteId = 1;
        when(service.getSiteById(anyInt())).thenReturn(Optional.of(santaSite));

        mvc.perform(MockMvcRequestBuilders.get("/sites/" + siteId))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(santaSite.getId()))
           .andExpect(jsonPath("$.name").value(santaSite.getName()))
           .andExpect(jsonPath("$.total_size").value(santaSite.getTotalSize()))
           .andExpect(jsonPath("$.primary_type").value(notNullValue()))
           .andExpect(jsonPath("$.primary_type.id").value(useType2.getId()))
           .andExpect(jsonPath("$.primary_type.name").value(useType2.getName()));
    }

    @Test
    void testGetSiteById_Empty() throws Exception {
        setUp();

        int siteId = 1;
        when(service.getSiteById(anyInt())).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get("/sites/" + siteId))
           .andExpect(status().isNoContent());
    }

    @Test
    void testGetSiteById_Exception() throws Exception {
        setUp();

        int siteId = 1;
        when(service.getSiteById(anyInt())).thenThrow(new RuntimeException());

        mvc.perform(MockMvcRequestBuilders.get("/sites/" + siteId))
           .andExpect(status().isInternalServerError());
    }
}