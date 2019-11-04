package com.rest.mintos.provider;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@CacheConfig(cacheNames={"locations"})
public class IpApiProvider implements LocationProvider {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    @Cacheable(cacheNames = "locations")
    public HashMap getLocationByIp(String ipAdress) {
        System.out.println(ipAdress);
        String ipAdress2 = "195.12.122.23";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String fooResourceUrl
            = "https://ipapi.co/" + ipAdress2 + "/json/";
        ResponseEntity<String> response
            = restTemplate.getForEntity(fooResourceUrl, String.class);
        HashMap locationMap;
        try {
            locationMap = objectMapper.readValue(response.getBody(), HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new HashMap();
        }
        return locationMap;
    }
}
