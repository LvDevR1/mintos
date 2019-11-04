package com.rest.mintos.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rest.mintos.domain.Forecast;
import com.rest.mintos.service.WeatherService;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(value = "/weather", produces = APPLICATION_JSON_VALUE)
    String getCurrencyByCode(HttpServletRequest request) {
        return weatherService.getWeatherByIp(request);
    }


    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    List<Forecast> getALL(HttpServletRequest request) {
        return null;
    }

}
