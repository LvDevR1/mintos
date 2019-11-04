package com.rest.mintos.provider;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.rest.mintos.api.beans.LocationBean;

@Component
@CacheConfig(cacheNames={"weathers"})
public class OpenWeatherProvider implements ForecastProvider {

    @Cacheable(cacheNames = "weathers")
    @Override
    public String getForecastResponse(LocationBean locationBean) {
        System.out.println(locationBean.toString());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String fooResourceUrl
            = "http://api.openweathermap.org/data/2.5/weather?lat=" + locationBean.getLatitude()
            + "&lon=" + locationBean.getLongitude() + "&appid=82a8c56f34d2d0125dbb349d361fb31c";
        ResponseEntity<String> response
            = restTemplate.getForEntity(fooResourceUrl, String.class);
        return response.getBody();

    }


}
