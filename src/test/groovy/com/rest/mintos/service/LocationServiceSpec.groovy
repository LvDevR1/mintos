package com.rest.mintos.service

import javax.servlet.http.HttpServletRequest

import com.rest.mintos.api.beans.LocationBean
import com.rest.mintos.domain.Forecast
import com.rest.mintos.provider.LocationProvider

import spock.lang.Specification

class LocationServiceSpec extends Specification {

    static final String IP_ADDRESS = '127.0.0.1'

    LocationProvider locationProvider = Mock()

    LocalForecastService localForecastService = Mock()

    LocationService locationService = new LocationService(locationProvider, localForecastService)

    def 'should get information from database'() {
        given:
            HttpServletRequest request = Mock(HttpServletRequest)
            request.getRemoteAddr() >> IP_ADDRESS
            Forecast forecast = Mock(Forecast)
            forecast.getLatitude() >> 10.0
            forecast.getLongitude() >> 20.0
            1 * localForecastService.getLastForecastByIp(IP_ADDRESS) >> Optional.ofNullable(forecast)
        when:
            LocationBean result = locationService.getLocationByIp(request)
        then:
            0 * locationProvider.getLocationByIp(IP_ADDRESS)
            !result.empty
            result.latitude == 10.0
            result.longitude == 20.0
    }

    def 'should get information from provider'() {
        given:
            LocationBean locationBean = new LocationBean(latitude: 10.0, longitude: 20.0)
            HttpServletRequest request = Mock(HttpServletRequest)
            request.getRemoteAddr() >> IP_ADDRESS
            1 * localForecastService.getLastForecastByIp(IP_ADDRESS) >> Optional.ofNullable(null)
            1 * locationProvider.getLocationByIp(IP_ADDRESS) >> locationBean
        when:
            LocationBean result = locationService.getLocationByIp(request)
        then:

            !result.empty
            result.latitude == 10.0
            result.longitude == 20.0
    }
}
