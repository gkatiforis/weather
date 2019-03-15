package com.katiforis.weather.integration.geonames.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown=true)
public class WeatherObservation implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("stationName")
    private String stationName;
    @JsonProperty("temperature")
    private Double temperature;
    @JsonProperty("humidity")
    private Double humidity;

    public WeatherObservation(){}
    public WeatherObservation(String stationName, Double temperature) {
        this.stationName = stationName;
        this.temperature = temperature;
    }

    public String getStationName() {
        return stationName;
    }

    public Double getTemperature() {
        return temperature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherObservation that = (WeatherObservation) o;

        if (stationName != null ? !stationName.equals(that.stationName) : that.stationName != null) return false;
        if (temperature != null ? !temperature.equals(that.temperature) : that.temperature != null) return false;
        return humidity != null ? humidity.equals(that.humidity) : that.humidity == null;
    }

    @Override
    public int hashCode() {
        int result = stationName != null ? stationName.hashCode() : 0;
        result = 31 * result + (temperature != null ? temperature.hashCode() : 0);
        result = 31 * result + (humidity != null ? humidity.hashCode() : 0);
        return result;
    }
}
