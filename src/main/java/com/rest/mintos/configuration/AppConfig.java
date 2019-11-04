package com.rest.mintos.configuration;

import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableCaching
public class AppConfig  extends CachingConfigurerSupport {

    //@Bean
    //@Override
    public CacheManager cacheManager() {
          // configure and return an implementation of Spridng's CacheManager SPI
          SimpleCacheManager cacheManager = new SimpleCacheManager();
          cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("forecasts"), new ConcurrentMapCache("weathers")));
          return cacheManager;
      }

}
