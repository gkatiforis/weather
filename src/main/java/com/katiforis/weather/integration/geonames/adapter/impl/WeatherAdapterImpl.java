package com.katiforis.weather.integration.geonames.adapter.impl;


import com.katiforis.weather.integration.geonames.adapter.WeatherAdapter;
import com.katiforis.weather.integration.geonames.model.Country;
import com.katiforis.weather.integration.geonames.util.Constants;
import com.katiforis.weather.integration.geonames.model.WeatherObservations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@Component
public class WeatherAdapterImpl implements WeatherAdapter {
    public static final Logger logger = LoggerFactory.getLogger(WeatherAdapterImpl.class);

    @Value(value="${geonames.host}")
    private String geoNamesHost;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public WeatherObservations getWeatherObservations(Country country, String username)  {
        logger.info("Start GeoNamesWeatherAdapterImpl.getWeatherObservations, {}, {}", country, username);
        ResponseEntity<WeatherObservations> result;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity requestEntity = new HttpEntity(headers);

        ParameterizedTypeReference<WeatherObservations> responseTypeRef = new ParameterizedTypeReference<WeatherObservations>() {};
        String url = geoNamesHost + Constants.WEATHER_SERVICE_GET_WEATHER_DETAILS_ENDPOINT;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .replaceQueryParam(Constants.WEATHER_SERVICE_WEST_PARAM, country.getWest())
                .replaceQueryParam(Constants.WEATHER_SERVICE_NORTH_PARAM, country.getNorth())
                .replaceQueryParam(Constants.WEATHER_SERVICE_EAST_PARAM, country.getEast())
                .replaceQueryParam(Constants.WEATHER_SERVICE_SOUTH_PARAM, country.getSouth())
                .replaceQueryParam(Constants.WEATHER_SERVICE_USERNAME_PARAM, username);

        URI composedUri = builder.build().toUri();
        WeatherObservations weatherObservations = null;
        try {
            logger.debug("Call URI:  {}", composedUri);
            result = restTemplate.exchange(composedUri, HttpMethod.GET, requestEntity, responseTypeRef);

            if(result != null){
                weatherObservations = result.getBody();
            }


        }catch(RestClientException e) {
            logger.error("Error getting response: {}", e);
            throw e;
        }
        logger.info("End GeoNamesWeatherAdapterImpl.getWeatherObservations");
        return weatherObservations;
    }
}
