package com.katiforis.weather.integration.geonames.adapter.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katiforis.weather.exception.CountryNotFoundException;
import com.katiforis.weather.integration.geonames.adapter.WeatherAdapter;
import com.katiforis.weather.integration.geonames.model.Country;
import com.katiforis.weather.integration.geonames.model.WeatherObservation;
import com.katiforis.weather.integration.geonames.model.WeatherObservations;
import com.katiforis.weather.integration.geonames.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;


@RunWith(SpringRunner.class)
@RestClientTest(WeatherAdapterImpl.class)
public class WeatherAdapterImplTest {

	@Autowired
	private WeatherAdapter weatherAdapter;

	@Value(value="${geonames.host}")
	private String geoNamesHost;

	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	private ObjectMapper mapper = new ObjectMapper();;

	@TestConfiguration
	static class CountryServiceImplTestContextConfiguration {
		@Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

	@Before
	public void init() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}


	private WeatherObservations getWeatherObservations(){
		WeatherObservations expectedWeatherObservations = new WeatherObservations();
		List<WeatherObservation> weatherObservations = new ArrayList<>();
		weatherObservations.add(new WeatherObservation("station", 17.8));
		expectedWeatherObservations.setWeatherObservations(weatherObservations);
		return expectedWeatherObservations;
	}

	@Test
	public void getWeatherObservationsTest() throws CountryNotFoundException, JsonProcessingException, URISyntaxException {
		Country	country = new Country(1.1, 1.1, 1.1, 1.1);
		mockServer.expect(ExpectedCount.once(),
				requestTo(new URI(geoNamesHost + Constants.WEATHER_SERVICE_GET_WEATHER_DETAILS_ENDPOINT +
						"?west=1.1&north=1.1&east=1.1&south=1.1&username=username")))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(mapper.writeValueAsString(getWeatherObservations())));

		assertTrue(weatherAdapter.getWeatherObservations(country, "username").equals(getWeatherObservations()));
		mockServer.verify();
	}

	@Test(expected = RestClientException.class)
	public void getWeatherObservationsRestClientExceptionTest() throws CountryNotFoundException, JsonProcessingException, URISyntaxException {
		Country	country = new Country(1.1, 1.1, 1.1, 1.1);
		mockServer.expect(ExpectedCount.once(),
				requestTo(new URI(geoNamesHost + Constants.WEATHER_SERVICE_GET_WEATHER_DETAILS_ENDPOINT +
						"?west=1.1&north=1.1&east=1.1&south=1.1&username=username")))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
						.contentType(MediaType.APPLICATION_JSON)
						.body(mapper.writeValueAsString(getWeatherObservations())));

		assertTrue(weatherAdapter.getWeatherObservations(country, "username").equals(getWeatherObservations()));
		mockServer.verify();
	}

	@Test
	public void getCountryNullBodyTest() throws CountryNotFoundException, JsonProcessingException, URISyntaxException {
		Country	country = new Country(1.1, 1.1, 1.1, 1.1);
		mockServer.expect(ExpectedCount.once(),
				requestTo(new URI(geoNamesHost + Constants.WEATHER_SERVICE_GET_WEATHER_DETAILS_ENDPOINT +
						"?west=1.1&north=1.1&east=1.1&south=1.1&username=username")))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON)
						);

		assertNull(weatherAdapter.getWeatherObservations(country, "username"));
		mockServer.verify();
	}
}
