package com.katiforis.weather.services.impl;

import com.katiforis.weather.exception.CountryNotFoundException;
import com.katiforis.weather.integration.geonames.model.Country;
import com.katiforis.weather.integration.geonames.adapter.CountriesAdapter;
import com.katiforis.weather.services.CountryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;


@RunWith(SpringRunner.class)
public class CountryServiceImplTest{

	@TestConfiguration
	static class CountryServiceImplTestContextConfiguration {
		@Bean
		public CountryService countryService() {
			return new CountryServiceImpl();
		}
	}

	@Autowired
	CountryService countryService;

	@MockBean
	CountriesAdapter countriesAdapter;


	@Test
	@WithMockUser(username = "username")
	public void getCountryTest() throws CountryNotFoundException {
		Country	expectedCountry = new Country(1.1, 1.1, 1.1, 1.1);
		Mockito.when(countriesAdapter.getCountry("GR", "username"))
				.thenReturn(new Country(1.1, 1.1, 1.1, 1.1));
		Country resultCountry = countryService.getCountry("GR");

		assertTrue(resultCountry.equals(expectedCountry));
	}


	@Test(expected = CountryNotFoundException.class)
	@WithMockUser(username = "username")
	public void getCountryCountryNotFoundExceptionTest() throws CountryNotFoundException {
		Mockito.when(countriesAdapter.getCountry("GR", "username")).thenReturn( null);
		countryService.getCountry("GR");
	}

	@Test
	public void getCountryCountryNoAuthenticationTest() throws CountryNotFoundException {
		assertNull(countryService.getCountry("GR"));
	}
}
