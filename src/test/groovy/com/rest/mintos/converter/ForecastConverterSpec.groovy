package com.rest.mintos.converter

import static com.rest.mintos.utils.BigDecimalUtils.amount
import static java.time.LocalDateTime.now

import java.time.LocalDateTime

import com.rest.mintos.api.beans.ForecastBean
import com.rest.mintos.domain.Forecast

import spock.lang.Specification

class ForecastConverterSpec extends Specification {

    ForecastConverter converter = new ForecastConverter()

    def 'should convert currency to bean'() {
        given:
            LocalDateTime requestDate = now()
            Forecast forecast = new Forecast(id: 1l, requestDate: requestDate, clientIp: '127.0.0.1', descritption: 'Test forecast', latitude: amount('10.40'),
                longitude: amount('23.44'))
        when:
            ForecastBean result = converter.convert(forecast)
        then:
            result.clientIp == '127.0.0.1'
            result.descritption == 'Test forecast'
            result.latitude == amount('10.40')
            result.longitude == amount('23.44')
            result.requestDate == requestDate
    }

}
