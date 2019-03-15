package com.katiforis.weather.services;

import com.katiforis.weather.exception.CountryNotFoundException;
import com.katiforis.weather.integration.geonames.model.Country;

public interface CountryService {
    /**
     * Returns <code>Country</code> model
     * @param countryCode
     * @return <code>Country</code> model
     * @throws CountryNotFoundException
     */
    Country getCountry(String countryCode) throws CountryNotFoundException;
}

