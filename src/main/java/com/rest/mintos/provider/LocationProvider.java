package com.rest.mintos.provider;

import java.util.HashMap;

public interface LocationProvider {
    HashMap getLocationByIp(String ipAdress);
}
