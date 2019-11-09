package com.rest.mintos.provider

import static org.springframework.http.HttpStatus.OK

import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

import com.fasterxml.jackson.databind.ObjectMapper
import com.rest.mintos.api.beans.LocationBean

import spock.lang.Specification

class IpApiProviderSpec extends Specification {

    static final String IP_ADDRESS = '127.0.0.1'

    static final String ACCESS_KEY = 'key'

    ObjectMapper objectMapper = Mock(ObjectMapper)

    RestTemplate restTemplate = Mock(RestTemplate)

    IpApiProvider provider = new IpApiProvider(ACCESS_KEY, objectMapper, restTemplate)

    def 'should get response from provider'() {
        given:
            restTemplate.getForEntity("http://api.ipapi.com/" + IP_ADDRESS + "?access_key=" + ACCESS_KEY, String.class) >>
                new ResponseEntity('test response', OK)
            objectMapper.readValue('test response', HashMap) >> ['latitude': 50.40, 'longitude': 23.34]
        when:
            LocationBean result = provider.getLocationByIp(IP_ADDRESS)
        then:
            result.clientIp == IP_ADDRESS
            result.latitude == 50.40
            result.longitude == 23.34
    }

    def 'should get empty bean'() {
        when:
            LocationBean result = provider.getEmptyLocationBean(IP_ADDRESS)
        then:
            result.empty
    }
}
