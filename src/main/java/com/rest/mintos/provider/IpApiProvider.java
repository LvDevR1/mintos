package com.rest.mintos.provider;

import static com.rest.mintos.utils.BigDecimalUtils.amount;
import static java.lang.String.valueOf;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rest.mintos.api.beans.LocationBean;

@Component
@CacheConfig(cacheNames = { "locations" })
public class IpApiProvider implements LocationProvider {

    private final String accessKey;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public IpApiProvider(@Value("${ipApi.accessKey:}") String accessKey, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.accessKey = accessKey;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable(cacheNames = "locations")
    @HystrixCommand(fallbackMethod = "getEmptyLocationBean")
    public LocationBean getLocationByIp(String ipAddress) {
        String resourceUrl = "http://api.ipapi.com/" + ipAddress + "?access_key=" + accessKey;
        HashMap locationMap;
        ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl, String.class);
        try {
            locationMap = objectMapper.readValue(response.getBody(), HashMap.class);
        } catch (Exception e) {
            throw new IllegalStateException("Could not get actual response from location provider");
        }
        BigDecimal lattitude = amount(valueOf(locationMap.get("latitude")));
        BigDecimal longtitude = amount(valueOf(locationMap.get("longitude")));
        return new LocationBean(lattitude, longtitude, ipAddress);
    }

    public LocationBean getEmptyLocationBean(String ipAddress) {
        return new LocationBean(null, null, ipAddress);
    }
}
