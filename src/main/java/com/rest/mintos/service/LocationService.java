package com.rest.mintos.service;

import static java.lang.String.valueOf;
import static org.springframework.util.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.mintos.api.beans.LocationBean;
import com.rest.mintos.provider.LocationProvider;

@Component
public class LocationService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    LocationProvider locationProvider;

    public LocationBean getLocationByIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        ipAddress = isEmpty(ipAddress) ? request.getRemoteAddr() : ipAddress;

        HashMap locationMap = locationProvider.getLocationByIp(ipAddress);
        BigDecimal lattitude = new BigDecimal(valueOf(locationMap.get("latitude")));
        BigDecimal longtitude = new BigDecimal(valueOf(locationMap.get("longitude")));

        return new LocationBean(lattitude, longtitude, ipAddress);
    }

}
