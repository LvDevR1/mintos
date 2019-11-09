package com.rest.mintos.provider;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rest.mintos.api.beans.LocationBean;
import com.rest.mintos.api.beans.WeatherResponseBean;

@Component
@CacheConfig(cacheNames = { "weathers" })
public class OpenWeatherProvider implements ForecastProvider {

    public static final String ACTUAL_STATUS = "Actual Response from provider";
    public static final String UNAVAILABLE_STATUS = "Service unavailable";

    private final String apiKey;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public OpenWeatherProvider(@Value("${openweather.apiKey:}") String apiKey, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Cacheable(cacheNames = "weathers")
    @HystrixCommand(fallbackMethod = "getEmptyBean" )
    @Override
    public WeatherResponseBean getForecastResponse(LocationBean locationBean) {
        System.out.println(locationBean.toString());
        String resourceUrl= "http://api.openweathermap.org/data/2.5/weather?lat=" + locationBean.getLatitude() + "&lon=" + locationBean.getLongitude() + "&appid=" + apiKey;
        ResponseEntity<String> providerResponse = restTemplate.getForEntity(resourceUrl, String.class);
        return parseResponse(providerResponse.getBody());
    }

    public WeatherResponseBean getEmptyBean(LocationBean locationBean) {
        return new WeatherResponseBean(false, UNAVAILABLE_STATUS, new HashMap());
    }

    private WeatherResponseBean parseResponse(String providerResponse) {
        HashMap response;
        try {
            response = objectMapper.readValue(providerResponse, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not get actual response from provider");
        }
        return new WeatherResponseBean(true, ACTUAL_STATUS, response);
    }

}
