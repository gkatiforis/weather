package com.katiforis.weather.services.impl;


import com.katiforis.weather.exception.CountryNotFoundException;
import com.katiforis.weather.integration.geonames.adapter.CountriesAdapter;
import com.katiforis.weather.integration.geonames.model.Country;
import com.katiforis.weather.services.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;



@Service
public class CountryServiceImpl implements CountryService {
	private static final Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);

	@Autowired
	CountriesAdapter countriesAdapter;

	@Override
	@Cacheable(value = "countries")
	public Country getCountry(String countryCode) throws CountryNotFoundException {
		logger.info("Start CountryServiceImpl.getCountry, {}", countryCode);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null){
			return null;
		}

		String currentPrincipal = authentication.getName();

		Country country = countriesAdapter.getCountry(countryCode, currentPrincipal);

		if (country == null) {
			throw new CountryNotFoundException("Country couldn't be found for county code: " + countryCode);
		}

		logger.info("End CountryServiceImpl.getCountry");
		return country;
	}
}
