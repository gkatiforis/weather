package com.katiforis.weather.integration.geonames.adapter;


import com.katiforis.weather.integration.geonames.model.Country;
import com.katiforis.weather.integration.geonames.model.WeatherObservations;

public interface WeatherAdapter {
    /**
     * Responsible to fetch <code>WeatherObservations</code> model from geonames.org service
     * @param country
     * @param username
     * @return <code>WeatherObservations</code> model
     */
    WeatherObservations getWeatherObservations(Country country, String username);
}
