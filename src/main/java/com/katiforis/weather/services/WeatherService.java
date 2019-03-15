package com.katiforis.weather.services;

import com.katiforis.weather.exception.CountryNotFoundException;
import com.katiforis.weather.integration.geonames.model.WeatherObservation;

import java.util.List;
import java.util.Map;

public interface WeatherService {
    /**
     * Returns list of <code>WeatherObservation</code> models
     * @param countryCode
     * @return list of <code>WeatherObservation</code> models
     * @throws CountryNotFoundException
     */
    List<WeatherObservation> getWeatherObservations(String countryCode) throws CountryNotFoundException;

    /**
     * Returns map of temperatures per stations from <code>WeatherObservation</code> model
     * @param countryCode
     * @return map of temperatures per stations from <code>WeatherObservation</code> model
     * @throws CountryNotFoundException
     */
    Map<String, Double> getTemperaturePerStation(String countryCode) throws CountryNotFoundException;
}

