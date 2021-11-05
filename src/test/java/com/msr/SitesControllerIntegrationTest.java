package com.msr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BuildingsApiApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
		locations = "classpath:application-integrationtest.properties")
public class SitesControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void testGetAllSite_200() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/sites/"))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$").isNotEmpty())
		   .andExpect(jsonPath("$.length()").value(6))
		   .andExpect(jsonPath("$[0].name").value("Measurabl HQ"))
		   .andExpect(jsonPath("$[0].address").value("707 Broadway Suite 1000"))
		   .andExpect(jsonPath("$[0].city").value("San Diego"))
		   .andExpect(jsonPath("$[0].state").value("CA"))
		   .andExpect(jsonPath("$[0].zipcode").value("92101"))
		   .andExpect(jsonPath("$[0].total_size").value(13000))
		   .andExpect(jsonPath("$[0].primary_type").value(notNullValue()))
		   .andExpect(jsonPath("$[0].primary_type.id").value(54))
		   .andExpect(jsonPath("$[0].primary_type.name").value("Office"))
		   .andExpect(jsonPath("$[5].name").value("Petco Park"))
		   .andExpect(jsonPath("$[5].address").value("100 Park Blvd"))
		   .andExpect(jsonPath("$[5].city").value("San Diego"))
		   .andExpect(jsonPath("$[5].state").value("CA"))
		   .andExpect(jsonPath("$[5].zipcode").value("92101"))
		   .andExpect(jsonPath("$[5].total_size").value(560000))
		   .andExpect(jsonPath("$[5].primary_type").value(notNullValue()))
		   .andExpect(jsonPath("$[5].primary_type.id").value(47))
		   .andExpect(jsonPath("$[5].primary_type.name").value("Open Stadium"));
	}

	@Test
	public void testGetAllSite_ByState_200() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/sites?state=CA"))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$").isNotEmpty())
		   .andExpect(jsonPath("$.length()").value(5))
		   .andExpect(jsonPath("$[0].name").value("Measurabl HQ"))
		   .andExpect(jsonPath("$[0].address").value("707 Broadway Suite 1000"))
		   .andExpect(jsonPath("$[0].city").value("San Diego"))
		   .andExpect(jsonPath("$[0].state").value("CA"))
		   .andExpect(jsonPath("$[0].zipcode").value("92101"))
		   .andExpect(jsonPath("$[0].total_size").value(13000))
		   .andExpect(jsonPath("$[0].primary_type").value(notNullValue()))
		   .andExpect(jsonPath("$[0].primary_type.id").value(54))
		   .andExpect(jsonPath("$[0].primary_type.name").value("Office"))
		   .andExpect(jsonPath("$[1].name").value("Arclight"))
		   .andExpect(jsonPath("$[1].address").value("4425 La Jolla Village Dr"))
		   .andExpect(jsonPath("$[1].city").value("San Diego"))
		   .andExpect(jsonPath("$[1].state").value("CA"))
		   .andExpect(jsonPath("$[1].zipcode").value("92122"))
		   .andExpect(jsonPath("$[1].total_size").value(65000))
		   .andExpect(jsonPath("$[1].primary_type").value(notNullValue()))
		   .andExpect(jsonPath("$[1].primary_type.id").value(40))
		   .andExpect(jsonPath("$[1].primary_type.name").value("Movie Theater"));
	}

	@Test
	public void testGetAllSite_ByState_204() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/sites?state=FL"))
		   .andExpect(status().isNoContent());
	}

	@Test
	public void testGetSiteById_200() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/sites/" + 1))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$.id").value(1))
		   .andExpect(jsonPath("$.name").value("Measurabl HQ"))
		   .andExpect(jsonPath("$.address").value("707 Broadway Suite 1000"))
		   .andExpect(jsonPath("$.city").value("San Diego"))
		   .andExpect(jsonPath("$.state").value("CA"))
		   .andExpect(jsonPath("$.zipcode").value("92101"))
		   .andExpect(jsonPath("$.total_size").value(13000))
		   .andExpect(jsonPath("$.primary_type").value(notNullValue()))
		   .andExpect(jsonPath("$.primary_type.id").value(54))
		   .andExpect(jsonPath("$.primary_type.name").value("Office"));

		mvc.perform(MockMvcRequestBuilders.get("/sites/" + 5))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$.id").value(5))
		   .andExpect(jsonPath("$.name").value("Bellagio"))
		   .andExpect(jsonPath("$.address").value("3600 S Las Vegas Blvd"))
		   .andExpect(jsonPath("$.city").value("Las Vegas"))
		   .andExpect(jsonPath("$.state").value("NV"))
		   .andExpect(jsonPath("$.zipcode").value("89109"))
		   .andExpect(jsonPath("$.total_size").value(1050000))
		   .andExpect(jsonPath("$.primary_type").value(notNullValue()))
		   .andExpect(jsonPath("$.primary_type.id").value(37))
		   .andExpect(jsonPath("$.primary_type.name").value("Casino"));
	}

	@Test
	public void testGetSiteById_204() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/sites/" + 999))
		   .andExpect(status().isNoContent());
	}
}
