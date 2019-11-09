package com.rest.mintos.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rest.mintos.domain.Forecast;

public interface ForecastRepository extends JpaRepository<Forecast, Long>, JpaSpecificationExecutor<Forecast> {

    List<Forecast> findByClientIpIgnoreCase(String clientIp);

    default List<Forecast> findByCoordinates(BigDecimal latitude, BigDecimal longitude) {
        return findAll(Specification.where(equalByLatitude(latitude)).and(equalByLongitude(longitude)));
    }

    default Specification<Forecast> equalByLatitude(BigDecimal latitude) {
        return (root, query, cb) -> cb.equal(root.get("latitude"), latitude);
    }

    default Specification<Forecast> equalByLongitude(BigDecimal longitude) {
        return (root, query, cb) -> cb.equal(root.get("longitude"), longitude);
    }

}