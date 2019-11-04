package com.rest.mintos.service;

import static java.time.LocalDateTime.now;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Component;

import com.rest.mintos.api.beans.LocationBean;
import com.rest.mintos.domain.Forecast;
import com.rest.mintos.provider.ForecastProvider;
import com.rest.mintos.repository.ForecastRepository;

@Component
public class WeatherService {

    private final LocationService locationService;
    private final ForecastRepository forecastRepository;
    private final ForecastProvider forecastProvider;

    public WeatherService(LocationService locationService, ForecastRepository forecastRepository, ForecastProvider forecastProvider) {
        this.locationService = locationService;
        this.forecastRepository = forecastRepository;
        this.forecastProvider = forecastProvider;
    }

    public String getWeatherByIp(HttpServletRequest request) {
        LocationBean locationBean = locationService.getLocationByIp(request);
        String weatherResponseBody = forecastProvider.getForecastResponse(locationBean);
        Forecast forecast = new Forecast();
        forecast.setClientIp(locationBean.getClientIp());
        forecast.setRequestDate(now());
        forecast.setDescritption(weatherResponseBody);
        forecast.setLatitude(locationBean.getLatitude());
        forecast.setLongitude(locationBean.getLongitude());
        forecastRepository.save(forecast);
        return weatherResponseBody;
    }


}
