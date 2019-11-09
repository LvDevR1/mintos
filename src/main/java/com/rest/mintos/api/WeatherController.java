package com.rest.mintos.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rest.mintos.api.beans.ForecastBean;
import com.rest.mintos.api.beans.WeatherResponseBean;
import com.rest.mintos.service.WeatherService;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(value = "/weather", produces = APPLICATION_JSON_VALUE)
    WeatherResponseBean getWeatherFromProvider(HttpServletRequest request) {
        return weatherService.getWeatherByIp(request);
    }

    @GetMapping(value = "historical/weather/{ipAdress}", produces = APPLICATION_JSON_VALUE)
    List<ForecastBean> getHistoricalDatabyIp(@PathVariable("ipAdress") String ipAdress) {
        return weatherService.getHistoricalDataByIpAdress(ipAdress);
    }

    @GetMapping(value = "historical/weather/{latitude}/{longitude}", produces = APPLICATION_JSON_VALUE)
    List<ForecastBean> getHistoricalDatabyCoordinates(@PathVariable("latitude") BigDecimal latitude, @PathVariable("longitude") BigDecimal longitude) {
        return weatherService.getHistoricalDataByCoordinates(latitude, longitude);
    }


}
