package com.rest.mintos.api.beans;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ForecastBean {

    private LocalDateTime requestDate;

    private String clientIp;

    private String descritption;

    private BigDecimal latitude;

    private BigDecimal longitude;


}
