package com.rest.mintos.configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;

@Configuration
public class AppConfig extends CachingConfigurerSupport {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CacheManager cacheManager(Ticker ticker) {
        CaffeineCache weathersCache = buildCache("weathers", ticker, 60);
        CaffeineCache forecastsCache = buildCache("forecasts", ticker, 120);
        CaffeineCache locationsCache = buildCache("locations", ticker, 120);
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(weathersCache, forecastsCache, locationsCache));
        return manager;
    }

    private CaffeineCache buildCache(String name, Ticker ticker, int minutesToExpire) {
        return new CaffeineCache(name, Caffeine.newBuilder()
            .expireAfterWrite(minutesToExpire, TimeUnit.MINUTES)
            .maximumSize(100)
            .ticker(ticker)
            .build());
    }

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }
}
