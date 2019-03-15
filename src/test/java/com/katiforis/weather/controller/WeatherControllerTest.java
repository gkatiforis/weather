package com.katiforis.weather.controller;

import com.katiforis.weather.exception.CountryNotFoundException;
import com.katiforis.weather.services.WeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.HashMap;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {

	private static final String basePath = "/weather/";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	WeatherService weatherService;

	@Test
	@WithMockUser(username = "username")
	public void getTemperaturePerStationTest() throws Exception {
		Map<String, Double> temperaturePerStation = new HashMap<>();
		temperaturePerStation.put("station", 17.8);

		Mockito.when(weatherService.getTemperaturePerStation("GR"))
				.thenReturn(temperaturePerStation);

		this.mockMvc.perform(get(basePath + "countries/GR/temperaturesPerStations"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "username")
	public void getTemperaturePerStationCountryNotFoundExceptionTest() throws Exception {
		Map<String, Double> temperaturePerStation = new HashMap<>();
		temperaturePerStation.put("station", 17.8);

		Mockito.when(weatherService.getTemperaturePerStation("GR")).thenAnswer( a -> {throw new CountryNotFoundException();});

		this.mockMvc.perform(get(basePath + "countries/GR/temperaturesPerStations"))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "username")
	public void getTemperaturePerStationExceptionTest() throws Exception {
		Map<String, Double> temperaturePerStation = new HashMap<>();
		temperaturePerStation.put("station", 17.8);

		Mockito.when(weatherService.getTemperaturePerStation("GR")).thenAnswer( a -> {throw new Exception();});

		this.mockMvc.perform(get(basePath + "countries/GR/temperaturesPerStations"))
				.andDo(print())
				.andExpect(status().isInternalServerError());
	}

}
