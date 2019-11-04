package com.rest.mintos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.mintos.domain.Forecast;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    Forecast findByClientIpIgnoreCase(String clientIp);

}