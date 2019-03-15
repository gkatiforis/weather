package com.katiforis.weather.integration.geonames.adapter.impl;


import com.katiforis.weather.integration.geonames.adapter.CountriesAdapter;
import com.katiforis.weather.integration.geonames.util.Constants;
import com.katiforis.weather.integration.geonames.model.Countries;
import com.katiforis.weather.integration.geonames.model.Country;

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
public class CountriesAdapterImpl implements CountriesAdapter {
    public static final Logger logger = LoggerFactory.getLogger(CountriesAdapterImpl.class);

    @Value(value="${geonames.host}")
    private String geoNamesHost;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Country getCountry(String countryCode, String username)  {
        logger.info("Start CountriesAdapterImpl.getCountry countryCode: {} username: {}", countryCode, username);
        ResponseEntity<Countries> result;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity requestEntity = new HttpEntity(headers);

        ParameterizedTypeReference<Countries> responseTypeRef = new ParameterizedTypeReference<Countries>() {};
        String url = geoNamesHost + Constants.COUNTRY_SERVICE_GET_COUNTRY_DETAILS_ENDPOINT;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .replaceQueryParam(Constants.COUNTRY_SERVICE_COUNTRY_CODE_PARAM, countryCode)
                .replaceQueryParam(Constants.COUNTRY_SERVICE_USERNAME_PARAM, username);

        URI composedUri = builder.build().toUri();
        Country country = null;
        try {
            logger.debug("Call URI: {}", composedUri);
            result = restTemplate.exchange(composedUri, HttpMethod.GET, requestEntity, responseTypeRef);
            if(result != null){
                Countries countries =  result.getBody();
                if(countries != null && countries.getCountries() != null && !countries.getCountries().isEmpty()){
                    country = countries.getCountries().get(0);
                }
            }

        }catch(RestClientException e) {
            logger.error("Error getting response: {}", e);
            throw e;
        }
        logger.info("End CountriesAdapterImpl.getCountry");
        return country;
    }
}
