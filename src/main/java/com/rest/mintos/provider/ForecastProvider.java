package com.rest.mintos.provider;

import com.rest.mintos.api.beans.LocationBean;

public interface ForecastProvider {

    String getForecastResponse(LocationBean locationBean);
}
