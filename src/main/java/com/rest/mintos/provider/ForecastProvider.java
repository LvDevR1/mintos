package com.rest.mintos.provider;

import com.rest.mintos.api.beans.LocationBean;
import com.rest.mintos.api.beans.WeatherResponseBean;

public interface ForecastProvider {

    WeatherResponseBean getForecastResponse(LocationBean locationBean);
}
