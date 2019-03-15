package com.katiforis.weather.integration.geonames.adapter.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katiforis.weather.exception.CountryNotFoundException;
import com.katiforis.weather.integration.geonames.adapter.CountriesAdapter;
import com.katiforis.weather.integration.geonames.model.Countries;
import com.katiforis.weather.integration.geonames.model.Country;
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
import java.util.Arrays;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;


@RunWith(SpringRunner.class)
@RestClientTest(CountriesAdapterImpl.class)
public class CountriesAdapterImplTest {

	@Autowired
	private CountriesAdapter countriesAdapter;

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


	private Countries getCountries(){
		Countries countries = new Countries();
		countries.setCountries(Arrays.asList(new Country(1.1, 1.1, 1.1, 1.1)));
		return countries;
	}

	@Test
	public void getCountryTest() throws CountryNotFoundException, JsonProcessingException, URISyntaxException {

		mockServer.expect(ExpectedCount.once(),
				requestTo(new URI(geoNamesHost + Constants.COUNTRY_SERVICE_GET_COUNTRY_DETAILS_ENDPOINT +
						"?country=GR&username=username")))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(mapper.writeValueAsString(getCountries())));

		assertTrue(countriesAdapter.getCountry("GR", "username").equals(getCountries().getCountries().get(0)));
		mockServer.verify();
	}

	@Test(expected = RestClientException.class)
	public void getCountryRestClientExceptionTest() throws CountryNotFoundException, JsonProcessingException, URISyntaxException {

		mockServer.expect(ExpectedCount.once(),
				requestTo(new URI(geoNamesHost + Constants.COUNTRY_SERVICE_GET_COUNTRY_DETAILS_ENDPOINT +
						"?country=GR&username=username")))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
						.contentType(MediaType.APPLICATION_JSON)
						.body(mapper.writeValueAsString(getCountries())));

		assertTrue(countriesAdapter.getCountry("GR", "username").equals(getCountries().getCountries().get(0)));
		mockServer.verify();
	}

	@Test
	public void getCountryNullBodyTest() throws CountryNotFoundException, JsonProcessingException, URISyntaxException {

		mockServer.expect(ExpectedCount.once(),
				requestTo(new URI(geoNamesHost + Constants.COUNTRY_SERVICE_GET_COUNTRY_DETAILS_ENDPOINT +
						"?country=GR&username=username")))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON)
						);

		assertNull(countriesAdapter.getCountry("GR", "username"));
		mockServer.verify();
	}

	@Test
	public void getCountryNullCountryListTest() throws CountryNotFoundException, JsonProcessingException, URISyntaxException {

		mockServer.expect(ExpectedCount.once(),
				requestTo(new URI(geoNamesHost + Constants.COUNTRY_SERVICE_GET_COUNTRY_DETAILS_ENDPOINT +
						"?country=GR&username=username")))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON)
						.body(mapper.writeValueAsString(new Countries())));


		assertNull(countriesAdapter.getCountry("GR", "username"));
		mockServer.verify();
	}

	@Test
	public void getCountryEmptyCountryListTest() throws CountryNotFoundException, JsonProcessingException, URISyntaxException {

		Countries countries = new Countries();
		countries.setCountries(Arrays.asList());
		mockServer.expect(ExpectedCount.once(),
				requestTo(new URI(geoNamesHost + Constants.COUNTRY_SERVICE_GET_COUNTRY_DETAILS_ENDPOINT +
						"?country=GR&username=username")))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON)
						.body(mapper.writeValueAsString(countries)));


		assertNull(countriesAdapter.getCountry("GR", "username"));
		mockServer.verify();
	}

}
