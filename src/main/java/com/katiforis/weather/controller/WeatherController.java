package com.katiforis.weather.controller;

import com.katiforis.weather.exception.CountryNotFoundException;
import com.katiforis.weather.services.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/weather")
public class WeatherController {
	public static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

	@Autowired
	WeatherService weatherService;

	/**
	 * Returns map of temperatures per stations
	 * URL example: /weather/countries/GR/temperaturesPerStations
	 * @param countryCode
	 * @return map of temperatures per stations
	 */
	@ResponseBody
	@GetMapping("countries/{countryCode}/temperaturesPerStations")
	public ResponseEntity<?> getTemperaturePerStation(@PathVariable("countryCode") String countryCode) {
		logger.debug("Start WeatherController.getTemperaturePerStation");

		Map<String, Double> temperaturePerStation;
		try {
			temperaturePerStation = weatherService.getTemperaturePerStation(countryCode);
		} catch (CountryNotFoundException e) {
			logger.error("Error: " + e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}catch (Exception e) {
			logger.error("Error: " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Something unexpected occurred: Make sure you have valid geoName account" +
							" and you don't have exceeded your daily request limit.");
		}
		logger.debug("End WeatherController.getTemperaturePerStation");
		return ResponseEntity.status(HttpStatus.OK).body(temperaturePerStation);
	}

}

