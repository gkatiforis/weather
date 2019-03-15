package com.katiforis.weather.integration.geonames.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Countries implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("geonames")
    private List<Country> countries;

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Countries countries1 = (Countries) o;

        return countries != null ? countries.equals(countries1.countries) : countries1.countries == null;
    }

    @Override
    public int hashCode() {
        return countries != null ? countries.hashCode() : 0;
    }
}
