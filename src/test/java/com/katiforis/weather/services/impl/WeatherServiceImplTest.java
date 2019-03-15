package com.katiforis.weather.services.impl;

import com.katiforis.weather.exception.CountryNotFoundException;
import com.katiforis.weather.integration.geonames.adapter.WeatherAdapter;
import com.katiforis.weather.integration.geonames.model.Country;
import com.katiforis.weather.integration.geonames.model.WeatherObservation;
import com.katiforis.weather.integration.geonames.model.WeatherObservations;
import com.katiforis.weather.services.CountryService;
import com.katiforis.weather.services.WeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.spy;


@RunWith(SpringRunner.class)
public class WeatherServiceImplTest {

	@TestConfiguration
	static class WeatherServiceImplTestContextConfiguration {
		@Bean
		public WeatherService countryService() {
			return new WeatherServiceImpl();
		}
	}

	@Autowired
	WeatherService weatherService;

	@MockBean
	CountryService countryService;

	@MockBean
	WeatherAdapter weatherAdapter;

	private WeatherObservations getWeatherObservations(){
		WeatherObservations expectedWeatherObservations = new WeatherObservations();
		List<WeatherObservation> weatherObservations = new ArrayList<>();
		weatherObservations.add(new WeatherObservation("station", 17.8));
		expectedWeatherObservations.setWeatherObservations(weatherObservations);
		return expectedWeatherObservations;
	}

	@Test
	@WithMockUser(username = "username")
	public void getWeatherObservationsTest() throws CountryNotFoundException {

		WeatherObservations expectedWeatherObservations = getWeatherObservations();
		Country country = new Country(1.1, 1.1, 1.1, 1.1);
		Mockito.when(countryService.getCountry("GR"))
				.thenReturn(country);

		Mockito.when(weatherAdapter.getWeatherObservations(country, "username")).thenReturn(expectedWeatherObservations);

		assertTrue(weatherService.getWeatherObservations("GR")
				.equals( getWeatherObservations().getWeatherObservations()));
	}

	@Test(expected = CountryNotFoundException.class )
	@WithMockUser(username = "username")
	public void getWeatherObservationsCountryNotFoundExceptionTest() throws CountryNotFoundException {
		Mockito.when(countryService.getCountry("GR")).thenAnswer( a -> {throw new CountryNotFoundException();});
		weatherService.getWeatherObservations("GR").isEmpty();
	}


	@Test
	@WithMockUser(username = "username")
	public void getWeatherObservationsNullCountryTest() throws CountryNotFoundException {
		Mockito.when(countryService.getCountry("GR")).thenReturn(null);
		assertTrue(weatherService.getWeatherObservations("GR").isEmpty());
	}

	@Test
	@WithMockUser(username = "username")
	public void getWeatherObservationsNullWeatherObservationsTest() throws CountryNotFoundException {
		Mockito.when(countryService.getCountry("GR")).thenReturn(new Country());
		assertTrue(weatherService.getWeatherObservations("GR").isEmpty());
	}

	@Test
	public void getWeatherObservationsNoAuthenticationTest() throws CountryNotFoundException {
		assertTrue(weatherService.getWeatherObservations("GR").isEmpty());
	}


	@Test
	public void getTemperaturePerStationTest() throws Exception {
		WeatherObservations expectedWeatherObservations = getWeatherObservations();

		Map<String, Double> temperaturePerStation = new HashMap<>();
		temperaturePerStation.put("station", 17.8);

		WeatherService  mock = spy(new WeatherServiceImpl());
		Mockito.doReturn(expectedWeatherObservations.getWeatherObservations())
				.when(mock).getWeatherObservations("GR");

		assertTrue(mock.getTemperaturePerStation("GR")
				.equals(temperaturePerStation));
	}

	@Test
	public void getTemperaturePerStationEmptyTest() throws Exception {
		WeatherService  mock = spy(new WeatherServiceImpl());
		Mockito.doReturn(Arrays.asList())
				.when(mock).getWeatherObservations("GR");

		assertTrue(mock.getTemperaturePerStation("GR")
				.equals( new HashMap<>()));
	}
}
