package com.rest.mintos.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "FORECASTS")
public class Forecast {

    private @Id
    @GeneratedValue
    Long id;

    private LocalDateTime requestDate;

    private String clientIp;

    private String descritption;

    private BigDecimal latitude;

    private BigDecimal longitude;

    public Forecast() {
    }

}
