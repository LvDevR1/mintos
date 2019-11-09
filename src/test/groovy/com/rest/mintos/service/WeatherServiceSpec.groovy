package com.rest.mintos.service

import static com.rest.mintos.provider.OpenWeatherProvider.ACTUAL_STATUS
import static com.rest.mintos.utils.BigDecimalUtils.amount

import javax.servlet.http.HttpServletRequest

import org.springframework.core.convert.ConversionService

import com.rest.mintos.api.beans.ForecastBean
import com.rest.mintos.api.beans.LocationBean
import com.rest.mintos.api.beans.WeatherResponseBean
import com.rest.mintos.domain.Forecast
import com.rest.mintos.provider.ForecastProvider
import com.rest.mintos.repository.ForecastRepository

import spock.lang.Specification

class WeatherServiceSpec extends Specification {

    static final String IP_ADDRESS = '127.0.0.1'

    LocationService locationService = Mock(LocationService)

    ForecastRepository forecastRepository = Mock(ForecastRepository)

    ForecastProvider forecastProvider = Mock(ForecastProvider)

    ConversionService conversionService = Mock(ConversionService)

    WeatherService service = new WeatherService(locationService, forecastRepository, forecastProvider, conversionService)

    def 'should ger weather response from provider'() {
        given:
            HttpServletRequest request = Mock(HttpServletRequest)
            LocationBean locationBean = new LocationBean(longitude: 1.0, latitude: 1.0)
            WeatherResponseBean weatherResponseBean = new WeatherResponseBean(status: ACTUAL_STATUS, success: true, forecast: ['weather': 'cloud'])
            locationService.getLocationByIp(request) >> locationBean
            forecastProvider.getForecastResponse(locationBean) >> weatherResponseBean
        when:
            WeatherResponseBean result = service.getWeatherByIp(request)
        then:
            1 * forecastRepository.save(_ as Forecast)
            result == weatherResponseBean
    }

    def 'should get empty response if can not get location'() {
        given:
            HttpServletRequest request = Mock(HttpServletRequest)
            LocationBean locationBean = new LocationBean()
            locationService.getLocationByIp(request) >> locationBean
        when:
            WeatherResponseBean result = service.getWeatherByIp(request)
        then:
            !result.success
    }

    def 'should get historical data by ip '() {
        given:
            Forecast forecast1 = Mock(Forecast)
            Forecast forecast2 = Mock(Forecast)
            ForecastBean forecastBean1 = new ForecastBean()
            ForecastBean forecastBean2 = new ForecastBean()

            1 * forecastRepository.findByClientIpIgnoreCase(IP_ADDRESS) >> [forecast1, forecast2]
            conversionService.convert(forecast1, ForecastBean) >> forecastBean1
            conversionService.convert(forecast2, ForecastBean) >> forecastBean2
        when:
            List<ForecastBean> result = service.getHistoricalDataByIpAdress(IP_ADDRESS)
        then:
            !result.isEmpty()
            result.containsAll([forecastBean1, forecastBean2])
    }

    def 'should get historical data by coordinates '() {
        given:
            BigDecimal latitude = amount(10.0)
            BigDecimal longitude = amount(20.0)
            Forecast forecast1 = Mock(Forecast)
            Forecast forecast2 = Mock(Forecast)
            ForecastBean forecastBean1 = new ForecastBean()
            ForecastBean forecastBean2 = new ForecastBean()

            1 * forecastRepository.findByCoordinates(latitude, longitude) >> [forecast1, forecast2]
            conversionService.convert(forecast1, ForecastBean) >> forecastBean1
            conversionService.convert(forecast2, ForecastBean) >> forecastBean2
        when:
            List<ForecastBean> result = service.getHistoricalDataByCoordinates(latitude, longitude)
        then:
            !result.isEmpty()
            result.containsAll([forecastBean1, forecastBean2])
    }
}
