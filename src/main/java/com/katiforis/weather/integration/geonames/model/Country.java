package com.katiforis.weather.integration.geonames.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown=true)
public class Country implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("countryName")
    private String name;
    @JsonProperty("west")
    private Double west;
    @JsonProperty("north")
    private Double north;
    @JsonProperty("east")
    private Double east;
    @JsonProperty("south")
    private Double south;

    public Country(){}
    public Country(Double west, Double north, Double east, Double south) {
        this.west = west;
        this.north = north;
        this.east = east;
        this.south = south;
    }

    public Double getWest() {
        return west;
    }

    public Double getNorth() {
        return north;
    }

    public Double getEast() {
        return east;
    }

    public Double getSouth() {
        return south;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWest(Double west) {
        this.west = west;
    }

    public void setNorth(Double north) {
        this.north = north;
    }

    public void setEast(Double east) {
        this.east = east;
    }

    public void setSouth(Double south) {
        this.south = south;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (name != null ? !name.equals(country.name) : country.name != null) return false;
        if (west != null ? !west.equals(country.west) : country.west != null) return false;
        if (north != null ? !north.equals(country.north) : country.north != null) return false;
        if (east != null ? !east.equals(country.east) : country.east != null) return false;
        return south != null ? south.equals(country.south) : country.south == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (west != null ? west.hashCode() : 0);
        result = 31 * result + (north != null ? north.hashCode() : 0);
        result = 31 * result + (east != null ? east.hashCode() : 0);
        result = 31 * result + (south != null ? south.hashCode() : 0);
        return result;
    }
}
