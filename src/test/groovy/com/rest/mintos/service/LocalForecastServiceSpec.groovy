package com.rest.mintos.service

import com.rest.mintos.domain.Forecast
import com.rest.mintos.repository.ForecastRepository

import spock.lang.Specification

class LocalForecastServiceSpec extends Specification {

    static final String IP_ADDRESS = '127.0.0.1'

    ForecastRepository forecastRepository = Mock(ForecastRepository)

    LocalForecastService service = new LocalForecastService(forecastRepository)

    def 'should get all forecasts by IP'() {
        given:
            Forecast forecast = Mock(Forecast)
            forecastRepository.findByClientIpIgnoreCase(IP_ADDRESS) >> [forecast]
        when:
            Optional<Forecast> result = service.getLastForecastByIp(IP_ADDRESS)
        then:
            result.get() == forecast
    }
}
