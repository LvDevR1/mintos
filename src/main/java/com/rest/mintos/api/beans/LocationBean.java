package com.rest.mintos.api.beans;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LocationBean {

    BigDecimal latitude;
    BigDecimal longitude;
    String clientIp;

    public boolean isEmpty(){
        return latitude == null || longitude == null;
    }
}
