package com.rest.mintos.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.rest.mintos.api.beans.LocationBean;
import com.rest.mintos.domain.Forecast;
import com.rest.mintos.provider.LocationProvider;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class LocationService {

    private final LocationProvider locationProvider;

    private final LocalForecastService localForecastService;

    public LocationBean getLocationByIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        ipAddress = isEmpty(ipAddress) ? request.getRemoteAddr() : ipAddress;
        Optional<Forecast> lastForecast = localForecastService.getLastForecastByIp(ipAddress);
        String finalIpAddress = ipAddress;
        return lastForecast.map(it -> new LocationBean(it.getLatitude(), it.getLongitude(), finalIpAddress))
            .orElseGet(() -> getLocationFromProvider(finalIpAddress));
    }

    private LocationBean getLocationFromProvider(String ipAddress) {
       return locationProvider.getLocationByIp(ipAddress);
    }

}
