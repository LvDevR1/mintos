package com.rest.mintos.api.beans;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherResponseBean {

    Boolean success;

    String status;

    Map forecast;

    public WeatherResponseBean(){}
}
