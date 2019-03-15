package com.katiforis.weather.integration.geonames.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WeatherObservations implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("weatherObservations")
    private List<WeatherObservation> weatherObservations;

    public List<WeatherObservation> getWeatherObservations() {
        return weatherObservations;
    }

    public void setWeatherObservations(List<WeatherObservation> weatherObservations) {
        this.weatherObservations = weatherObservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherObservations that = (WeatherObservations) o;

        return weatherObservations != null ? weatherObservations.equals(that.weatherObservations) : that.weatherObservations == null;
    }

    @Override
    public int hashCode() {
        return weatherObservations != null ? weatherObservations.hashCode() : 0;
    }
}
