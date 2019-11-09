package com.rest.mintos.provider;

import com.rest.mintos.api.beans.LocationBean;

public interface LocationProvider {
    LocationBean getLocationByIp(String ipAdress);
}
