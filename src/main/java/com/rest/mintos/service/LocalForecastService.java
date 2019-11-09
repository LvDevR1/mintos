package com.rest.mintos.service;

import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.rest.mintos.domain.Forecast;
import com.rest.mintos.repository.ForecastRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
@CacheConfig(cacheNames={"forecasts"})
public class LocalForecastService {

    private final ForecastRepository forecastRepository;

    @Cacheable(cacheNames = "forecasts")
    public Optional<Forecast> getLastForecastByIp(String clientIp) {
        return forecastRepository.findByClientIpIgnoreCase(clientIp)
            .stream().findFirst();
    }
}
