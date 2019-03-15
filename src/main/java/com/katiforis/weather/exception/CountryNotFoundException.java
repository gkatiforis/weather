package com.katiforis.weather.exception;

/**
 * Used when <code>Country</code> model is not presented
 */
public class CountryNotFoundException extends Exception {
    public CountryNotFoundException() {
    }

    public CountryNotFoundException(String message) {
        super(message);
    }
}
