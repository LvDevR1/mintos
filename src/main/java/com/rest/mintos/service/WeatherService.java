package com.rest.mintos.service;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import com.rest.mintos.api.beans.ForecastBean;
import com.rest.mintos.api.beans.LocationBean;
import com.rest.mintos.api.beans.WeatherResponseBean;
import com.rest.mintos.domain.Forecast;
import com.rest.mintos.provider.ForecastProvider;
import com.rest.mintos.repository.ForecastRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class WeatherService {

    private final LocationService locationService;
    private final ForecastRepository forecastRepository;
    private final ForecastProvider forecastProvider;
    private final ConversionService conversionService;

    public WeatherResponseBean getWeatherByIp(HttpServletRequest request) {
        LocationBean locationBean = locationService.getLocationByIp(request);
        if(locationBean.isEmpty()){
            return new WeatherResponseBean(false, "Could not determine your location" , null);
        }
        WeatherResponseBean weatherResponseBean = forecastProvider.getForecastResponse(locationBean);
        saveForecast(locationBean, weatherResponseBean);
        return weatherResponseBean;
    }

    private void saveForecast(LocationBean locationBean, WeatherResponseBean weatherResponseBean){
        Forecast forecast = new Forecast();
        forecast.setClientIp(locationBean.getClientIp());
        forecast.setRequestDate(now());
        forecast.setDescritption(weatherResponseBean.getForecast().toString());
        forecast.setLatitude(locationBean.getLatitude());
        forecast.setLongitude(locationBean.getLongitude());
        forecastRepository.save(forecast);
    }

    public List<ForecastBean> getHistoricalDataByIpAdress(String ipAdress) {
        List<Forecast> forecasts =  forecastRepository.findByClientIpIgnoreCase(ipAdress);
        return forecasts.stream().map(it -> conversionService.convert(it, ForecastBean.class)).collect(toList());
    }

    public List<ForecastBean> getHistoricalDataByCoordinates(BigDecimal latitude, BigDecimal longitude) {
        List<Forecast> forecasts =  forecastRepository.findByCoordinates(latitude, longitude);
        return forecasts.stream().map(it -> conversionService.convert(it, ForecastBean.class)).collect(toList());
    }


}
