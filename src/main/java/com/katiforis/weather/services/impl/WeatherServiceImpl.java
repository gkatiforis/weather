package com.katiforis.weather.services.impl;


import com.katiforis.weather.exception.CountryNotFoundException;
import com.katiforis.weather.integration.geonames.adapter.WeatherAdapter;
import com.katiforis.weather.integration.geonames.model.WeatherObservation;
import com.katiforis.weather.integration.geonames.model.Country;
import com.katiforis.weather.integration.geonames.model.WeatherObservations;
import com.katiforis.weather.services.CountryService;
import com.katiforis.weather.services.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class WeatherServiceImpl implements WeatherService {
	private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

	@Autowired
	CountryService countryService;

	@Autowired
	WeatherAdapter weatherAdapter;

	@Override
	@Cacheable(value = "temperaturePerStation")
	public Map<String, Double> getTemperaturePerStation(String countryCode) throws CountryNotFoundException {
		logger.info("Start WeatherServiceImpl.getTemperaturePerStation, {}", countryCode);
		return this.getWeatherObservations(countryCode)
				.stream()
				.collect(Collectors.toMap(WeatherObservation::getStationName, WeatherObservation::getTemperature));
	}

	@Override
	@Cacheable(value = "weatherObservations")
	public List<WeatherObservation> getWeatherObservations(String countryCode) throws CountryNotFoundException {
		logger.info("Start WeatherServiceImpl.getWeatherObservations, {}", countryCode);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(authentication == null){
			//return empty list in case of no authentication
			return Arrays.asList();
		}

		String currentPrincipal = authentication.getName();

		//get country by countryCode
		Country country = countryService.getCountry(countryCode);

		if(country == null){
			//return empty list in case of invalid country
			return Arrays.asList();
		}

		//get weather observations based on country's location
		WeatherObservations weatherObservations = weatherAdapter.getWeatherObservations(country, currentPrincipal);

		if(weatherObservations == null){
			//return empty list in case of invalid weather observations
			return Arrays.asList();
		}

		logger.info("End WeatherServiceImpl.getWeatherObservations");
		return weatherObservations.getWeatherObservations();
	}


}
