package com.katiforis.weather.integration.geonames.adapter;


import com.katiforis.weather.integration.geonames.model.Country;

public interface CountriesAdapter {
    /**
     * Responsible to fetch <code>Country</code> model from geonames.org service
     * @param countryCode
     * @param username
     * @return <code>Country</code> model
     */
    Country getCountry(String countryCode, String username);
}
