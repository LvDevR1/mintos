package com.rest.mintos.provider

import static com.rest.mintos.provider.OpenWeatherProvider.ACTUAL_STATUS
import static com.rest.mintos.provider.OpenWeatherProvider.UNAVAILABLE_STATUS
import static org.springframework.http.HttpStatus.OK

import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

import com.fasterxml.jackson.databind.ObjectMapper
import com.rest.mintos.api.beans.LocationBean
import com.rest.mintos.api.beans.WeatherResponseBean

import spock.lang.Specification

class OpenWeatherProviderSpec extends Specification {

    static final String ACCESS_KEY = 'key'

    static final BigDecimal LONGITUDE = 12.00

    static final BigDecimal LATITUDE = 40.40

    ObjectMapper objectMapper = Mock(ObjectMapper)

    RestTemplate restTemplate = Mock(RestTemplate)

    LocationBean locationBean = new LocationBean(latitude: LATITUDE, longitude: LONGITUDE)

    OpenWeatherProvider provider = new OpenWeatherProvider(ACCESS_KEY, objectMapper, restTemplate)

    def 'should get response from provider'() {
        given:
            restTemplate.getForEntity("http://api.openweathermap.org/data/2.5/weather?lat=" + LATITUDE + "&lon=" + LONGITUDE + "&appid=" + ACCESS_KEY, String.class) >>
                new ResponseEntity('test response', OK)
            objectMapper.readValue('test response', HashMap) >> ['weather': 'clouds']
        when:
            WeatherResponseBean result = provider.getForecastResponse(locationBean)
        then:
            result.status == ACTUAL_STATUS
            result.status
            result.forecast == ['weather': 'clouds']
    }

    def 'should get empty response from provider'() {
        when:
            WeatherResponseBean result = provider.getEmptyBean(locationBean)
        then:
            !result.success
            result.status == UNAVAILABLE_STATUS
    }
}
